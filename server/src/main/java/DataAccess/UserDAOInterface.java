package DataAccess;

public interface UserDAOInterface {
    UserData getUser(String username);
    void createUser(String username, String password, String email);
    void updateUser(String username, String password, String email);
    void deleteUser(String username);
    void clearUsers();



    String getPassword(String username);
    String getEmail(String username);

}
