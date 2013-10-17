package dbAccessObjects;



import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbAccess{
    
    public void startConnection(String dbPath) throws SQLException;
    
    public void stopConnection() throws SQLException;
    
    public ResultSet sendQuery(String query) throws SQLException;
}
