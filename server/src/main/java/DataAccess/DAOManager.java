package DataAccess;

public class DAOManager {
    public AuthDAOInterface authDAO;
    public UserDAOInterface userDAO;
    public GameDAOInterface gameDAO;

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
