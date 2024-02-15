package DataAccess;

public interface AuthDAOInterface {

    AuthData createAuthToken(String username);
    AuthData getAuth(String token);
    void deleteAuth(String token);
    void clearAuth();

}
