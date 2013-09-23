import java.sql.SQLException;

import bddAccessObjects.BeanBDAccessMysql;


public class testBean {

    /**
     * @param args
     */
    public static void main(String[] args) {
       BeanBDAccessMysql bean = new BeanBDAccessMysql();
       
       try {
        bean.startConnection("//127.0.0.1:3306/sakila?profileSQL=true");
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }

}
