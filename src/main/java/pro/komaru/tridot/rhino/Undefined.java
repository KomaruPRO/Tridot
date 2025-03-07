/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino;

import java.io.*;
import java.lang.reflect.*;

/**
 * This class implements the Undefined value in JavaScript.
 */
public class Undefined implements Serializable {
	public static final Object instance = new Undefined();
	public static final Scriptable SCRIPTABLE_UNDEFINED;

	static {
		SCRIPTABLE_UNDEFINED = (Scriptable) Proxy.newProxyInstance(Undefined.class.getClassLoader(), new Class[]{Scriptable.class}, (proxy, method, args) -> {
			if (method.getName().equals("toString")) {
				return "undefined";
			}
			if (method.getName().equals("equals")) {
				return args.length > 0 && isUndefined(args[0]);
			}
			throw new UnsupportedOperationException("undefined doesn't support " + method.getName());
		});
	}

	public static boolean isUndefined(Object obj) {
		return Undefined.instance == obj || Undefined.SCRIPTABLE_UNDEFINED == obj;
	}

	private Undefined() {
	}

	@Serial
	public Object readResolve() {
		return instance;
	}

	@Override
	public boolean equals(Object obj) {
		return isUndefined(obj) || super.equals(obj);
	}

	@Override
	public int hashCode() {
		// All instances of Undefined are equivalent!
		return 0;
	}
}
