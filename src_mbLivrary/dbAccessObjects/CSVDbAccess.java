package dbAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVDbAccess implements DbAccess{

private Connection connection;
    
    public CSVDbAccess() throws ClassNotFoundException {
        Class.forName("org.relique.jdbc.csv.CsvDriver");
    }
    
    public synchronized void startConnection(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:relique:csv:" + dbPath);
    }
    
    public synchronized void stopConnection() throws SQLException {
        if(connection != null)
            connection.close();
    }
    
    public synchronized ResultSet sendQuery(String query) throws SQLException {
        Statement instruction = connection.createStatement();
        return instruction.executeQuery(query);
    }
    
}
