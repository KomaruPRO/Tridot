/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino;

import java.util.*;

/**
 * Implementation of resizable array with focus on minimizing memory usage by storing few initial array elements in object fields. Can also be used as a stack.
 */

public class ObjArray {
	private static final int FIELDS_STORE_SIZE = 5;

	private static RuntimeException onInvalidIndex(int index, int upperBound) {
		// \u2209 is "NOT ELEMENT OF"
		String msg = index + " \u2209 [0, " + upperBound + ')';
		throw new IndexOutOfBoundsException(msg);
	}

	private static RuntimeException onEmptyStackTopRead() {
		throw new RuntimeException("Empty stack");
	}

	private static RuntimeException onSeledMutation() {
		throw new IllegalStateException("Attempt to modify sealed array");
	}

	// Number of data elements
	private int size;
	private boolean sealed;
	private transient Object f0, f1, f2, f3, f4;
	private transient Object[] data;

	public ObjArray() {
	}

	public final boolean isSealed() {
		return sealed;
	}

	public final void seal() {
		sealed = true;
	}

	public final boolean isEmpty() {
		return size == 0;
	}

	public final int size() {
		return size;
	}

	public final void setSize(int newSize) {
		if (newSize < 0) {
			throw new IllegalArgumentException();
		}
		if (sealed) {
			throw onSeledMutation();
		}
		int N = size;
		if (newSize < N) {
			for (int i = newSize; i != N; ++i) {
				setImpl(i, null);
			}
		} else if (newSize > N) {
			if (newSize > FIELDS_STORE_SIZE) {
				ensureCapacity(newSize);
			}
		}
		size = newSize;
	}

	public final Object get(int index) {
		if (!(0 <= index && index < size)) {
			throw onInvalidIndex(index, size);
		}
		return getImpl(index);
	}

	public final void set(int index, Object value) {
		if (!(0 <= index && index < size)) {
			throw onInvalidIndex(index, size);
		}
		if (sealed) {
			throw onSeledMutation();
		}
		setImpl(index, value);
	}

	private Object getImpl(int index) {
		return switch (index) {
			case 0 -> f0;
			case 1 -> f1;
			case 2 -> f2;
			case 3 -> f3;
			case 4 -> f4;
			default -> data[index - FIELDS_STORE_SIZE];
		};
	}

	private void setImpl(int index, Object value) {
		switch (index) {
			case 0 -> f0 = value;
			case 1 -> f1 = value;
			case 2 -> f2 = value;
			case 3 -> f3 = value;
			case 4 -> f4 = value;
			default -> data[index - FIELDS_STORE_SIZE] = value;
		}

	}

