package containerDbAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import dbAccessObjects.MysqlDbAccess;
import dbDataObjects.Client;
import dbDataObjects.Part;
import dbDataObjects.PartsType;
import dbDataObjects.Production;

public class ContainerAccess{
    
    private MysqlDbAccess beanAccess;
    private static ContainerAccess containerReference;
    

    private ContainerAccess() {
        try {
            beanAccess = new MysqlDbAccess();
            beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized static ContainerAccess getInstance() {
    	if (containerReference == null) {
    		containerReference = new ContainerAccess();
    	}
    	
    	return containerReference;
    }
    
    public synchronized Part getInfoParts(PartsType type) {
        try {
            ResultSet rs = beanAccess.sendQuery("select * from parts where label = '" + type.getType() + "'");
            rs.next();
            
            return new Part(type, rs.getInt("fabricationTime"), rs.getString("dimensions"), rs.getFloat("productionCost"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized void sendProductionInfo(Production production){
        try {
            int prodId;
            ResultSet rs = beanAccess.sendQuery("SELECT MAX(idProductions) FROM production");
            rs.next();
            prodId = rs.getInt(1) + 1;
            beanAccess.insertRow("INSERT INTO production VALUES (" + prodId + ",'" +
            production.getIdParts() + "'," + production.getQuantity() + "," + production.getDefectivePartsQuantity() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized Set<String> getClients() {
    	try {
    		Set<String> clientsList = new HashSet<String>(); 
    		ResultSet rs = beanAccess.sendQuery("SELECT login FROM Clients");
    		while(rs.next()) {
    			clientsList.add(rs.getString("login"));
    		}
    		return clientsList;
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    
    
}
