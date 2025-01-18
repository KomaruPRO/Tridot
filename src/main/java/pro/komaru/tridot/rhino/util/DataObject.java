package pro.komaru.tridot.rhino.util;

import pro.komaru.tridot.rhino.*;

import java.util.*;
import java.util.function.*;

/**
 * @author LatvianModder
 */
public interface DataObject {
	<T> T createDataObject(Supplier<T> instanceFactory, Context cx);

	<T> List<T> createDataObjectList(Supplier<T> instanceFactory, Context cx);

	boolean isDataObjectList();
}
