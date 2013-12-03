package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dbAccessObjects.MysqlDbAccess;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mike
 */
//@WebServlet(name = "ServletLogin", urlPatterns = {"/ServletLogin"})
public class ServletLogin extends HttpServlet {
    private PrintWriter out;
    private static final Logger logger = Logger.getGlobal();
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
        logger.info("Entering Get");
        if (request.getParameter("action").equals("signIn")) {
            try {
                response.setContentType("text/html");
                out = response.getWriter();

                ResultSet rs = beanAccess.sendQuery("SELECT login, password "
                                                        + "FROM clients "
                                                        + "WHERE login = '" + request.getParameter("login")
                                                        + "' AND password = '" + request.getParameter("password") + "'");
                if( rs.next()) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("login.isDone", request.getParameter("login"));
                    session.setAttribute("login.pass", request.getParameter("password"));
                    TreeMap<String, Integer> cartStock = new TreeMap<String, Integer>();
                    session.setAttribute("cartStock", cartStock);
                    TreeMap<String, Integer> cartOrders = new TreeMap<String, Integer>();
                    session.setAttribute("cartOrders", cartOrders);
                    response.sendRedirect("jspInit.jsp");
                } else {
                    response.sendRedirect("errorLogin.html");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                out.println(ex.getMessage());
            }
        }
        
        if (request.getParameter("action").equals("signUp")) {
            try {
                response.setContentType("text/html");
                out = response.getWriter();
                
                ResultSet rsId = beanAccess.sendQuery("SELECT MAX(idClients) FROM clients");
                int id;
                if(rsId.next()){
                    id = rsId.getInt("MAX(idClients)") + 1;
                }
                else {
                    id = 1;
                }
                
                ResultSet rs = beanAccess.sendQuery("SELECT * FROM clients WHERE login = '"+ request.getParameter("login") +"'");
                if(rs.next()){
                    logger.info("User already exist");
                    response.sendRedirect("registration.html");
                }
                else {
                    beanAccess.insertRow("INSERT INTO clients (idClients, firstName, login, lastName, address, phoneNumber, email, password) "
                                       + "VALUES (" + id + ", '" + request.getParameter("firstname") + "','"+ request.getParameter("login")
                                       + "','"+ request.getParameter("lastname") + "','" + request.getParameter("address") + "','"+ request.getParameter("phonenumber")
                                       + "','" + request.getParameter("email") + "','"+ request.getParameter("password") + "')");
                    response.sendRedirect("login.html");
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                out.println(ex.getMessage());
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
}
