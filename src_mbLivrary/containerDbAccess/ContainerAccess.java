package containerDbAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;
import java.sql.Date;

import dbAccessObjects.MysqlDbAccess;
import dbDataObjects.Client;
import dbDataObjects.Order;
import dbDataObjects.Part;
import dbDataObjects.PartsType;
import dbDataObjects.Production;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public synchronized int getLastProdOrderId() {
        try {
            ResultSet rs = beanAccess.sendQuery("SELECT MAX(idProductionOrders) FROM productionOrders");
            rs.next();
            
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public synchronized Part getInfoParts(PartsType type) {
        try {
            ResultSet rs = beanAccess.sendQuery("select * from parts where label = '" + type.getType() + "'");
            rs.next();
            
            return new Part(rs.getInt("idParts"), type, rs.getInt("fabricationTime"), rs.getString("dimensions"), rs.getFloat("productionCost"));
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
            
            rs = beanAccess.sendQuery("SELECT label FROM parts WHERE idParts=" + production.getRefPart());
            rs.next();
            String label = rs.getString("label");
            beanAccess.insertRow("INSERT INTO production (idProductions, refOrder, refPart, quantity, defectQuantity) "
                               + "VALUES (" + prodId + ", " + String.valueOf(production.getRefOrder()) + ", "
                               + String.valueOf(production.getRefPart()) + ", " + String.valueOf(production.getQuantity()) 
                               + ", " + String.valueOf(production.getDefectivePartsQuantity()) + ")");
            beanAccess.updateRow("UPDATE parts SET quantity=quantity+" + String.valueOf(production.getQuantity())
                               + " WHERE label='" + label + "'");
            //beanAccess.updateRow("UPDATE productionOrders SET treated = 1 WHERE idProductionOrders= "
            //                   + String.valueOf(production.getRefOrder()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void sendProductionOrder(Order order){
        try {
            int id;
            ResultSet rs = beanAccess.sendQuery("SELECT MAX(idProductionOrders) FROM productionOrders");
            rs.next();
            id = rs.getInt(1) + 1;
            Date nowDate = new Date(order.getDate().getTime());
            beanAccess.insertRow("INSERT INTO productionOrders (idProductionOrders, date, refClient, partType, quantity, treated) "
                               + "VALUES (" + id + ",'" + nowDate.toString() + "'," + order.getRefClient() + ",'"
                               + order.getType() + "', " + order.getQuantity() + ", 0" + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized Order getOrderToTreat() {
        Order orderToTreat = null;
        try {
            ResultSet rs = beanAccess.sendQuery("SELECT * FROM  productionorders"
                                              + " WHERE treated = 0 AND date <= CURDATE()"
                                              + " ORDER BY date ASC");
            if(rs.next()) {
                orderToTreat = new Order(rs.getInt("idProductionOrders")
                                            , new java.util.Date(rs.getDate("date").getTime())
                                            , rs.getInt("refClient")
                                            , PartsType.valueOf(rs.getString("partType"))
                                            , rs.getInt("quantity"), rs.getBoolean("treated"));
                beanAccess.updateRow("UPDATE productionOrders SET treated = 1 WHERE idProductionOrders= "
                               + String.valueOf(rs.getInt("idProductionOrders")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ContainerAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderToTreat;
    }
    
    public synchronized boolean cancelOrder(int orderId) {
        try {
                beanAccess.updateRow("DELETE FROM productionOrders WHERE idProductionOrders= "
                               + String.valueOf(orderId) + " AND treated = 0");
                ResultSet rs = beanAccess.sendQuery("SELECT * FROM  productionorders"
                                              + " WHERE idProductionOrders = "
                                              + String.valueOf(orderId));
                if(rs.next()) {
                    return false;
                } else {
                    return true;
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(ContainerAccess.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public synchronized TreeSet<String> getClientsLogin() {
    	try {
    		TreeSet<String> clientsList = new TreeSet<String>(); 
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
    
    public synchronized Client getClient(String login) {
    	try {
    		ResultSet rs = beanAccess.sendQuery("SELECT * FROM Clients WHERE login='" + login + "'");
                Client client = null;
    		if(rs.next()) {
    			client = new Client(rs.getInt("idClients"), rs.getString("lastName"), rs.getString("firstName"), 
                                            rs.getString("login"), rs.getString("password"), rs.getString("address"), 
                                            rs.getString("phoneNumber"), rs.getString("email"));
    		}
    		return client;
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public synchronized boolean clientAuthorized(String login, String password){
        try {
            ResultSet rs = beanAccess.sendQuery("SELECT login, password "
                                                + "FROM Clients "
                                                + "WHERE login = '" + login
                                                + "' AND password = '" + password
                                                + "'");
            return rs.next();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public synchronized void sendReport(String location, int hours, int minutes, 
            int seconds, int type, String description, boolean resolved) {
        try {
            int id;
            ResultSet rs = beanAccess.sendQuery("SELECT MAX(id) FROM report");
            rs.next();
            id = rs.getInt(1) + 1;
            String bitResolved;
            if (resolved) {
                bitResolved = "1";
            } else {
                bitResolved = "0";
            }
            beanAccess.insertRow("INSERT INTO report (id, location, hours, minutes, seconds, type, description, resolved) "
                               + "VALUES (" + id + ",'" + location + "'," + String.valueOf(hours) + ", " + String.valueOf(minutes)
                               + ", " + String.valueOf(seconds) + ", " + String.valueOf(type) + ", '" + description + "', " 
                               + bitResolved + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void sendEmergency(String location, int hours, int minutes, 
            int seconds, int type, int numberPeople) {
        try {
            int id;
            ResultSet rs = beanAccess.sendQuery("SELECT MAX(id) FROM emergency");
            rs.next();
            id = rs.getInt(1) + 1;
            beanAccess.insertRow("INSERT INTO emergency (id, location, hours, minutes, seconds, type, numberPeople) "
                               + "VALUES (" + id + ",'" + location + "'," + String.valueOf(hours) + ", " + String.valueOf(minutes)
                               + ", " + String.valueOf(seconds) + ", " + String.valueOf(type) + ", " + String.valueOf(numberPeople) + ")");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized String getPassword(String login) {
        try {
    		ResultSet rs = beanAccess.sendQuery("SELECT password FROM Clients WHERE login='" + login + "'");
                String password = "";
    		if(rs.next()) {
    			password = rs.getString("password");
    		}
    		return password;
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    
    
}
