package service;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
}
