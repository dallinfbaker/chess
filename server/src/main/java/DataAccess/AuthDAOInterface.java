package DataAccess;

public interface AuthDAOInterface {

    String createAuthToken(String username);
    AuthData getAuth(String token);
    void deleteAuth(String token);
    void clearAuth();

}
