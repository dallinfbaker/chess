package server.WebSocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ResponseException extends Exception {
    final private int statusCode;


    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
//    public String detailMessage() {
//        return this.getMessage();
//    }
//
//    public JsonElement serialize() {
//        return new JsonPrimitive("\"message\": " + super.getMessage());
//    }
}
