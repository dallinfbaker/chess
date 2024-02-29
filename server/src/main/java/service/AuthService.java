package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import server.WebSocket.ResponseException;

public class AuthService {
    private final AuthDAOInterface authDAO;
    public AuthService(AuthDAOInterface authDAO) { this.authDAO = authDAO; }
    public void checkAuth(String auth) throws ResponseException {
        try { if (!authDAO.validAuth(auth)) throw new ResponseException(401, "Error: unauthorized"); }
        catch (DataAccessException e) { throw new ResponseException(401, "Error: unauthorized"); }
    }

    public String getUsername(String auth) throws ResponseException {
        try { return authDAO.getAuth(auth).username(); }
        catch (DataAccessException e) { throw new ResponseException(401, "Error: unauthorized"); }

    }
}
