/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino;

import java.util.*;

/**
 * An object that implements deep equality test of objects, including their
 * reference graph topology, that is in addition to establishing by-value
 * equality of objects, it also establishes that their reachable object graphs
 * have identical shape. It is capable of custom-comparing a wide range of
 * various objects, including various Rhino Scriptables, Java arrays, Java
 * Lists, and to some degree Java Maps and Sets (sorted Maps are okay, as well
 * as Sets with elements that can be sorted using their Comparable
 * implementation, and Maps whose keysets work the same). The requirement for
 * sortable maps and sets is to ensure deterministic order of traversal, which
 * is necessary for establishing structural equality of object graphs.
 * <p>
 * An instance of this object is stateful in that it memoizes pairs of objects
 * that already compared equal, so reusing an instance for repeated equality
 * tests of potentially overlapping object graph is beneficial for performance
 * as long as all equality test invocations returns true. Reuse is not advised
 * after an equality test returned false since there is a heuristic in comparing
 * cyclic data structures that can memoize false equalities if two cyclic data
 * structures end up being unequal.
 */
final class EqualObjectGraphs {
	private static final ThreadLocal<EqualObjectGraphs> instance = new ThreadLocal<>();

	static <T> T withThreadLocal(java.util.function.Function<EqualObjectGraphs, T> action) {
		final EqualObjectGraphs currEq = instance.get();
		if (currEq == null) {
			final EqualObjectGraphs eq = new EqualObjectGraphs();
			instance.set(eq);
			try {
				return action.apply(eq);
			} finally {
				instance.set(null);
			}
		}
		return action.apply(currEq);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static Iterator<Map.Entry> sortedEntries(final Map m) {
		// Yes, this throws ClassCastException if the keys aren't comparable. That's okay. We only support maps with
		// deterministic traversal order.
		final Map sortedMap = (m instanceof SortedMap<?, ?> ? m : new TreeMap(m));
		return sortedMap.entrySet().iterator();
	}

	private static Object[] sortedSet(final Set<?> s) {
		final Object[] a = s.toArray();
		Arrays.sort(a); // ClassCastException possible
		return a;
	}

	// Sort IDs deterministically
	private static Object[] getSortedIds(Context cx, final Scriptable s) {
		final Object[] ids = getIds(cx, s);
		Arrays.sort(ids, (a, b) -> {
			if (a instanceof Integer) {
				if (b instanceof Integer) {
					return ((Integer) a).compareTo((Integer) b);
				} else if (b instanceof String || b instanceof Symbol) {
					return -1; // ints before strings or symbols
				}
			} else if (a instanceof String) {
				if (b instanceof String) {
					return ((String) a).compareTo((String) b);
				} else if (b instanceof Integer) {
					return 1; // strings after ints
				} else if (b instanceof Symbol) {
					return -1; // strings before symbols
				}
			} else if (a instanceof Symbol) {
				if (b instanceof Symbol) {
					// As long as people bother to reasonably name their symbols,
					// this will work. If there's clashes in symbol names (e.g.
					// lots of unnamed symbols) it can lead to false inequalities.
					return getSymbolName((Symbol) a).compareTo(getSymbolName((Symbol) b));
				} else if (b instanceof Integer || b instanceof String) {
					return 1; // symbols after ints and strings
				}
			}
			// We can only compare Rhino key types: Integer, String, Symbol
			throw new ClassCastException();
		});
		return ids;
	}

	private static String getSymbolName(final Symbol s) {
		if (s instanceof SymbolKey) {
			return ((SymbolKey) s).getName();
		} else if (s instanceof NativeSymbol) {
			return ((NativeSymbol) s).getKey().getName();
		} else {
			// We can only handle native Rhino Symbol types
			throw new ClassCastException();
		}
	}

	private static Object[] getIds(Context cx, Scriptable s) {
		if (s instanceof ScriptableObject) {
			// Grabs symbols too
			return ((ScriptableObject) s).getIds(cx, true, true);
		} else {
			return s.getAllIds(cx);
		}
	}

	private static Object getValue(final Scriptable s, final Object id, Context cx) {
		if (id instanceof Symbol) {
			return ScriptableObject.getProperty(s, (Symbol) id, cx);
		} else if (id instanceof Integer) {
			return ScriptableObject.getProperty(s, (Integer) id, cx);
		} else if (id instanceof String) {
			return ScriptableObject.getProperty(s, (String) id, cx);
		} else {
			throw new ClassCastException();
		}
	}

	// Object pairs already known to be equal. Used to short-circuit repeated traversals of objects reachable through
	// different paths as well as to detect structural inequality.
	private final Map<Object, Object> knownEquals = new IdentityHashMap<>();
	// Currently compared objects; used to avoid infinite recursion over cyclic object graphs.
	private final Map<Object, Object> currentlyCompared = new IdentityHashMap<>();

	boolean equalGraphs(Context cx, Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		}

		final Object curr2 = currentlyCompared.get(o1);
		if (curr2 == o2) {
			// Provisionally afford that if we're already recursively comparing
			// (o1, o2) that they'll be equal. NOTE: this is the heuristic
			// mentioned in the class JavaDoc that can drive memoizing false
			// equalities if cyclic data structures end up being unequal.
			// While it would be possible to fix that with additional code, the
			// usual usage of equality comparisons is short-circuit-on-false anyway,
			// so this edge case should not arise in normal usage and the additional
			// code complexity to guard against it is not worth it.
			return true;
		} else if (curr2 != null) {
			// If we're already recursively comparing o1 to some other object,
			// this comparison is structurally false.
			return false;
		}

		final Object prev2 = knownEquals.get(o1);
		if (prev2 == o2) {
			// o1 known to be equal to o2.
			return true;
		} else if (prev2 != null) {
			// o1 known to be equal to something other than o2.
			return false;
		}

		final Object prev1 = knownEquals.get(o2);
		assert prev1 != o1; // otherwise we would've already returned at prev2 == o2
		if (prev1 != null) {
			// o2 known to be equal to something other than o1.
			return false;
		}

		currentlyCompared.put(o1, o2);
		final boolean eq = equalGraphsNoMemo(cx, o1, o2);
		if (eq) {
			knownEquals.put(o1, o2);
			knownEquals.put(o2, o1);
		}
		currentlyCompared.remove(o1);
		return eq;
	}

