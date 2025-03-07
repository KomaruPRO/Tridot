package pro.komaru.tridot.rhino.util;

import java.util.*;
import java.util.function.*;

/**
 * @author LatvianModder
 */
public class DynamicMap<V> extends HashMap<String, V> {
	private final Function<String, ? extends V> objectFactory;

	public DynamicMap(Function<String, ? extends V> f) {
		objectFactory = f;
	}

	@Override
	public V get(Object key) {
		String k = key.toString();
		V v = super.get(k);

		if (v == null) {
			v = objectFactory.apply(k);
			put(k, v);
		}

		return v;
	}

	@Override
	public boolean containsKey(Object name) {
		return true;
	}
}
