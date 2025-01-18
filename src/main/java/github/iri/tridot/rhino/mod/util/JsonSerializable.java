package github.iri.tridot.rhino.mod.util;

import com.google.gson.JsonElement;
import github.iri.tridot.rhino.util.RemapForJS;

/**
 * @author LatvianModder
 */
public interface JsonSerializable {
	@RemapForJS("toJson")
	JsonElement toJsonJS();
}