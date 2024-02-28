package DataAccess;

public class DAOManager {
    public AuthDAOMemory authDAO = new AuthDAOMemory();
    public UserDAOMemory userDAO = new UserDAOMemory();
    public GameDAOMemory gameDAO = new GameDAOMemory();
}
