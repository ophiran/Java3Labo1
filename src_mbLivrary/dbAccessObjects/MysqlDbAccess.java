package dbAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.beans.*;
import java.io.Serializable;


public class MysqlDbAccess implements DbAccess, Serializable{

    private Connection connection;
    
    public MysqlDbAccess() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }
    
    public synchronized void startConnection(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql:" + dbPath,"","");
    }
    
    public synchronized void startConnection(String dbPath, String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql:" + dbPath,user,password);
    }
    
    public synchronized void stopConnection() throws SQLException {
        if(connection != null)
            connection.close();
    }
    
    public synchronized ResultSet sendQuery(String query) throws SQLException {
        Statement instruction = connection.createStatement();
        return instruction.executeQuery(query);
    }
    
    public synchronized void insertRow(String query) throws SQLException {
        Statement instruction = connection.createStatement();
        instruction.executeUpdate(query);
    }
    
    public synchronized void updateRow(String query) throws SQLException {
        Statement instruction = connection.createStatement();
        instruction.executeUpdate(query);
    }
    
    public synchronized void commit() throws SQLException {
        connection.commit();
    }
        
    @Override
    protected void finalize() throws Throwable {
        if(connection != null) connection.close();
        super.finalize();
    }
    
}
