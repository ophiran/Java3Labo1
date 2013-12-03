package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dbAccessObjects.MysqlDbAccess;
import dbDataObjects.PartsType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import productionLib.LoginRequest;
import productionLib.LoginResponse;
import productionLib.OrderRequest;
import productionLib.OrderResponse;

/**
 *
 * @author mike
 */
public class ServletControl extends HttpServlet implements HttpSessionListener{
    private PrintWriter out;
    private MysqlDbAccess beanAccess;
    private Socket socketOrder;
    private DataOutputStream dos;
    private DataInputStream dis;
    
    @Override
    public void init() {
        try {
            beanAccess = new MysqlDbAccess();
            beanAccess.startConnection("//127.0.0.1:3306/mydb", "root", "");
            socketOrder = new Socket("192.168.187.128", 2000); // TODO get ip from properties
            dos = new DataOutputStream(socketOrder.getOutputStream());
            dis = new DataInputStream(socketOrder.getInputStream());
            String clientInfo = "192.168.1.111:0\r\n";
            dos.write(clientInfo.getBytes());
            String connectionStatus = readNetwork(dis);
            if (connectionStatus == "notAvailable") {
                Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, "Server orders not available", "");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void destroy() {
        if (beanAccess != null) {
            try {
                beanAccess.stopConnection();
                dis.close();
                dos.close();
                socketOrder.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
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
            TreeMap<String, Integer> cartStock = (TreeMap<String, Integer>) session.getAttribute("cartStock");
            if(cartStock != null) {
                emptyCart(cartStock);
            }
            cartStock = new TreeMap<String, Integer>();
            session.setAttribute("cartStock", cartStock);
            //TreeMap<String, Integer> cartOrders = (TreeMap<String, Integer>) session.getAttribute("cartOrders");
            TreeMap<String, Integer> cartOrders = new TreeMap<String, Integer>();
            session.setAttribute("cartOrders", cartOrders);
            reloadCart(request, response);
        }
        if(request.getParameter("action").equals("newOrder")) {
            try {
                TreeMap<String, Integer> cartStock = (TreeMap<String, Integer>) session.getAttribute("cartStock");
                TreeMap<String, Integer> cartOrders = (TreeMap<String, Integer>) session.getAttribute("cartOrders");
                if(cartStock != null && cartOrders != null) {
                    response.setContentType("text/html;charset=UTF-8");
                    out = response.getWriter();

                    Enumeration<String> parametersList = request.getParameterNames();
                    do {
                        String parameter = parametersList.nextElement();
                        int newQuantity;
                        if(!request.getParameter(parameter).equals("") && !parameter.equals("action") && !parameter.equals("submit")) {
                            ResultSet rs = beanAccess.sendQuery("SELECT quantity FROM parts WHERE label ='" + parameter + "'");
                            rs.next();
                            int dbQuantity = rs.getInt("quantity");
                            int neededQuantity = Integer.valueOf(request.getParameter(parameter));
                            int quantityFromStock;
                            int quantityToOrder;
                            /*Determine how much is taken directly from stock and how much
                              will need to be ordered */
                            if (neededQuantity > dbQuantity) {
                                quantityToOrder = neededQuantity - dbQuantity;
                                quantityFromStock = dbQuantity;
                            } else {
                                quantityToOrder = 0;
                                quantityFromStock = neededQuantity;
                            }
                            
                            //Update stock's cartStock
                            if (cartStock.containsKey(parameter)) {
                                newQuantity = cartStock.get(parameter) + quantityFromStock;
                            } else {
                                newQuantity = quantityFromStock;
                            }
                            cartStock.put(parameter, newQuantity);
                            
                            //Update data base
                            beanAccess.updateRow("UPDATE parts SET quantity=quantity-" + String.valueOf(quantityFromStock)
                                               + " WHERE label='" + parameter + "'");
                            
                            //Update orders' cartStock
                            if (cartOrders.containsKey(parameter)) {
                                newQuantity = cartOrders.get(parameter) + quantityToOrder;
                            } else {
                                newQuantity = quantityToOrder;
                            }
                            cartOrders.put(parameter, newQuantity);
                            
                             
                        }
                    }while(parametersList.hasMoreElements());
                    reloadCart(request, response);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                out.println("ERROR SQL");
                out.println(ex.getMessage());
            }
        }
       if(request.getParameter("action").equals("toPay")) {
           double totalPrice = 0;
           ResultSet rs;
           //Adds price for each part in stock's cartStock
           TreeMap<String, Integer> cartStock = (TreeMap<String, Integer>) session.getAttribute("cartStock");
            if(cartStock != null) {
                for(String s: cartStock.keySet()) {
                    if(cartStock.get(s) > 0) {
                        try {
                            rs = beanAccess.sendQuery("SELECT productionCost FROM parts WHERE label='" + s + "'");
                            rs.next();
                            totalPrice += ((rs.getDouble("productionCost") + 1)*cartStock.get(s));
                        } catch (SQLException ex) {
                            Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            //Adds price for each part in orders' cartStock
            TreeMap<String, Integer> cartOrders = (TreeMap<String, Integer>) session.getAttribute("cartOrders");
            if(cartOrders != null) {
                for(String s: cartOrders.keySet()) {
                    if(cartOrders.get(s) > 0) {
                        try {
                            rs = beanAccess.sendQuery("SELECT productionCost FROM parts WHERE label='" + s + "'");
                            rs.next();
                            totalPrice += ((rs.getDouble("productionCost") + 1)*cartOrders.get(s));
                        } catch (SQLException ex) {
                            Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            request.setAttribute("total", totalPrice);
            RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/jspPay.jsp");
            reqDispatcher.forward(request,response);
           //response.sendRedirect("jspPay.jsp");
       }
       if(request.getParameter("action").equals("toCaddie")) {
           reloadCart(request, response);
       }
       if(request.getParameter("action").equals("toContest")) {
           response.sendRedirect("contest.html");
       }
       if(request.getParameter("action").equals("pay")) {
            try {
                //Place an order in the database for the parts ordered from stock
                TreeMap<String, Integer> cartStock = (TreeMap<String, Integer>) session.getAttribute("cartStock");
                if (cartStock != null) {
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
                    for(String s: cartStock.keySet()) {
                        if(cartStock.get(s) > 0) {
                            id++;
                            beanAccess.updateRow("INSERT INTO orders (idOrders, date, refClient, partType, quantity) "
                                               + "VALUES (" + String.valueOf(id) +", '" + nowDate.toString() + "', " + refClient
                                               + ", '" + s + "', " + cartStock.get(s) + ")");
                        }
                    }
                }
                //Send orders to Orders' server
                TreeMap<String, Integer> cartOrders = (TreeMap<String, Integer>) session.getAttribute("cartOrders");
                if (cartOrders != null) {
                    LoginRequest req = new LoginRequest((String) session.getAttribute("login.isDone")
                            , (String) session.getAttribute("login.pass"));
                    dos.write(req.networkString().getBytes());
                    String receivedStr = readNetwork(dis);
                    LoginResponse resp = new LoginResponse(receivedStr);
                    if (resp.ack == true) {
                        for(String s: cartOrders.keySet()) {
                            if(cartOrders.get(s) > 0) {
                                Date desiredDate = new Date();
                                Calendar c = Calendar.getInstance();
                                c.setTime(desiredDate);
                                c.add(Calendar.WEEK_OF_YEAR, 1);
                                OrderRequest orderReq = new OrderRequest(PartsType.valueOf(s), cartOrders.get(s), c.getTime());
                                dos.write(orderReq.networkString().getBytes());
                                String orderRespStr = readNetwork(dis);
                                OrderResponse orderResp = new OrderResponse(orderRespStr);
                                if (orderResp.ack == false) {
                                    // TODO error
                                }
                            }
                        }
                    }
                }
                session.setAttribute("cartOrders", null);
                session.setAttribute("cartStock", null);
                session.invalidate();
                response.sendRedirect("index.html");
            } catch (SQLException ex) {
                out.println(ex.getMessage());
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    private void reloadCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
                ResultSet rs = beanAccess.sendQuery("SELECT label, productionCost, quantity FROM parts");
                request.setAttribute("stock", rs);
                RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/jspCaddie.jsp");
                reqDispatcher.forward(request,response);
            } catch (SQLException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String readNetwork(DataInputStream dis) {
        boolean endingEncountered = false;
        boolean halfEndingEncountered = false;
        StringBuffer receiveMsg = new StringBuffer();
        while (!endingEncountered) {
            try {
                byte b = dis.readByte();
                if (b == (byte) '\r') {
                    halfEndingEncountered = true;
                } else if (halfEndingEncountered && (b == (byte) '\n')) {
                    endingEncountered = true;
                } else {
                    receiveMsg.append((char) b);
                    halfEndingEncountered = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(ServletControl.class.getName()).log(Level.SEVERE, "readByte failed", ex);
            }
        }
        return receiveMsg.toString().trim();
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        TreeMap<String, Integer> cart = (TreeMap<String, Integer>) se.getSession().getAttribute("cartStock");
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
