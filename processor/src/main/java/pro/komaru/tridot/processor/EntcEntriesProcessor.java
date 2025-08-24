package pro.komaru.tridot.processor;

import com.moandjiezana.toml.Toml;
import com.squareup.javapoet.*;
import lombok.Data;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SupportedAnnotationTypes(EntcEntriesProcessor.ENTRY_ANNOTATION)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class EntcEntriesProcessor extends AbstractProcessor {

    public static final String
        ENTRY_ANNOTATION = "pro.komaru.tridot.api.anno.EntcEntries",
        ENTC_COMPONENT_CONTAINER = "pro.komaru.tridot.core.entity.entc.EntcCompContainer",
        ENTC_DISPATCHER = "pro.komaru.tridot.core.entity.entc.EntcDispatcher",
        ENTC_MANAGER = "pro.komaru.tridot.core.entity.entc.EntcManager"
                ;

    private Filer filer;
    private Messager messager;
    private List<MethodBridgeData> methodBridges = new ArrayList<>();
    @Data
    public static class MethodBridgeData {
        private String methodName;
        private String eventName;
        private List<String> args;
        private String returns;
        private boolean cancellable;
        private String order;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        try {
            methodBridges = loadConfig();
            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Loaded " + methodBridges.size() + " method bridges from entitycomp_config.toml");
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to load entitycomp_config.toml: " + e.getMessage());
        }
    }

    public static List<MethodBridgeData> loadConfig() {
        InputStream is = EntcEntriesProcessor.class.getClassLoader()
                .getResourceAsStream("entitycomp_config.toml");

        if (is == null) {
            throw new IllegalStateException("Config entitycomp_config.toml not found in resources!");
        }

        Toml toml = new Toml().read(is);
        List<MethodBridgeData> methods = new ArrayList<>();

        for (String table : toml.toMap().keySet()) {
            List<Toml> entries = toml.getTables(table);
            for (Toml entry : entries) {
                MethodBridgeData data = new MethodBridgeData();
                data.setEventName(table);
                data.setMethodName(entry.getString("name"));
                data.setArgs(entry.getList("args", new ArrayList<>()));
                data.setCancellable(Boolean.TRUE.equals(entry.getBoolean("cancellable", false)));
                data.setOrder(entry.getString("order", "START"));
                data.setReturns(entry.getString("returns", "void"));
                methods.add(data);
            }
        }
        return methods;
    }

    private static TypeName resolveType(String type) {
        return switch (type) {
            case "void" -> TypeName.VOID;
            case "int" -> TypeName.INT;
            case "boolean" -> TypeName.BOOLEAN;
            case "float" -> TypeName.FLOAT;
            case "double" -> TypeName.DOUBLE;
            case "long" -> TypeName.LONG;
            case "byte" -> TypeName.BYTE;
            case "short" -> TypeName.SHORT;
            case "char" -> TypeName.CHAR;
            default -> ClassName.bestGuess(type);
        };
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement annotationType = processingEnv.getElementUtils()
                .getTypeElement(ENTRY_ANNOTATION);
        for (Element e : roundEnv.getElementsAnnotatedWith(annotationType)) {
            if (!(e instanceof TypeElement typeElement)) continue;

            for (Element field : typeElement.getEnclosedElements()) {
                if (!(field instanceof VariableElement var)) continue;

                String fieldTypeFqn = var.asType().toString();
                String fieldTypeName = var.asType().toString();
                String simpleName = fieldTypeFqn.substring(fieldTypeFqn.lastIndexOf('.') + 1);
                String wrapperName = simpleName.endsWith("Entity") ? simpleName.replaceAll("Entity$", "Entc") : simpleName + "Entc";

                try {
                    generateWrapper(wrapperName, fieldTypeFqn);
                } catch (IOException ex) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate " + wrapperName + ": " + ex);
                }
            }
        }
        return true;
    }

    private void generateWrapper(String wrapperName, String originalFqn) throws IOException {
        TypeElement superType = processingEnv.getElementUtils().getTypeElement(originalFqn);
        MethodSpec ctor = generateConstructor(superType);

        TypeSpec.Builder clazz = TypeSpec.classBuilder(wrapperName)
                .superclass(ClassName.bestGuess(originalFqn))
                .addSuperinterface(ClassName.bestGuess(ENTC_DISPATCHER))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(ctor);
        if(superType.getModifiers().contains(Modifier.ABSTRACT))
            clazz.addModifiers(Modifier.ABSTRACT);

        generateEntcIntegration(clazz);

        Map<String, List<MethodBridgeData>> grouped = methodBridges.stream()
                .collect(Collectors.groupingBy(b ->
                        b.getMethodName() + "(" + String.join(",", b.getArgs()) + "):" + b.getReturns()
                ));

        for (List<MethodBridgeData> group : grouped.values()) {
            MethodBridgeData first = group.get(0);

            MethodSpec.Builder method = MethodSpec.methodBuilder(first.getMethodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);

            for (int i = 0; i < first.getArgs().size(); i++) {
                method.addParameter(resolveType(first.getArgs().get(i)), "arg" + i);
            }

            TypeName returnType = "void".equals(first.getReturns())
                    ? TypeName.VOID
                    : resolveType(first.getReturns());
            method.returns(returnType);

            CodeBlock.Builder body = CodeBlock.builder();

            group.stream()
                    .filter(b -> "START".equals(b.getOrder()))
                    .forEach(b -> {
                        body.add("// injected " + b.getEventName()+"\n");
                        if (b.isCancellable()) {
                            body.add("// cancellable logic\n");
                        }
                    });

            String superCall = "super." + first.getMethodName() + "(" +
                    IntStream.range(0, first.getArgs().size())
                            .mapToObj(i -> "arg" + i)
                            .collect(Collectors.joining(", ")) + ")";

            if ("void".equals(first.getReturns())) {
                body.addStatement(superCall);

                group.stream()
                        .filter(b -> "END".equals(b.getOrder()))
                        .forEach(b -> {
                            body.add("// injected " + b.getEventName()+"\n");
                        });

            } else {
                String resultVar = "result";
                body.addStatement("$T " + resultVar + " = " + superCall, returnType);

                group.stream()
                        .filter(b -> "END".equals(b.getOrder()))
                        .forEach(b -> {
                            body.add("// injected " + b.getEventName()+"\n");
                        });

                body.addStatement("return " + resultVar);
            }

            method.addCode(body.build());
            clazz.addMethod(method.build());
        }

        JavaFile.builder("pro.komaru.tridot.gen", clazz.build())
                .indent("    ")
                .addFileComment("- Generated by Ent-C annotation processor\n")
                .addFileComment("Integrated Ent-C system to the "+originalFqn+" class")
                .build()
                .writeTo(filer);
    }

    private void generateEntcIntegration(TypeSpec.Builder clazz) {
        ClassName entcHooks = ClassName.get(
                "pro.komaru.tridot.core.entity.entc",
                "EntcHooks"
        );

        FieldSpec compContainerField = FieldSpec.builder(ClassName.bestGuess(ENTC_COMPONENT_CONTAINER), "componentContainer")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T(this)", ClassName.bestGuess(ENTC_COMPONENT_CONTAINER))
                .build();
        FieldSpec entcManagerField = FieldSpec.builder(ClassName.bestGuess(ENTC_MANAGER), "entc")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T(this,this)", ClassName.bestGuess(ENTC_MANAGER))
                .build();

        MethodSpec compContainerMethod = MethodSpec.methodBuilder("componentContainer")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(ENTC_COMPONENT_CONTAINER))
                .addAnnotation(Override.class)
                .addStatement("return this.componentContainer")
                .build();

        MethodSpec initializeEntcMethod = MethodSpec.methodBuilder("initEntc")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N.init()", entcManagerField)
                .build();

        clazz.addField(compContainerField);
        clazz.addField(entcManagerField);
        clazz.addMethod(compContainerMethod);
        clazz.addMethod(initializeEntcMethod);
    }

    private MethodSpec generateConstructor(TypeElement superClass) {
        for (Element e : superClass.getEnclosedElements()) {
            if (e.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) e;

                MethodSpec.Builder ctor = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC);

                StringBuilder superCall = new StringBuilder("super(");
                List<String> paramNames = new ArrayList<>();

                int i = 0;
                for (VariableElement param : constructor.getParameters()) {
                    TypeName paramType = TypeName.get(param.asType());
                    String paramName = param.getSimpleName().toString();
                    if (paramName.isEmpty()) paramName = "arg" + i;
                    paramNames.add(paramName);

                    ctor.addParameter(paramType, paramName);
                    superCall.append(paramName);
                    if (i < constructor.getParameters().size() - 1) {
                        superCall.append(", ");
                    }
                    i++;
                }
                superCall.append(")");

                ctor.addStatement(superCall.toString());

                ctor.addStatement("initEntc()");

                return ctor.build();
            }
        }

        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("// no-args constructor (super has no ctor?)")
                .build();
    }
}
