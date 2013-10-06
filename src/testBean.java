import java.sql.SQLException;
import java.sql.ResultSet;

import containerDbAccess.ContainerAccess;
import dbAccessObjects.CSVDbAccess;
import dbAccessObjects.MysqlDbAccess;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       
       System.out.println("Trying to connect...");
        try {
            
            MysqlDbAccess bean = new MysqlDbAccess();
            bean.startConnection("//127.0.0.1:3306/mydb", "root", "root");
            System.out.println("Connection established");
            ResultSet rs = bean.sendQuery("select count(*) from parts");
            while(rs.next()){
                //System.out.println(((Integer)rs.getInt("fabricationTime")).toString());
                System.out.println(rs.getInt("count(*)"));
            }
            bean.stopConnection();
            System.out.println("Deconnected");

            
            /*
            BeanBDAccessCSV bean = new BeanBDAccessCSV();
            bean.startConnection("c:\\mydb");
            System.out.println("Connection established");
            ResultSet rs = bean.sendQuery("select * from parts");
            while(rs.next()){
                System.out.println(((Integer)rs.getInt("FabricationTime")).toString());
            }
            bean.stopConnection();
            System.out.println("Deconnected");
            */
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe){
           cnfe.printStackTrace();
        }
        

    }

}
