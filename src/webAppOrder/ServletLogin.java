package webAppOrder;

import java.io.IOException;
import java.io.PrintWriter;

import dbDataObjects.Client;

import javax.servlet.*;
import javax.servlet.http.*;

import containerDbAccess.ContainerAccess;


public class ServletLogin extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/text");
		Client loggerClient = new Client(request.getParameter("name"), request.getParameter("pass"));
		ContainerAccess db = ContainerAccess.getInstance();
		
		if (db.getClients().contains(loggerClient)) {
			response.sendRedirect("http://localhost:80/welcome.html");
		} else {
			response.sendRedirect("http://localhost:80/errorLogin.html");
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
