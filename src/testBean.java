import java.sql.SQLException;

import bddAccessObjects.BeanBDAccessMysql;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       
       
        try {
            BeanBDAccessMysql bean = new BeanBDAccessMysql();
            bean.startConnection("//127.0.0.1:3306/sakila?profileSQL=true");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe){
           
        }

    }

}
