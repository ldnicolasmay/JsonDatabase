package io.github.ldnicolasmay.jsondatabase.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientApp {

    // Command Line Parameters

    @Parameter(names = {"--type", "t"}, description = "type of request: set, get, delete, exit")
    String type;
    @Parameter(names = {"--key", "-k"}, description = "JSON key for set, get, and delete requests")
    String key;
    @Parameter(names = {"--value", "-v"}, description = "JSON value for set requests")
    String value;
    @Parameter(names = {"--in", "-i"}, description = "JSON request file")
    String jsonRequestFilePathString;


    // Static Fields

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    private static final String CLIENT_START_MSG = "Client started!";
    // private static final Path QUERY_PATH = Path.of("src/client/data");
    private static final Path QUERY_PATH = Path.of("src/main/resources/client/data");


    // Static Methods

    private static String readJsonFileAsString(String jsonFilePathString) {

        String jsonString = "";

        try {
            Path jsonFilePath = QUERY_PATH.resolve(jsonFilePathString);
            jsonString = Files.readString(jsonFilePath);
        } catch (IOException e) {
            System.err.printf("Could not find: %s\n", jsonFilePathString);
            e.printStackTrace();
        }

        return jsonString;
    }


    // main

    public static void main(String... argv) {

        ClientApp clientApp = new ClientApp();

        JCommander.newBuilder()
                .addObject(clientApp)
                .build()
                .parse(argv);

        clientApp.run();
    }


    // Instance Method

    private void run() {

        // Retrieve or build request JSON string
        StringBuilder stringBuilder = new StringBuilder();
        String requestJsonString;

        if (jsonRequestFilePathString != null && !"".equals(jsonRequestFilePathString)) {
            // Retrieve JSON string from file
            requestJsonString = readJsonFileAsString(jsonRequestFilePathString);
        } else {
            // Build JSON string from command line arguments
            stringBuilder.append("{");
            // Request type
            stringBuilder.append(String.format("\"type\":\"%s\"", type));
            // Request key
            if ("set".equals(type) || "get".equals(type) || "delete".equals(type)) {
                stringBuilder.append(String.format(",\"key\":\"%s\"", key));
            }
            // Request value
            if ("set".equals(type)) {
                stringBuilder.append(String.format(",\"value\":\"%s\"", value));
            }
            stringBuilder.append("}");
            requestJsonString = stringBuilder.toString();
        }

        // Connect to server; Send request; Receive response
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {

            System.out.printf("%s\n", CLIENT_START_MSG);
            System.out.printf("Sent: %s\n", requestJsonString);

            dataOutputStream.writeUTF(requestJsonString);

            String responseJSON = dataInputStream.readUTF();
            System.out.printf("Received: %s\n", responseJSON);

        } catch (IOException e) {
            System.err.printf("Could not connect to %s at port %d\n", SERVER_ADDRESS, SERVER_PORT);
            e.printStackTrace();
        }
    }
}
