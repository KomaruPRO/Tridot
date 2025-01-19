package pro.komaru.tridot.core.rhino;

import pro.komaru.tridot.core.struct.*;
import pro.komaru.tridot.core.struct.data.Pair;
import pro.komaru.tridot.core.struct.data.Seq;
import pro.komaru.tridot.rhino.*;
import pro.komaru.tridot.rhino.util.wrap.*;

import java.lang.reflect.*;
import java.util.*;

public class JS {
    public static Pair<Context, ScriptableObject> begin() {
        Context context = Context.enter();
        TypeWrappers wraps = context.getTypeWrappers();
        Wrappers.register(wraps);
        ScriptableObject scope = context.initStandardObjects(new TopLevel());
        return new Pair<>(context,scope);
    };
    public static void run(Context context, ScriptableObject scope, String body, String filename) {
        context.evaluateString(scope,body,filename,1,null);
    }
    public static void addToScope(ScriptableObject scope, Context ctx, Object object, boolean fields, boolean methods) {
        if(fields)
            for (Field field : object.getClass().getFields()) {
                if(!field.isAnnotationPresent(Scope.class)) continue;
                try {
                    ScriptableObject.putConstProperty(
                            scope,field.getName(),field.get(object),ctx
                    );
                    Alias[] aliases = field.getAnnotationsByType(Alias.class);
                    for (Alias alias : aliases) {
                        ScriptableObject.putConstProperty(
                                scope,alias.value(),field.get(object),ctx
                        );
                    }
                } catch (IllegalAccessException e) {

                }
            }
        if(methods) {
            HashMap<String, Seq<Method>> mthds = new HashMap<>();
            for (Method method : object.getClass().getMethods()) {
                if (!method.isAnnotationPresent(Scope.class)) continue;
                mthds.putIfAbsent(method.getName(),Seq.with());
                mthds.get(method.getName()).add(method);
                Alias[] aliases = method.getAnnotationsByType(Alias.class);
                for (Alias alias : aliases) {
                    mthds.putIfAbsent(alias.value(),Seq.with());
                    mthds.get(alias.value()).add(method);
                }
            }
            mthds.forEach((k,v) -> {
                Seq<MemberBox> members = Seq.with();
                for (Method method : v) {
                    members.add(new MemberBox(method));
                }
                MethodWrapperFunction fn = new MethodWrapperFunction(members.toArray(),object);
                ScriptableObject.putConstProperty(
                        scope,k,fn,ctx
                );
            });
        }
    }
    public static void addToScope(ScriptableObject scope, Context ctx, Class<?> clazz, boolean fields, boolean methods) {
        if (fields) {
            for (Field field : clazz.getFields()) {
                if (!field.isAnnotationPresent(Scope.class)) continue;
                try {
                    ScriptableObject.putConstProperty(scope, field.getName(), field.get(null), ctx); // Для классов используется null
                    Alias[] aliases = field.getAnnotationsByType(Alias.class);
                    for (Alias alias : aliases) {
                        ScriptableObject.putConstProperty(scope, alias.value(), field.get(null), ctx);
                    }
                } catch (IllegalAccessException | NullPointerException e) {
                    // Обработка исключения, если нужно
                }
            }
        }

        if (methods) {
            HashMap<String, Seq<Method>> mthds = new HashMap<>();
                for (Method method : clazz.getMethods()) {
                    if (!method.isAnnotationPresent(Scope.class)) continue;
                    try {
                        mthds.putIfAbsent(method.getName(), Seq.with());
                        mthds.get(method.getName()).add(method);
                        Alias[] aliases = method.getAnnotationsByType(Alias.class);
                        for (Alias alias : aliases) {
                            mthds.putIfAbsent(alias.value(), Seq.with());
                            mthds.get(alias.value()).add(method);
                        }
                    } catch (NullPointerException ignored) {

                    }
                }

            mthds.forEach((k, v) -> {
                Seq<MemberBox> members = Seq.with();
                for (Method method : v) {
                    members.add(new MemberBox(method));
                }
                MethodWrapperFunction fn = new MethodWrapperFunction(members.toArray(), null); // Здесь тоже null, так как мы работаем с классом
                ScriptableObject.putConstProperty(scope, k, fn, ctx);
            });
        }
    }


