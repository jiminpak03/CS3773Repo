package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String firstName;
		String lastName;
		String email;
		int id;
		
		//connects to the database and verifies login input
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
			
			//checks if username and password match in the database
			PreparedStatement pst = con.prepareStatement("SELECT * FROM employee WHERE username=? AND user_password=?");

			pst.setString(1,username);
			pst.setString(2, password);
			
			ResultSet rs = pst.executeQuery();
			
			//if they match, user gets logged into main page
			if(rs.next()) {
				id = rs.getInt("employee_id");
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				email = rs.getString("email");
				
				session.setAttribute("id", id);
				session.setAttribute("username", username);		
				session.setAttribute("name", firstName + " " + lastName);
				session.setAttribute("email", email);
				response.sendRedirect("main.jsp");
			}
			//else an error message gets displayed
			else {
				session.setAttribute("errorLogin", "Incorrect username or password");
				response.sendRedirect("login.jsp");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

