package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dbAccessObjects.MysqlDbAccess;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpUtils;

/**
 *
 * @author mike
 */
public class ServletControl extends HttpServlet implements HttpSessionListener{
    private PrintWriter out;
    private MysqlDbAccess beanAccess;
    
    @Override
    public void init() {
        try {
            beanAccess = new MysqlDbAccess();
            beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void destroy() {
        if (beanAccess != null) {
            try {
                beanAccess.stopConnection();
            } catch (SQLException ex) {
                Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
        HttpSession session = request.getSession(true);
        Object exist = session.getAttribute("login.isDone");
        if (exist==null)
        {
            response.sendRedirect("login.html");
            return;
        }
        if(request.getParameter("action").equals("authorize")) {
            response.sendRedirect("jspInit.html");
        }
        if(request.getParameter("action").equals("newCart")) {
            TreeMap<String, Integer> cart = (TreeMap<String, Integer>) session.getAttribute("cart");
            if(cart != null) {
                emptyCart(cart);
            }
            cart = new TreeMap<String, Integer>();
            session.setAttribute("cart", cart);
            response.sendRedirect("jspCaddie.jsp");
        }
        if(request.getParameter("action").equals("newOrder")) {
            try {
                TreeMap<String, Integer> cart = (TreeMap<String, Integer>) session.getAttribute("cart");
                if(cart != null) {
                    response.setContentType("text/html");
                    out = response.getWriter();

                    Enumeration<String> parametersList = request.getParameterNames();
                    do {
                        String parameter = parametersList.nextElement();
                        int newQuantity;
                        if(!request.getParameter(parameter).equals("") && !parameter.equals("action") && !parameter.equals("submit")) {
                            if (cart.containsKey(parameter)) {
                                newQuantity = cart.get(parameter) + Integer.valueOf(request.getParameter(parameter));
                            } else {
                                newQuantity = Integer.valueOf(request.getParameter(parameter));
                            }
                            cart.put(parameter, newQuantity);
                            beanAccess.updateRow("UPDATE parts SET quantity=quantity-" + request.getParameter(parameter)
                                               + " WHERE label='" + parameter + "'");
                        }
                    }while(parametersList.hasMoreElements());
                    response.sendRedirect("jspCaddie.jsp");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                out.println("ERROR SQL");
                out.println(ex.getMessage());
            }
        }
       if(request.getParameter("action").equals("toPay")) {
           response.sendRedirect("jspPay.jsp");
       }
       if(request.getParameter("action").equals("toCaddie")) {
           response.sendRedirect("jspCaddie.jsp");
       }
       if(request.getParameter("action").equals("toContest")) {
           response.sendRedirect("contest.html");
       }
       if(request.getParameter("action").equals("pay")) {
            try {
                TreeMap<String, Integer> cart = (TreeMap<String, Integer>) session.getAttribute("cart");
                if(cart != null) {
                    java.sql.Date nowDate = new java.sql.Date(new java.util.Date().getTime());
                    ResultSet rs = beanAccess.sendQuery("SELECT MAX(idOrders) FROM orders");
                    ResultSet rsClientId = beanAccess.sendQuery("SELECT idClients FROM clients WHERE login='" + session.getAttribute("login.isDone") + "'");
                    rsClientId.next();
                    int refClient = rsClientId.getInt("idClients");
                    int id;
                    if(rs.next()) {
                        id = rs.getInt("MAX(idOrders)");
                    } else {
                        id = 0;
                    }
                    for(String s: cart.keySet()) {
                        if(cart.get(s) > 0) {
                            id++;
                            beanAccess.updateRow("INSERT INTO orders (idOrders, date, refClient, partType, quantity) "
                                               + "VALUES (" + String.valueOf(id) +", '" + nowDate.toString() + "', " + refClient
                                               + ", '" + s + "', " + cart.get(s) + ")");
                        }
                    }
                }
                session.setAttribute("cart", null);
                session.invalidate();
                response.sendRedirect("index.html");
            } catch (SQLException ex) {
                out.println(ex.getMessage());
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    
    private void emptyCart(TreeMap<String, Integer> cart) {
        try {
                MysqlDbAccess beanAccess2 = new MysqlDbAccess();
                beanAccess2.startConnection("//127.0.0.1:3306/mydb", "root", "");
                for(String s: cart.keySet()) {
                    beanAccess2.updateRow("UPDATE parts SET quantity=quantity+" + String.valueOf(cart.get(s))
                                               + " WHERE label='" + s + "'");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        TreeMap<String, Integer> cart = (TreeMap<String, Integer>) se.getSession().getAttribute("cart");
        if(cart != null) {
            emptyCart(cart);
        }
    }
        
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        processRequest(request, response);      
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {

        processRequest(request, response);
    }
        

}
