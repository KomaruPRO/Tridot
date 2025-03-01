/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino;

import java.util.*;

/**
 * <p>This class represents a string composed of two components, each of which
 * may be a <code>java.lang.String</code> or another ConsString.</p>
 *
 * <p>This string representation is optimized for concatenation using the "+"
 * operator. Instead of immediately copying both components to a new character
 * array, ConsString keeps references to the original components and only
 * converts them to a String if either toString() is called or a certain depth
 * level is reached.</p>
 *
 * <p>Note that instances of this class are only immutable if both parts are
 * immutable, i.e. either Strings or ConsStrings that are ultimately composed
 * of Strings.</p>
 *
 * <p>Both the name and the concept are borrowed from V8.</p>
 */
public class ConsString implements CharSequence {
	private final int length;
	private CharSequence left;
	private CharSequence right;
	private boolean isFlat;

	public ConsString(CharSequence str1, CharSequence str2) {
		left = str1;
		right = str2;
		length = left.length() + right.length();
		isFlat = false;
	}

	@Override
	public String toString() {
		return isFlat ? (String) left : flatten();
	}

	private synchronized String flatten() {
		if (!isFlat) {
			final char[] chars = new char[length];
			int charPos = length;

			ArrayDeque<CharSequence> stack = new ArrayDeque<>();
			stack.addFirst(left);

			CharSequence next = right;
			do {
				if (next instanceof ConsString casted) {
					if (casted.isFlat) {
						next = casted.left;
					} else {
						stack.addFirst(casted.left);
						next = casted.right;
						continue;
					}
				}

				final String str = (String) next;
				charPos -= str.length();
				str.getChars(0, str.length(), chars, charPos);
				next = stack.isEmpty() ? null : stack.removeFirst();
			} while (next != null);

			left = new String(chars);
			right = "";
			isFlat = true;
		}
		return (String) left;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public char charAt(int index) {
		String str = isFlat ? (String) left : flatten();
		return str.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		String str = isFlat ? (String) left : flatten();
		return str.substring(start, end);
	}
}
