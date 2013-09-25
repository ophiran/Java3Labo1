import java.sql.SQLException;

import bddAccessObjects.BeanBDAccessCSV;
import bddAccessObjects.BeanBDAccessMysql;
import java.sql.ResultSet;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       
       System.out.println("Trying to connect...");
        try {
            /*
            BeanBDAccessMysql bean = new BeanBDAccessMysql();
            bean.startConnection("//127.0.0.1:3306/mydb", "root", "");
            System.out.println("Connection established");
            ResultSet rs = bean.sendQuery("select * from parts");
            while(rs.next()){
                System.out.println(((Integer)rs.getInt("FabricationTime")).toString());
            }
            bean.stopConnection();
            System.out.println("Deconnected");
            */
            BeanBDAccessCSV bean = new BeanBDAccessCSV();
            bean.startConnection("c:\\mydb");
            System.out.println("Connection established");
            ResultSet rs = bean.sendQuery("select * from parts");
            while(rs.next()){
                System.out.println(((Integer)rs.getInt("FabricationTime")).toString());
            }
            bean.stopConnection();
            System.out.println("Deconnected");
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe){
           cnfe.printStackTrace();
        }
        

    }

}
