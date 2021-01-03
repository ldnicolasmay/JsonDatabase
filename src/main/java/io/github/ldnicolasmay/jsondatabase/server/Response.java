package io.github.ldnicolasmay.jsondatabase.server;

import com.google.gson.JsonElement;

public class Response {

    // Instance Fields

    private final String response;
    private final JsonElement value;
    private final String reason;


    // Constructor

    public Response(String response, JsonElement value, String reason) {
        this.response = response;
        this.value = value;
        this.reason = reason;
    }


    // Instance Methods

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        // response
        stringBuilder.append(String.format("\"response\":\"%s\"", response));
        // value
        if (value != null) {
            stringBuilder.append(String.format(",\"value\":%s", value));
        }
        // reason
        if (reason != null) {
            stringBuilder.append(String.format(",\"reason\":\"%s\"", reason));
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
