package io.github.ldnicolasmay.jsondatabase.server;

class DatabaseGet implements Command {

    // Instance Fields

    JsonDatabase jsonDatabase;
    String[] key;


    // Constructors

    public DatabaseGet(JsonDatabase jsonDatabase, String[] key) {
        this.jsonDatabase = jsonDatabase;
        this.key = key;
    }

    public DatabaseGet(JsonDatabase jsonDatabase, String key) {
        this.jsonDatabase = jsonDatabase;
        this.key = new String[]{key};
    }


    // Instance Methods

    @Override
    public Response execute() {
        return jsonDatabase.get(key);
    }
}
