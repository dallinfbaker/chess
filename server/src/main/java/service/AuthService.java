package service;

import DataAccess.AuthDAO;
import server.WebSocket.ResponseException;

public class AuthService {
    private final AuthDAO authDAO;
    public AuthService(AuthDAO authDAO) { this.authDAO = authDAO; }
    public void checkAuth(String auth) throws ResponseException { if (!authDAO.validAuth(auth)) throw new ResponseException(401, "Error: unauthorized"); }
    public String getUsername(String auth) throws ResponseException { return authDAO.getAuth(auth).getUsername(); }
}
