package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class Connection /*extends Endpoint*/ {
    public String authToken;
    public Session session;

    public Connection(String name, Session session) {
        authToken = name;
        this.session = session;
    }

    public void send(String msg) throws IOException { session.getRemote().sendString(msg); }

//    @Override
//    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {}
}