	private boolean equalGraphsNoMemo(Context cx, Object o1, Object o2) {
		if (o1 instanceof Wrapper) {
			return o2 instanceof Wrapper && equalGraphs(cx, ((Wrapper) o1).unwrap(), ((Wrapper) o2).unwrap());
		} else if (o1 instanceof Scriptable) {
			return o2 instanceof Scriptable && equalScriptables(cx, (Scriptable) o1, (Scriptable) o2);
		} else if (o1 instanceof ConsString) {
			return ((ConsString) o1).toString().equals(o2);
		} else if (o2 instanceof ConsString) {
			return o1.equals(((ConsString) o2).toString());
		} else if (o1 instanceof SymbolKey) {
			return o2 instanceof SymbolKey && equalGraphs(cx, ((SymbolKey) o1).getName(), ((SymbolKey) o2).getName());
		} else if (o1 instanceof Object[]) {
			return o2 instanceof Object[] && equalObjectArrays(cx, (Object[]) o1, (Object[]) o2);
		} else if (o1.getClass().isArray()) {
			return Objects.deepEquals(o1, o2);
		} else if (o1 instanceof List<?>) {
			return o2 instanceof List<?> && equalLists(cx, (List<?>) o1, (List<?>) o2);
		} else if (o1 instanceof Map<?, ?>) {
			return o2 instanceof Map<?, ?> && equalMaps(cx, (Map<?, ?>) o1, (Map<?, ?>) o2);
		} else if (o1 instanceof Set<?>) {
			return o2 instanceof Set<?> && equalSets(cx, (Set<?>) o1, (Set<?>) o2);
		} else if (o1 instanceof NativeGlobal) {
			return o2 instanceof NativeGlobal; // stateless objects
		} else if (o1 instanceof JavaAdapter) {
			return o2 instanceof JavaAdapter; // stateless objects
		}

		// Fallback case for everything else.
		return o1.equals(o2);
	}

	private boolean equalScriptables(Context cx, final Scriptable s1, final Scriptable s2) {
		final Object[] ids1 = getSortedIds(cx, s1);
		final Object[] ids2 = getSortedIds(cx, s2);
		if (!equalObjectArrays(cx, ids1, ids2)) {
			return false;
		}
		final int l = ids1.length;
		for (int i = 0; i < l; ++i) {
			if (!equalGraphs(cx, getValue(s1, ids1[i], cx), getValue(s2, ids2[i], cx))) {
				return false;
			}
		}
		if (!equalGraphs(cx, s1.getPrototype(cx), s2.getPrototype(cx))) {
			return false;
		} else if (!equalGraphs(cx, s1.getParentScope(), s2.getParentScope())) {
			return false;
		}

		// Handle special Scriptable implementations
		if (s1 instanceof IdFunctionObject s3) {
			return s2 instanceof IdFunctionObject s4 && IdFunctionObject.equalObjectGraphs(cx, s3, s4, this);
		} else if (s1 instanceof ArrowFunction s3) {
			return s2 instanceof ArrowFunction s4 && ArrowFunction.equalObjectGraphs(cx, s3, s4, this);
		} else if (s1 instanceof BoundFunction s3) {
			return s2 instanceof BoundFunction s4 && BoundFunction.equalObjectGraphs(cx, s3, s4, this);
		} else if (s1 instanceof NativeSymbol s3) {
			return s2 instanceof NativeSymbol s4 && equalGraphs(cx, s3.getKey(), s4.getKey());
		}
		return true;
	}

	private boolean equalObjectArrays(Context cx, final Object[] a1, final Object[] a2) {
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; ++i) {
			if (!equalGraphs(cx, a1[i], a2[i])) {
				return false;
			}
		}
		return true;
	}

	private boolean equalLists(Context cx, final List<?> l1, final List<?> l2) {
		if (l1.size() != l2.size()) {
			return false;
		}
		final Iterator<?> i1 = l1.iterator();
		final Iterator<?> i2 = l2.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			if (!equalGraphs(cx, i1.next(), i2.next())) {
				return false;
			}
		}
		assert !(i1.hasNext() || i2.hasNext());
		return true;
	}

	@SuppressWarnings("rawtypes")
	private boolean equalMaps(Context cx, final Map<?, ?> m1, final Map<?, ?> m2) {
		if (m1.size() != m2.size()) {
			return false;
		}
		final Iterator<Map.Entry> i1 = sortedEntries(m1);
		final Iterator<Map.Entry> i2 = sortedEntries(m2);

		while (i1.hasNext() && i2.hasNext()) {
			final Map.Entry kv1 = i1.next();
			final Map.Entry kv2 = i2.next();
			if (!(equalGraphs(cx, kv1.getKey(), kv2.getKey()) && equalGraphs(cx, kv1.getValue(), kv2.getValue()))) {
				return false;
			}
		}
		assert !(i1.hasNext() || i2.hasNext());
		// TODO: assert linked maps traversal order?
		return true;

	}

	private boolean equalSets(Context cx, final Set<?> s1, final Set<?> s2) {
		return equalObjectArrays(cx, sortedSet(s1), sortedSet(s2));
	}
}
