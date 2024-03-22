package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;

    public Connection(String name, Session session) {
        userName = name;
        this.session = session;
    }

    public void send(String msg) throws IOException { session.getRemote().sendString(msg); }
}
