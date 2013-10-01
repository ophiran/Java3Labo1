package containerBddAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.RuleBasedCollator;

import bddAccessObjects.BeanBDAccessMysql;
import bddDataObjects.Part;
import bddDataObjects.PartsType;
import bddDataObjects.Production;

public class ContainerAccess{
    
    private BeanBDAccessMysql beanAccess;
    
    public ContainerAccess() {
        try {
            beanAccess = new BeanBDAccessMysql();
            beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "root");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Part getInfoParts(PartsType type) {
        try {
            ResultSet rs = beanAccess.sendQuery("select * from parts where label = '" + type.getType() + "'");
            rs.next();
            
            return new Part(type, rs.getInt("fabricationTime"), rs.getString("dimensions"), rs.getFloat("productionCost"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void sendProductionInfo(Production production){
        try {
            ResultSet rs = beanAccess.sendQuery("");
        } catch (SQLException e) {
            
        }
    }
    
    
}
