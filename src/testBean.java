import java.sql.SQLException;

import bddAccessObjects.BeanBDAccessMysql;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       BeanBDAccessMysql bean = new BeanBDAccessMysql();
       
       try {
        bean.startConnection("//127.0.0.1/mydb");
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }

}
