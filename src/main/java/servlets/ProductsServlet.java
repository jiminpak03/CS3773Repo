package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Servlet implementation class ProductsServlet
 */
@WebServlet("/ProductsServlet")
@MultipartConfig(maxFileSize = 16177215)
public class ProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Sets array list of products to be displayed
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DOGET method accessed");
		HttpSession session = request.getSession();
		String change = (String) session.getAttribute("changeList");
		@SuppressWarnings("unchecked")
		ArrayList<String[]> list = (ArrayList<String[]>) session.getAttribute("list");
		
		//no change to product array list
		if((list != null) && ("false".equals(change))){
			System.out.println("list already set, no change to catalog");
			request.getRequestDispatcher("main.jsp?view=products").forward(request, response);
		}
		//modifies product array list
		else {
			ArrayList<String[]> products = new ArrayList<String[]>();
			String id;
			String name;
			String price;
			String quant;
			String sale;
			String desc;
			String image;

			try {
				//connects to database
				System.out.print("connecting to database\n");
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				
				PreparedStatement pst = con.prepareStatement("SELECT product_id, product_name, price, quantity, sale, product_description, image FROM product"); 
				ResultSet rs = pst.executeQuery();
				
				//retrieves all the products from database and places into array list
				while(rs.next()) {
					id = String.valueOf(rs.getInt("product_id"));
					name = rs.getString("product_name");
					price = String.format("%.2f", rs.getDouble("price"));
					quant = String.valueOf(rs.getInt("quantity"));
					int s = rs.getInt("sale");
					if(s > 0) {
						sale = String.valueOf(s);
					}
					else{
						sale = "";
					}
					desc = rs.getString("product_description"); 
					if(desc == null) {
						desc = "";
					}
					
					image = rs.getString("image"); //this will get path to image from database
					//image = "image";
					
					String p[] = {id, name, price, quant, sale, desc, image};
					products.add(p);
				}
				
				pst.close();
				con.close();
				
	            System.out.println("successfully displaying all products in array list");
	            session.setAttribute("list", products);
	            session.setAttribute("changeList", "false");
				request.getRequestDispatcher("main.jsp?view=products").forward(request, response);
				
			}catch(Exception e) {
				System.out.print("ERROR in ProductsServlet doGet: unable to retrieve products from database");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Alters data and product list based on button pressed
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String btnAdd = request.getParameter("btn-add-confirm");
		String btnEdit = request.getParameter("btn-edit-confirm");
		String btnDelete = request.getParameter("btn-delete");
		String btnSort = request.getParameter("btn-sort");
		String search = request.getParameter("searchProduct");
	
		//Adjusts array list based on search bar	
		if(search != null) {	
			System.out.println("\n\n====SEARCH BAR====="+ search);
			ArrayList<String[]> products = new ArrayList<String[]>();
			String id;
			String name;
			String price;
			String quant;
			String sale;
			String desc;
			String image;
	
			//Empty search bar
			if("".equals(search.trim())) {	
				try {
					System.out.println("\n\n====empty search bar --- displaying default...====");	
					System.out.print("connecting to database\n");
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
					
					PreparedStatement pst = con.prepareStatement("SELECT product_id, product_name, price, quantity, sale, product_description, image FROM product");
					ResultSet rs = pst.executeQuery();
					
					while(rs.next()) {
						id = String.valueOf(rs.getInt("product_id"));
						name = rs.getString("product_name");
						price = String.format("%.2f", rs.getDouble("price"));
						quant = String.valueOf(rs.getInt("quantity"));
						int s = rs.getInt("sale");
						if(s > 0) {
							sale = String.valueOf(s);
						}
						else{
							sale = "";
						}
						desc = rs.getString("product_description");
						
						//image = "has image";
						image = rs.getString("image");
						//js
						
						String p[] = {id, name, price, quant, sale, desc, image};
						products.add(p);
					}
					pst.close();
					con.close();
					
		            System.out.println("all products listed");
		            session.setAttribute("list", products);
					session.setAttribute("search", "");
		            session.setAttribute("changeList", "false");
				}catch(Exception e) {
					System.out.print("ERROR in ProductsServlet doPost: unable to search products from database");
					e.printStackTrace();
				}
			}
			
			//Text in search bar
			else {
				System.out.println("\n\n====SEARCH BAR====\nsearching for: " + search);
				try{
					//connects to database
					System.out.print("connecting to database\n");
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
					
					PreparedStatement pst = con.prepareStatement("SELECT * FROM product WHERE product_name LIKE ? OR product_description LIKE ?");
					pst.setString(1, "%" + search.trim() + "%");
					pst.setString(2, "%" + search.trim() + "%");
					ResultSet rs = pst.executeQuery();
					
					while(rs.next()) {
						id = String.valueOf(rs.getInt("product_id"));
						name = rs.getString("product_name");
						price = String.format("%.2f", rs.getDouble("price"));
						quant = String.valueOf(rs.getInt("quantity"));
						int s = rs.getInt("sale");
						if(s > 0) {
							sale = String.valueOf(s);
						}
						else{
							sale = "";
						}
						desc = rs.getString("product_description");
						image = "has image";
						
						String p[] = {id, name, price, quant, sale, desc, image};
						products.add(p);
					}
					pst.close();
					con.close();
					
		            System.out.println("search bar array set");
		            session.setAttribute("list", products);
					session.setAttribute("search", search);
		            session.setAttribute("changeList", "false");
				}catch(Exception e) {
					System.out.print("ERROR in ProductsServlet doPost: unable to search input text products from database");
					e.printStackTrace();
				}
			}
		}
		
		//adds a new product
		if(btnAdd != null) {
			System.out.println("\n\n====Adding a new product====");
			String name = request.getParameter("productName").trim();
			String price = request.getParameter("productPrice").trim();
			String quant = request.getParameter("productQuant").trim();
			String sale = request.getParameter("productSale").trim();
			String desc = request.getParameter("productDesc").trim();
			Part image = request.getPart("productImage"); 
		
			String imagePath = ""; //js
			
			try {
				//connects to database
				System.out.println("connecting to database..");
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				
				PreparedStatement pst = con.prepareStatement("INSERT INTO product(product_id, product_name, price, quantity, sale, product_description, image) VALUES (?, ?, ?, ?, ?, ?, ?)");
				InputStream is = image.getInputStream();
			
				//insert product based on input
				pst.setInt(1, 0);
				pst.setString(2, name);
				pst.setDouble(3, Double.valueOf(price));
				pst.setInt(4, Integer.valueOf(quant));
				if(!sale.isEmpty()) {
					pst.setInt(5, Integer.valueOf(sale));
				}
				else{
					pst.setInt(5, 0);
				}
				pst.setString(6, desc);
				pst.setBlob(7,is);

				pst.executeUpdate();
				pst.close();
				is.close();
				con.close();
				
				System.out.println("Successfully added a new product\n\n");
				session.setAttribute("changeList", "true");
				response.sendRedirect("ProductsServlet");
				
			}catch(Exception e) {
				System.out.println("ERROR in ProductsServlet doPost: unable to add new product\n\n");
				session.setAttribute("errorProducts", "Product was unable to be added (make sure image is a jpg)");
			    request.getRequestDispatcher("main.jsp?view=products").forward(request, response);
				e.printStackTrace();
			}
		}
		
		//edits a product
		else if(btnEdit != null) {
			System.out.println("\n\n====Editing a product====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> products = (ArrayList<String[]>) session.getAttribute("list");
			int index = Integer.valueOf(request.getParameter("index").trim());
			String[] product = products.get(index);
			int productId = Integer.parseInt(product[0]);
			
			String name = request.getParameter("productName").trim();
			String price = request.getParameter("productPrice").trim();
			String quant = request.getParameter("productQuant").trim();
			String sale = request.getParameter("productSale").trim();
			String desc = request.getParameter("productDesc").trim(); 
			
			
			try {
				//connects to database
				System.out.println("connecting to database");
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				System.out.println("choosing edit....");
				
				//edit name
				if(!name.equals(product[1])) {
					System.out.println("\tupdating name: " + name+ " to "+ product[1] + "...");
					PreparedStatement pst = con.prepareStatement("UPDATE product SET product_name=? WHERE product_id = ?");
					pst.setString(1, name);
					pst.setInt(2,productId);
					pst.executeUpdate();
					pst.close();
					System.out.println("\tsuccessfully updated name\n");
				}
				
				//edit price
				if(!price.equals(product[2])) {
					System.out.println("\tupdating price: " + price+ " to "+ product[2] + "...");
					PreparedStatement pst = con.prepareStatement("UPDATE product SET price=? WHERE product_id = ?");
					pst.setDouble(1, Double.valueOf(price));
					pst.setInt(2,productId);
					pst.executeUpdate();
					pst.close();
					System.out.println("\tsuccessfully updated price\n");
				}
				
				//edit quantity
				if(!quant.equals(product[3])) {
					System.out.println("\tupdating quantity: " + quant+ " to "+ product[3]+"...");
					PreparedStatement pst = con.prepareStatement("UPDATE product SET quantity=? WHERE product_id = ?");
					pst.setInt(1, Integer.valueOf(quant));
					pst.setInt(2,productId);
					pst.executeUpdate();
					pst.close();
					System.out.println("\tsuccesfully updated quantity\n");
				}
				
				//edit sale
				if(!sale.equals(product[4])) {
					System.out.println("\tupdating sale: " + sale+ " to "+ product[4]+"...");
					PreparedStatement pst = con.prepareStatement("UPDATE product SET sale=? WHERE product_id = ?");
					if(!sale.isEmpty()) {
						pst.setInt(1, Integer.valueOf(sale));
					}
					else{
						pst.setInt(1, 0);
					}
					pst.setInt(2,productId);
					pst.executeUpdate();
					pst.close();
					System.out.println("\tsuccesfully updated sale\n");
				}
				
				//edit description
				if(!desc.equals(product[5])){
					System.out.println("\tupdating description: " + desc+ " to "+ product[5]+"...");
					PreparedStatement pst = con.prepareStatement("UPDATE product SET product_description=? WHERE product_id = ?");
					pst.setString(1, desc);
					pst.setInt(2,productId);
					pst.executeUpdate();
					pst.close();
					System.out.println("\tsuccesfully updated description\n");
				}
			
				//edit image
				Part image = request.getPart("changeProductImage");
				if (image != null && image.getSize() > 0) {
					System.out.println("\tupdating the image...");
					try {
						//connects to database
						System.out.println("connecting to database..");
						PreparedStatement pst = con.prepareStatement("UPDATE product SET image=? WHERE product_id = ?");
						InputStream is = image.getInputStream();
	
						//insert product based on input
						pst.setBlob(1, is);
						pst.setInt(2,productId);

						pst.executeUpdate();
						System.out.println("\tsucessfully updated image\n");
						pst.close();
						is.close();
					}catch(Exception e) {
						System.out.println("ERROR in ProductsServlet doPost: unable change the image\n");
						session.setAttribute("errorProducts", "Error in changing product image (make sure image is a jpg)");
					    request.getRequestDispatcher("main.jsp?view=products").forward(request, response);
						e.printStackTrace();
					}
				}
				con.close();
				System.out.println("UPDATE COMPLETED\n\n");
				session.setAttribute("changeList", "true");
				response.sendRedirect("ProductsServlet");
			}catch(Exception e) {
				System.out.print("ERROR in ProductsServlet doPost: unable to update product");
				session.setAttribute("errorProducts", "Product was unable to be edited");
				e.printStackTrace();
			}
		}
		
		//deletes a product
		else if(btnDelete != null) {	
			System.out.println("\n\n====Deleting a product====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> products = (ArrayList<String[]>) session.getAttribute("list");
			int index = Integer.valueOf(request.getParameter("index").trim());
			String[] product = products.get(index);
			int productId = Integer.parseInt(product[0]);
			
			try {
				//connects to database
				System.out.println("connecting to database...");
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				
				//delete product based on product_id
				PreparedStatement pst = con.prepareStatement("DELETE FROM product WHERE product_id = ?");
				pst.setInt(1, productId);
				System.out.println("deleting: " + productId + " " + product[1]);
				
				pst.executeUpdate();
				pst.close();
				con.close();
				
				System.out.println("Successfully deleted product\n\n");
				session.setAttribute("changeList", "true");
				response.sendRedirect("ProductsServlet");
				
			}catch(Exception e) {
				System.out.print("ERROR in ProductsServlet doPost: unable to delete product");
				e.printStackTrace();
			}
		}
		
		//Sorts products based on drop down 'sort by'
		else if(btnSort != null) {
			System.out.println("\n\n====Sorting by: "+btnSort+"====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> products = (ArrayList<String[]>) session.getAttribute("list");
			
			if(products == null) {
				System.out.println("Sorting: list is null\n\n");
				response.sendRedirect("ProductsServlet");
			}
			
			Collections.sort(products, new Comparator<String[]>() {
				public int  compare(String[] p1, String[] p2) {
					//calculate price with sale
					double price1 = Double.valueOf(p1[2]);
					double price2 = Double.valueOf(p2[2]);
					double salePrice1;
					double salePrice2;
					
					if(p1[4] != null && (!p1[4].isEmpty()) && Integer.parseInt(p1[4]) > 0) {
						salePrice1 = ((Integer.valueOf(p1[4]))/100.0) * price1;
						salePrice1 = price1 - salePrice1;
					}
					else {
						salePrice1 = price1;
					}
					if(p2[4] != null && (!p2[4].isEmpty()) && Integer.parseInt(p2[4]) > 0) {
						salePrice2 = ((Integer.valueOf(p2[4]))/100.0) * price2;
						salePrice2 = price2 - salePrice2;
					}
					else {
						salePrice2 = price2;
					}
					
					//sorts product list by price Low to High
					if (btnSort.equals("price1") && products!=null) {
						session.setAttribute("changeList", "false");
						session.setAttribute("sort", "price1");
						return Double.valueOf(salePrice1).compareTo(Double.valueOf(salePrice2));
					}
					//sorts product list by price High to Low
					else if(btnSort.equals("price2")) {
						session.setAttribute("changeList", "false");
						session.setAttribute("sort", "price2");
						return Double.valueOf(salePrice2).compareTo(Double.valueOf(salePrice1));
					}
					//sorts product list by availability Low to High
					else if(btnSort.equals("available1")) {
						session.setAttribute("changeList", "false");
						session.setAttribute("sort", "available1");
						return Integer.valueOf(p1[3]).compareTo(Integer.valueOf((p2[3])));
					}
					//sorts product list by availability High to Low
					else if(btnSort.equals("available2")) {
						session.setAttribute("changeList", "false");
						session.setAttribute("sort", "available2");
						return Integer.valueOf(p2[3]).compareTo(Integer.valueOf((p1[3])));
					}
					//sorts product list by default 
					else {
						session.setAttribute("changeList", "false");
						session.setAttribute("sort", "default");
						return Integer.valueOf(p1[0]).compareTo(Integer.valueOf((p2[0])));
					}
				}
			});	
			response.sendRedirect("ProductsServlet");
		}
		else {
			System.out.println("No button registered in ProductsServlet\n\n");
			response.sendRedirect("ProductsServlet");
		}	
	}

}
