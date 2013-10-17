<%-- 
    Document   : jspPay
    Created on : Oct 14, 2013, 9:49:40 PM
    Author     : mike
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payement page</title>
    </head>
    <body>
        <h1>Click to pay and exit your session </h1>
        <form method="POST" action="ServletControl">
            <input type="hidden" name="action" value="pay"/>
            <input type="submit" name="Pay and quit"value="Pay"/>
        </form>
    </body>
</html>
