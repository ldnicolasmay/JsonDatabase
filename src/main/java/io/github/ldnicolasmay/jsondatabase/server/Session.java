package io.github.ldnicolasmay.jsondatabase.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {

    // Instance Fields

    private final Socket socket;
    private final JsonDatabase jsonDatabase;
    private final Request request;


    // Constructor

    public Session(Socket socket, JsonDatabase jsonDatabase, Request request) {
        this.socket = socket;
        this.jsonDatabase = jsonDatabase;
        this.request = request;
    }


    // Runnable interface run() override

    @Override
    public void run() {

        JsonDatabaseController jsonDatabaseController = new JsonDatabaseController();

        Response response;
        String commandType;

        try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {

            commandType = request.getTypeAsString();

            switch (commandType) {

                case "set":
                    Command dbSet = null;
                    if (request.getKey().isJsonPrimitive()) {
                        dbSet = new DatabaseSet(jsonDatabase, request.getKeyAsString(), request.getValue());
                    } else if (request.getKey().isJsonArray()) {
                        dbSet = new DatabaseSet(jsonDatabase, request.getKeyAsArray(), request.getValue());
                    }
                    jsonDatabaseController.setCommand(dbSet);
                    break;

                case "get":
                    Command dbGet = null;
                    if (request.getKey().isJsonPrimitive()) {
                        dbGet = new DatabaseGet(jsonDatabase, request.getKeyAsString());
                    } else if (request.getKey().isJsonArray()) {
                        dbGet = new DatabaseGet(jsonDatabase, request.getKeyAsArray());
                    }
                    jsonDatabaseController.setCommand(dbGet);
                    break;

                case "delete":
                    Command dbDelete = null;
                    if (request.getKey().isJsonPrimitive()) {
                        dbDelete = new DatabaseDelete(jsonDatabase, request.getKeyAsString());
                    } else if (request.getKey().isJsonArray()) {
                        dbDelete = new DatabaseDelete(jsonDatabase, request.getKeyAsArray());
                    }
                    jsonDatabaseController.setCommand(dbDelete);
                    break;

                case "exit":
                    Command dbExit = new DatabaseExit(jsonDatabase);
                    jsonDatabaseController.setCommand(dbExit);
                    break;

                default:
                    break;
            }

            response = jsonDatabaseController.executeCommand();
            dataOutputStream.writeUTF(response.toString());
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
