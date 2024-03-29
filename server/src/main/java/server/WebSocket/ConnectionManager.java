package server.WebSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.*;

public class ConnectionManager {
    public HashMap<Integer, HashMap<String, Session>> connections = new HashMap<>();

    public void add(int id, String auth, Session session) {
        if (!connections.containsKey(id)) { connections.put(id, new HashMap<>()); }
        connections.get(id).put(auth, session);
    }
    public void remove(int id, String auth) { connections.get(id).remove(auth); }

    public void broadcast(int id, String ignoredAuth, ServerMessage msg) throws IOException {
        HashMap<String, Session> sessions = connections.get(id);
        Session session;
//        ServerMessage msg;
        for (String auth : sessions.keySet()) {
            if (Objects.equals(ignoredAuth, auth)) continue;
            session = sessions.get(auth);
            if (Objects.isNull(session)) sessions.remove(auth);
            else if (session.isOpen()) {
//                msg = switch (type) {
//                    case NOTIFICATION -> new NotificationMessage(auth, message);
//                    case LOAD_GAME -> new LoadGameMessage();
//                    case ERROR -> new ErrorMessage(auth, message);
//                }
                msg.setAuth(auth);
                send(session, msg);
            }
            else { sessions.remove(auth); }
        }
    }
    public void sendError(Session session, ErrorMessage msg) { if (session.isOpen()) send(session, msg); }
    public void send(Session session, ServerMessage msg) {
        try { session.getRemote().sendString(new Gson().toJson(msg)); }
        catch (IOException e) { throw new RuntimeException(e); }

//        session.getRemote().send(new Gson().toJson(msg));
    }
}