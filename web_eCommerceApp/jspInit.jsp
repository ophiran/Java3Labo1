<%-- 
    Document   : jspInit
    Created on : Oct 14, 2013, 9:36:56 PM
    Author     : mike
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MetalBuilding</title>
    </head>
    <body>
        <h1> Make a choice </h1>
        <% Object login = session.getAttribute("login.isDone");
           if(login != null){
                out.println("Hello dear " + (String)login);
           } else {
               response.sendRedirect("login.html");
           }
        %>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="toContest"/>
            <input type="submit" value="Take part in our contest"/>
        </form>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="toCaddie"/>
            <input type="submit" value="Start shopping"/>
        </form>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="toPay"/>
            <input type="submit" value="Pay and log out"/>
        </form>
    </body>
</html>
