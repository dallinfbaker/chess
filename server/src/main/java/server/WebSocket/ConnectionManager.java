package server.WebSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.*;

public class ConnectionManager {
    public HashMap<Integer, HashMap<String, Connection>> connections = new HashMap<>();

    public void add(int id, String auth, Session session) {
        var connection = new Connection(auth, session);
        if (!connections.containsKey(id)) { connections.put(id, new HashMap<>()); }
        connections.get(id).put(auth, connection);
    }

    public void remove(int id, String auth) { connections.get(id).remove(auth); }

    public void broadcast(int id, String ignoredAuth, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Connection con : connections.get(id).values()) {
            if (Objects.isNull(con) || Objects.equals(ignoredAuth, con.authToken)) continue;
            if (con.session.isOpen()) {
                con.send( new Gson().toJson(message)); }
            else { removeList.add(con); }
        }
        for (var c : removeList) { connections.get(id).remove(c.authToken); }
    }
}