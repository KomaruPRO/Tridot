/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.annotations;

import pro.komaru.tridot.rhino.Scriptable;
import pro.komaru.tridot.rhino.ScriptableObject;
import pro.komaru.tridot.rhino.Context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that marks a Java method as JavaScript function. This can
 * be used as an alternative to the <code>jsFunction_</code> prefix described in
 * {@link ScriptableObject#defineClass(Scriptable, Class, boolean, boolean, Context)}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JSFunction {
	String value() default "";
}
