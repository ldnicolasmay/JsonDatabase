package io.github.ldnicolasmay.jsondatabase.server;

public class JsonDatabaseController {

    // Instance Fields

    Command command;


    // Constructor

    void setCommand(Command command) {
        this.command = command;
    }

    // Instance Methods

    Response executeCommand() {
        return command.execute();
    }

}
