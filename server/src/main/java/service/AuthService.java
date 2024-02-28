package service;

import DataAccess.AuthDAOMemory;
import server.WebSocket.ResponseException;

public class AuthService {
    private final AuthDAOMemory authDAO;
    public AuthService(AuthDAOMemory authDAO) { this.authDAO = authDAO; }
    public void checkAuth(String auth) throws ResponseException { if (!authDAO.validAuth(auth)) throw new ResponseException(401, "Error: unauthorized"); }
    public String getUsername(String auth) throws ResponseException { return authDAO.getAuth(auth).username(); }
}
