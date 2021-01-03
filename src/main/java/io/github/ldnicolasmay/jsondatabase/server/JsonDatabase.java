package io.github.ldnicolasmay.jsondatabase.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class JsonDatabase {

    // Static Fields

    private static final String OK_RESPONSE = "OK";
    private static final String ERROR_RESPONSE = "ERROR";
    private static final String MISSING_KEY_REASON = "No such key";


    // Static Methods

    private static JsonObject readJsonFromFile(Path jsonFilePath) {

        JsonObject jsonTree = null;

        try (Reader reader = new FileReader(jsonFilePath.toFile(), StandardCharsets.UTF_8)) {

            JsonElement jsonElement = JsonParser.parseReader(reader);
            jsonTree = jsonElement.getAsJsonObject();

        } catch (IOException e) {
            System.err.printf("Could not read file %s\n", jsonFilePath.toString());
            e.printStackTrace();
        }

        return jsonTree;
    }

    private static void writeJsonToFile(JsonObject jsonObject, Path jsonFilePath) {

        try (Writer writer = new FileWriter(jsonFilePath.toFile(), StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            gson.toJson(jsonObject, writer);

        } catch (IOException e) {
            System.err.printf("Could not write %s to file %s\n", jsonObject.toString(), jsonFilePath.toString());
            e.printStackTrace();
        }
    }

    private static JsonElement getFromJsonObject(JsonObject jsonObject,
                                                 String[] keys,
                                                 int keyDepth) {
        JsonElement value = null;

        if (keyDepth < keys.length) {

            String currentKey = keys[keyDepth];

            if (keyDepth == keys.length - 1 && jsonObject.has(currentKey)) {
                value = jsonObject.get(currentKey);
            } else if (jsonObject.has(currentKey)) {
                JsonObject existingBranch = jsonObject.getAsJsonObject(currentKey);
                value = getFromJsonObject(existingBranch, keys, keyDepth + 1);
            }
        }

        return value;
    }

    private static JsonObject putInJsonObject(JsonObject jsonObject,
                                                String[] keys,
                                                int keyDepth,
                                                JsonElement value) {
        String currentKey = keys[keyDepth];

        if (keyDepth == keys.length - 1) {
            jsonObject.add(currentKey, value);
        } else if (jsonObject.has(currentKey) && jsonObject.get(currentKey).isJsonObject()) {
            JsonObject existingBranch = jsonObject.getAsJsonObject(currentKey);
            jsonObject.add(currentKey, putInJsonObject(existingBranch, keys, keyDepth + 1, value));
        } else {
            JsonObject newBranch = new JsonObject();
            jsonObject.add(currentKey, putInJsonObject(newBranch, keys, keyDepth + 1, value));
        }

        return jsonObject;
    }

    private static JsonElement removeFromJsonObject(JsonObject jsonObject,
                                                    String[] keys,
                                                    int keyDepth) {
        JsonElement value = null;

        if (keyDepth < keys.length) {

            String currentKey = keys[keyDepth];

            if (keyDepth == keys.length - 1 && jsonObject.has(currentKey)) {
                value = jsonObject.remove(currentKey);
            } else if (jsonObject.has(currentKey) && jsonObject.get(currentKey).isJsonObject()) {
                JsonObject existingBranch = jsonObject.getAsJsonObject(currentKey);
                value = removeFromJsonObject(existingBranch, keys, keyDepth + 1);
            }
        }

        return value;
    }


    // Instance Fields

    private final Path jsonFilePath;


    // Constructor

    public JsonDatabase(Path jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }


    // Instance Methods

    public synchronized Response set(String[] key, JsonElement value) {

        JsonObject jsonTree = readJsonFromFile(jsonFilePath);

        putInJsonObject(jsonTree, key, 0, value);
        writeJsonToFile(jsonTree, jsonFilePath);

        return new Response(OK_RESPONSE, null, null);
    }

    public synchronized Response get(String[] key) {

        Response response;

        JsonObject jsonTree = readJsonFromFile(jsonFilePath);
        JsonElement jsonValue = getFromJsonObject(jsonTree, key, 0);

        if (jsonValue != null) {
            response = new Response(OK_RESPONSE, jsonValue, null);
        } else {
            response = new Response(ERROR_RESPONSE, null, MISSING_KEY_REASON);
        }

        return response;
    }

    public synchronized Response delete(String[] key) {

        Response response;

        JsonObject jsonTree = readJsonFromFile(jsonFilePath);
        JsonElement value = removeFromJsonObject(jsonTree, key, 0);

        if (value != null) {
            response = new Response(OK_RESPONSE, null, null);
            writeJsonToFile(jsonTree, jsonFilePath);
        } else {
            response = new Response(ERROR_RESPONSE, null, MISSING_KEY_REASON);
        }

        return response;
    }

    public synchronized Response exit() {
        return new Response(OK_RESPONSE, null, null);
    }

}