    public static class MethodWrapperFunction extends NativeJavaMethod {

        public Object self;
        public MethodWrapperFunction(MemberBox[] methods, Object self) {
            super(methods);
            this.self = self;
        }
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            // Find a method that matches the types given.
            if (methods.length == 0) {
                throw new RuntimeException("No methods defined for call");
            }

            int index = findCachedFunction(cx, args);
            if (index < 0) {
                Class<?> c = methods[0].getDeclaringClass();
                String sig = c.getName() + '.' + getFunctionName() + '(' + scriptSignature(args) + ')';
                throw Context.reportRuntimeError1("msg.java.no_such_method", sig, cx);
            }

            MemberBox meth = methods[index];
            Class<?>[] argTypes = meth.argTypes;

            if (meth.vararg) {
                // marshall the explicit parameters
                Object[] newArgs = new Object[argTypes.length];
                for (int i = 0; i < argTypes.length - 1; i++) {
                    newArgs[i] = Context.jsToJava(cx, args[i], argTypes[i]);
                }

                Object varArgs;

                // Handle special situation where a single variable parameter
                // is given and it is a Java or ECMA array or is null.
                if (args.length == argTypes.length && (args[args.length - 1] == null || args[args.length - 1] instanceof NativeArray || args[args.length - 1] instanceof NativeJavaArray)) {
                    // convert the ECMA array into a native array
                    varArgs = Context.jsToJava(cx, args[args.length - 1], argTypes[argTypes.length - 1]);
                } else {
                    // marshall the variable parameters
                    Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
                    varArgs = Array.newInstance(componentType, args.length - argTypes.length + 1);
                    for (int i = 0; i < Array.getLength(varArgs); i++) {
                        Object value = Context.jsToJava(cx, args[argTypes.length - 1 + i], componentType);
                        Array.set(varArgs, i, value);
                    }
                }

                // add varargs
                newArgs[argTypes.length - 1] = varArgs;
                // replace the original args with the new one
                args = newArgs;
            } else {
                // First, we marshall the args.
                Object[] origArgs = args;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    Object coerced = arg;

				/*
				if (arg != null) {
					TypeWrapperFactory<?> factory = argTypes[i] != null && cx.hasTypeWrappers() ? cx.getTypeWrappers().getWrapperFactory(argTypes[i], arg) : null;

					if (factory != null) {
						coerced = factory.wrap(arg);
					}
				}
				 */

                    coerced = Context.jsToJava(cx, coerced, argTypes[i]);

                    if (coerced != arg) {
                        if (origArgs == args) {
                            args = args.clone();
                        }
                        args[i] = coerced;
                    }
                }
            }
            Object javaObject;
            if (meth.isStatic()) {
                javaObject = null;  // don't need an object
            } else {
                Scriptable o = cx.getWrapFactory().wrapAsJavaObject(cx,scope,self,self.getClass());
                Class<?> c = meth.getDeclaringClass();
                for (; ; ) {
                    if (o == null) {
                        throw Context.reportRuntimeError3("msg.nonjava.method", getFunctionName(), ScriptRuntime.toString(cx, o), c.getName(), cx);
                    }
                    if (o instanceof Wrapper) {
                        javaObject = ((Wrapper) o).unwrap();
                        if (c.isInstance(javaObject)) {
                            break;
                        }
                    }
                    o = o.getPrototype(cx);
                }
            }
            if (debug) {
                printDebug("Calling ", meth, args);
            }

            Object retval = meth.invoke(javaObject, args, cx, scope);
            Class<?> staticType = meth.getReturnType();

            if (debug) {
                Class<?> actualType = (retval == null) ? null : retval.getClass();
                System.err.println(" ----- Returned " + retval + " actual = " + actualType + " expect = " + staticType);
            }

            Object wrapped = cx.getWrapFactory().wrap(cx, scope, retval, staticType);
            if (debug) {
                Class<?> actualType = (wrapped == null) ? null : wrapped.getClass();
                System.err.println(" ----- Wrapped as " + wrapped + " class = " + actualType);
            }

            if (wrapped == null && staticType == Void.TYPE) {
                wrapped = Undefined.instance;
            }
            return wrapped;
        }
    }
}
