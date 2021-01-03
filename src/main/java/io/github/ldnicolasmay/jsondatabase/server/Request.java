package io.github.ldnicolasmay.jsondatabase.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class Request {

    // Instance Fields

    private final JsonPrimitive type;
    private final JsonElement key; // either JsonPrimitive "a" or JsonArray ["a", "b"]
    private final JsonElement value; // either JsonPrimitive "a" or JsonObject {"a":{"b":"foo"}}


    // Constructor

    public Request(JsonPrimitive type, JsonElement key, JsonElement value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }


    // Instance Methods

    public JsonPrimitive getType() {
        return type;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    public String getTypeAsString() {
        return type.getAsString();
    }

    public String getKeyAsString() {

        if (key.isJsonPrimitive()) {
            return key.getAsString();
        }

        return null;
    }

    public String[] getKeyAsArray() {
        if (key.isJsonArray()) {
            JsonArray jsonArray = key.getAsJsonArray();
            int arraySize = jsonArray.size();
            String[] stringArray = new String[arraySize];

            for (int i = 0; i < arraySize; i++) {
                stringArray[i] = jsonArray.get(i).getAsString();
            }

            return stringArray;
        }

        return null;
    }

    public String getValueAsString() {
        if (value.isJsonPrimitive()) {
            return key.getAsString();
        }

        return null;
    }
}
