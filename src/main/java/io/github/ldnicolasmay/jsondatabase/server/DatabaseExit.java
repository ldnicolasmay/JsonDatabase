package io.github.ldnicolasmay.jsondatabase.server;

class DatabaseExit implements Command {

    // Instance Fields

    JsonDatabase jsonDatabase;


    // Constructor

    public DatabaseExit(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }


    // Instance Methods
    @Override
    public Response execute() {
        return jsonDatabase.exit();
    }
}