	public int indexOf(Object obj) {
		int N = size;
		for (int i = 0; i != N; ++i) {
			Object current = getImpl(i);
			if (Objects.equals(current, obj)) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(Object obj) {
		for (int i = size; i != 0; ) {
			--i;
			Object current = getImpl(i);
			if (Objects.equals(current, obj)) {
				return i;
			}
		}
		return -1;
	}

	public final Object peek() {
		int N = size;
		if (N == 0) {
			throw onEmptyStackTopRead();
		}
		return getImpl(N - 1);
	}

	public final Object pop() {
		if (sealed) {
			throw onSeledMutation();
		}
		int N = size;
		--N;
		Object top;
		switch (N) {
			case -1 -> throw onEmptyStackTopRead();
			case 0 -> {
				top = f0;
				f0 = null;
			}
			case 1 -> {
				top = f1;
				f1 = null;
			}
			case 2 -> {
				top = f2;
				f2 = null;
			}
			case 3 -> {
				top = f3;
				f3 = null;
			}
			case 4 -> {
				top = f4;
				f4 = null;
			}
			default -> {
				top = data[N - FIELDS_STORE_SIZE];
				data[N - FIELDS_STORE_SIZE] = null;
			}
		}
		size = N;
		return top;
	}

	public final void push(Object value) {
		add(value);
	}

	public final void add(Object value) {
		if (sealed) {
			throw onSeledMutation();
		}
		int N = size;
		if (N >= FIELDS_STORE_SIZE) {
			ensureCapacity(N + 1);
		}
		size = N + 1;
		setImpl(N, value);
	}

	public final void add(int index, Object value) {
		int N = size;
		if (!(0 <= index && index <= N)) {
			throw onInvalidIndex(index, N + 1);
		}
		if (sealed) {
			throw onSeledMutation();
		}
		Object tmp;
		switch (index) {
			case 0:
				if (N == 0) {
					f0 = value;
					break;
				}
				tmp = f0;
				f0 = value;
				value = tmp;
				/* fall through */
			case 1:
				if (N == 1) {
					f1 = value;
					break;
				}
				tmp = f1;
				f1 = value;
				value = tmp;
				/* fall through */
			case 2:
				if (N == 2) {
					f2 = value;
					break;
				}
				tmp = f2;
				f2 = value;
				value = tmp;
				/* fall through */
			case 3:
				if (N == 3) {
					f3 = value;
					break;
				}
				tmp = f3;
				f3 = value;
				value = tmp;
				/* fall through */
			case 4:
				if (N == 4) {
					f4 = value;
					break;
				}
				tmp = f4;
				f4 = value;
				value = tmp;

				index = FIELDS_STORE_SIZE;
				/* fall through */
			default:
				ensureCapacity(N + 1);
				if (index != N) {
					System.arraycopy(data, index - FIELDS_STORE_SIZE, data, index - FIELDS_STORE_SIZE + 1, N - index);
				}
				data[index - FIELDS_STORE_SIZE] = value;
		}
		size = N + 1;
	}

	public final void remove(int index) {
		int N = size;
		if (!(0 <= index && index < N)) {
			throw onInvalidIndex(index, N);
		}
		if (sealed) {
			throw onSeledMutation();
		}
		--N;
		switch (index) {
			case 0:
				if (N == 0) {
					f0 = null;
					break;
				}
				f0 = f1;
				/* fall through */
			case 1:
				if (N == 1) {
					f1 = null;
					break;
				}
				f1 = f2;
				/* fall through */
			case 2:
				if (N == 2) {
					f2 = null;
					break;
				}
				f2 = f3;
				/* fall through */
			case 3:
				if (N == 3) {
					f3 = null;
					break;
				}
				f3 = f4;
				/* fall through */
			case 4:
				if (N == 4) {
					f4 = null;
					break;
				}
				f4 = data[0];

				index = FIELDS_STORE_SIZE;
				/* fall through */
			default:
				if (index != N) {
					System.arraycopy(data, index - FIELDS_STORE_SIZE + 1, data, index - FIELDS_STORE_SIZE, N - index);
				}
				data[N - FIELDS_STORE_SIZE] = null;
		}
		size = N;
	}

	public final void clear() {
		if (sealed) {
			throw onSeledMutation();
		}
		int N = size;
		for (int i = 0; i != N; ++i) {
			setImpl(i, null);
		}
		size = 0;
	}

	public final Object[] toArray() {
		Object[] array = new Object[size];
		toArray(array, 0);
		return array;
	}

	public final void toArray(Object[] array) {
		toArray(array, 0);
	}

	public final void toArray(Object[] array, int offset) {
		int N = size;
		switch (N) {
			default:
				System.arraycopy(data, 0, array, offset + FIELDS_STORE_SIZE, N - FIELDS_STORE_SIZE);
				/* fall through */
			case 5:
				array[offset + 4] = f4;
				/* fall through */
			case 4:
				array[offset + 3] = f3;
				/* fall through */
			case 3:
				array[offset + 2] = f2;
				/* fall through */
			case 2:
				array[offset + 1] = f1;
				/* fall through */
			case 1:
				array[offset] = f0;
				/* fall through */
			case 0:
				break;
		}
	}

	private void ensureCapacity(int minimalCapacity) {
		int required = minimalCapacity - FIELDS_STORE_SIZE;
		if (required <= 0) {
			throw new IllegalArgumentException();
		}
		if (data == null) {
			int alloc = FIELDS_STORE_SIZE * 2;
			if (alloc < required) {
				alloc = required;
			}
			data = new Object[alloc];
		} else {
			int alloc = data.length;
			if (alloc < required) {
				if (alloc <= FIELDS_STORE_SIZE) {
					alloc = FIELDS_STORE_SIZE * 2;
				} else {
					alloc *= 2;
				}
				if (alloc < required) {
					alloc = required;
				}
				Object[] tmp = new Object[alloc];
				if (size > FIELDS_STORE_SIZE) {
					System.arraycopy(data, 0, tmp, 0, size - FIELDS_STORE_SIZE);
				}
				data = tmp;
			}
		}
	}
}
