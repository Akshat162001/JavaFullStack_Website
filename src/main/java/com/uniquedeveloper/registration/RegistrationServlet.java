package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        Connection con = null;
        PreparedStatement ps = null;

        // Retrieve form parameters
        String uname = request.getParameter("name");
        String uemail = request.getParameter("email");
        String upass = request.getParameter("pass");
        String ucontact = request.getParameter("contact");

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/student?useSSL=false", "root", "tiger");

            // Prepare SQL statement
            ps = con.prepareStatement("INSERT INTO student (uname, uemail, ucontact,upass) VALUES (?, ?, ?, ?)");
            ps.setString(1, uname);
            ps.setString(2, uemail);
            ps.setString(3, ucontact);
            ps.setString(4, upass);

            // Execute update and forward based on result
            int rowCount = ps.executeUpdate();
            dispatcher = request.getRequestDispatcher("registration.jsp");

            if (rowCount > 0) {
                request.setAttribute("status", "success");
            } else {
                request.setAttribute("status", "failure");
            }
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (dispatcher != null) {
                request.setAttribute("status", "error");
                dispatcher.forward(request, response);
            }
        } finally {
            // Clean up resources
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
