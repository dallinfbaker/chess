package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String name, Session session) {
        var connection = new Connection(name, session);
        connections.put(name, connection);
    }

    public void remove(String name) { connections.remove(name); }

    public void broadcast(String excludeUser, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.userName.equals(excludeUser)) { c.send(message.toString()); }
            } else { removeList.add(c); }
        }
        for (var c : removeList) { connections.remove(c.userName); }
    }
}