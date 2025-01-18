package github.iri.tridot.rhino.util;

import github.iri.tridot.rhino.Context;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public interface DataObject {
	<T> T createDataObject(Supplier<T> instanceFactory, Context cx);

	<T> List<T> createDataObjectList(Supplier<T> instanceFactory, Context cx);

	boolean isDataObjectList();
}
