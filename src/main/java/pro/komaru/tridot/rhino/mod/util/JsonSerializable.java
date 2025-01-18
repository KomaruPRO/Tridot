package pro.komaru.tridot.rhino.mod.util;

import com.google.gson.JsonElement;
import pro.komaru.tridot.rhino.util.RemapForJS;

/**
 * @author LatvianModder
 */
public interface JsonSerializable {
	@RemapForJS("toJson")
	JsonElement toJsonJS();
}