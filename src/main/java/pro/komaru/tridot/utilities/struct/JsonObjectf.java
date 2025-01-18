package pro.komaru.tridot.utilities.struct;

import com.google.gson.*;

public class JsonObjectf {
    private final JsonObject parent;

    public JsonObjectf(JsonObject parent) {
        this.parent = parent;
    }

    public boolean isObj(String key) {
        return parent.get(key) instanceof JsonObject;
    }
    public boolean isStr(String key) {
        return parent.get(key) instanceof JsonPrimitive p && p.isString();
    }
    public boolean isNum(String key) {
        return parent.get(key) instanceof JsonPrimitive p && p.isNumber();
    }
    public JsonObject o(String key) {
        return parent.get(key).getAsJsonObject();
    }
    public JsonObjectf of(String key) {
        return new JsonObjectf(o(key));
    }
    public double d(String key) {
        return parent.get(key).getAsDouble();
    }
    public float f(String key) {
        return parent.get(key).getAsFloat();
    }
    public int i(String key) {
        return parent.get(key).getAsInt();
    }
    public String str(String key) {
        return parent.get(key).getAsString();
    }
}
