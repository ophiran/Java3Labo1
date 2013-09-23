package bddAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BeanBDAccessMysql {

    private Connection connection;
    
    public BeanBDAccessMysql() {
        
    }
    
    public void startConnection(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mm.mysql:" + dbPath,"","");
    }
    
    public void stopConnection() throws SQLException {
        if(connection != null)
            connection.close();
    }
    
    public ResultSet sendQuery(String query) throws SQLException {
        Statement instruction = connection.createStatement();
        return instruction.executeQuery(query);
    }
    
    
    
}
