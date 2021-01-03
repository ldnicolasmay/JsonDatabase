package io.github.ldnicolasmay.jsondatabase.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    // Static Fields

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String SERVER_START_MSG = "Server started!";
    private static final int POOL_SIZE = 4;
    // private static final Path QUERY_PATH = Path.of("src/server/data");
    private static final Path QUERY_PATH = Path.of("src/main/resources/server/data");
    private static final Path JSON_DB_PATH = QUERY_PATH.resolve("db.json");


    // main

    public static void main(String[] args) {

        // Create executor service to queue app requests in
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        JsonDatabase jsonDatabase = new JsonDatabase(JSON_DB_PATH);

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {

            System.out.printf("%s\n", SERVER_START_MSG);

            boolean exited = false;
            String requestJsonString;
            Request request;
            String commandType;

            while (!exited) {

                // Wait for client socket connections
                Socket socket = serverSocket.accept();

                // Once connected, receive JSON request string from client
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                requestJsonString = dataInputStream.readUTF();
                // ********************************************************************
                System.out.println("requestJsonString=" + requestJsonString);

                // Parse JSON request string; Create Request object
                JsonObject requestJsonObject = JsonParser.parseString(requestJsonString)
                        .getAsJsonObject();
                JsonPrimitive requestType = requestJsonObject.getAsJsonPrimitive("type");
                JsonElement requestKey = requestJsonObject.get("key");
                JsonElement requestValue = requestJsonObject.get("value");
                request = new Request(requestType, requestKey, requestValue);

                // Exit if request type from client is "exit"
                commandType = request.getTypeAsString();
                exited = "exit".equals(commandType);

                // Queue request in executor service to be handled concurrently
                Session session = new Session(socket, jsonDatabase, request);
                executorService.submit(session);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stop executor service
        executorService.shutdown();
    }
}
