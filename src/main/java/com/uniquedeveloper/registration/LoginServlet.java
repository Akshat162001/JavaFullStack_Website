package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		 String uemail = request.getParameter("username");
	        String upass = request.getParameter("password");

	        HttpSession session = request.getSession();
	        RequestDispatcher dispatcher = null;

	        try {
	            // Load MySQL JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            // Establish connection to the database
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/student?useSSL=false", "root", "tiger");

	            // Prepare SQL query to verify the user credentials
	            PreparedStatement pst = con.prepareStatement("SELECT * FROM student WHERE uemail = ? AND upass = ?");
	            pst.setString(1, uemail);
	            pst.setString(2, upass);

	            // Execute the query
	            ResultSet rs = pst.executeQuery();

	            // If user credentials match, set session attribute and redirect to the index page
	            if (rs.next()) {
	                session.setAttribute("name", rs.getString("uname")); // Store user's name in session
	                dispatcher = request.getRequestDispatcher("index.jsp");
	            } else {
	                // If no matching user found, set an error message and forward to login page
	                request.setAttribute("status", "Failed something is wrong");
	                dispatcher = request.getRequestDispatcher("login.jsp");
	            }

	            // Forward the request to the appropriate page
	            dispatcher.forward(request, response);

	        } catch (Exception e) {
	            // Handle any exceptions (e.g., database errors)
	            e.printStackTrace();
//	            request.setAttribute("errorMessage", "An error occurred. Please try again.");
//	            dispatcher = request.getRequestDispatcher("login.jsp");
//	            dispatcher.forward(request, response);
	        }
	    }
	 
}

