package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * Retrieves image to be displayed
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int productId = Integer.parseInt(request.getParameter("id"));
		
		try {
			//connects to database 
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
			
			//selects corresponding image
			PreparedStatement pst = con.prepareStatement("SELECT image FROM product WHERE product_id = ?"); 
			pst.setInt(1,productId);
			ResultSet rs = pst.executeQuery();
			
			//outputs image
			if(rs.next()) {
				//changes made by js - image from blob to vchar
				String imagePath = rs.getString("image");
	            if(imagePath != null && !imagePath.isEmpty()) {
	                response.sendRedirect(imagePath);
	            }
				/*
				byte[] img = rs.getBlob("image").getBytes(1, (int) rs.getBlob("image").length());
				response.setContentType("image/jpeg");
				OutputStream os = response.getOutputStream();
				
				os.write(img);
				os.flush();
				os.close();
				*/
			}
			pst.close();
			con.close();
		}catch(Exception e) {
			System.out.println("ERROR: ImageServlet doGet: Faiure to retrieve images from data base");
		}
	}


}
