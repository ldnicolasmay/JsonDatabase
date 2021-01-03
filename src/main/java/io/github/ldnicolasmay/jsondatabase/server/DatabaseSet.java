package io.github.ldnicolasmay.jsondatabase.server;

import com.google.gson.JsonElement;

class DatabaseSet implements Command {

    // Instance Fields

    JsonDatabase jsonDatabase;
    String[] key;
    JsonElement value;


    // Constructors

    public DatabaseSet(JsonDatabase jsonDatabase, String[] key, JsonElement value) {
        this.jsonDatabase = jsonDatabase;
        this.key = key;
        this.value = value;
    }

    public DatabaseSet(JsonDatabase jsonDatabase, String key, JsonElement value) {
        this.jsonDatabase = jsonDatabase;
        this.key = new String[]{key};
        this.value = value;
    }


    // Instance Methods

    @Override
    public Response execute() {
        return jsonDatabase.set(key, value);
    }
}
