import java.sql.SQLException;

import bddAccessObjects.BeanBDAccessCSV;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       
       
        try {
            BeanBDAccessCSV bean = new BeanBDAccessCSV();
            bean.startConnection("/127.0.0.1:3306/mydb");
            Thread.sleep(2000);
            bean.stopConnection();
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe){
           
        } catch (InterruptedException ie) {
            
        }
        

    }

}
