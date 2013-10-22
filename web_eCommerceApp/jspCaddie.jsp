<%-- 
    Document   : jspCaddie
    Created on : Oct 14, 2013, 9:49:52 PM
    Author     : mike
--%>


<%@page import="java.util.TreeMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<% Object login = session.getAttribute("login.isDone");
   if(login == null){
       response.sendRedirect("login.html");
   }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>E-Caddie</title>
    </head>
    <body>
        <h1>New Order</h1>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="newOrder"/>
            <table>
                <thead>
                    <tr>
                        <td>Part type</td>
                        <td>Price</td>
                        <td>Quantity</td>
                    </tr>
                </thead>
                <tbody>
            <%  ResultSet rs = (ResultSet)request.getAttribute("stock");
                while (rs.next()) {
                    float price = Float.parseFloat(rs.getString("productionCost")) + 1;
                    String label = rs.getString("label");
                    int maxQuantity = rs.getInt("quantity");
               %>
                  <tr>
                    <td><%=label%></td>
                    <td><%=String.valueOf(price)%> €</td>
                    <td>
                        <input type="number" name="<%=label%>" min='0' max='<%=maxQuantity%>'/>
                    </td>
                  </tr>
               <%
               }
               %>
                </tbody>
            </table>
            <input type="submit" name="submit" value="Add to Cart"/>
        </form>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="newCart"/>
            <input type="submit" value="Empty your cart"/>
        </form>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="toPay"/>
            <input type="submit" value="Pay"/>
        </form>
    </body>
</html>
