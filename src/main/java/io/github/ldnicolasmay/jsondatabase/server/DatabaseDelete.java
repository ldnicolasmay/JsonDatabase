package io.github.ldnicolasmay.jsondatabase.server;

class DatabaseDelete implements Command {

    // Instance Fields

    JsonDatabase jsonDatabase;
    String[] key;

    // Constructors

    public DatabaseDelete(JsonDatabase jsonDatabase, String[] key) {
        this.jsonDatabase = jsonDatabase;
        this.key = key;
    }

    public DatabaseDelete(JsonDatabase jsonDatabase, String key) {
        this.jsonDatabase = jsonDatabase;
        this.key = new String[]{key};
    }


    // Instance Methods

    @Override
    public Response execute() {
        return jsonDatabase.delete(key);
    }
}
