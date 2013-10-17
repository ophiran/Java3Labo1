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
import javax.servlet.http.HttpUtils;

/**
 *
 * @author mike
 */
public class ServletControl extends HttpServlet {
    PrintWriter out;
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
            response.sendRedirect("welcome.html");
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

                    MysqlDbAccess beanAccess = new MysqlDbAccess();
                    beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
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
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
                out.println("ERROR SQL");
                out.println(ex.getMessage());
            }
        }
       if(request.getParameter("action").equals("toPay")) {
           response.sendRedirect("jspPay.jsp");
       }
       if(request.getParameter("action").equals("pay")) {
            try {
                TreeMap<String, Integer> cart = (TreeMap<String, Integer>) session.getAttribute("cart");
                if(cart != null) {
                    MysqlDbAccess beanAccess = new MysqlDbAccess();
                    beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
                    ResultSet rs = beanAccess.sendQuery("SELECT MAX(idOrders) FROM orders");
                    int id;
                    if(rs.next()) {
                        id = rs.getInt("MAX(idOrders)");
                    } else {
                        id = 0;
                    }
                    for(String s: cart.keySet()) {
                        if(cart.get(s) > 0) {
                            id++;
                            beanAccess.updateRow("INSERT INTO orders (idOrders, partType, quantity) "
                                               + "VALUES (" + String.valueOf(id) + ", '" + s + "', "
                                               + cart.get(s) + ")");
                        }
                    }
                }
                session.invalidate();
                response.sendRedirect("index.html");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                out.println(ex.getMessage());
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        
        private void emptyCart(TreeMap<String, Integer> cart) {
            try {
                MysqlDbAccess beanAccess = new MysqlDbAccess();
                beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
                for(String s: cart.keySet()) {
                    beanAccess.updateRow("UPDATE parts SET quantity=quantity+" + String.valueOf(cart.get(s))
                                               + " WHERE label='" + s + "'");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
