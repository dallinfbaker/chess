package DataAccess;

public class DAOManager {
    public AuthDAOInterface authDAO = new AuthDAOMemory();
    public UserDAOInterface userDAO = new UserDAOMemory();
    public GameDAOInterface gameDAO = new GameDAOMemory();

    public DAOManager(boolean database) {
        if (database) {
            authDAO = new AuthDAODB();
            userDAO = new UserDAODB();
            gameDAO = new GameDAODB();
        }
        else {
            authDAO = new AuthDAOMemory();
            userDAO = new UserDAOMemory();
            gameDAO = new GameDAOMemory();
        }
    }
}
