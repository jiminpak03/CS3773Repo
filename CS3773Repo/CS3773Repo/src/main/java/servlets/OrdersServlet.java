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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Servlet implementation class OrdersServlet
 */
@WebServlet("/OrdersServlet")
@MultipartConfig(maxFileSize = 16177215)
public class OrdersServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public OrdersServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("OrdersServlet called");
		System.out.println("DOGET method accessed");
		HttpSession session = request.getSession();
		String change = (String) session.getAttribute("changeOrderList");
	    @SuppressWarnings("unchecked")
		ArrayList<String[]> list = (ArrayList<String[]>) session.getAttribute("orderList");
	    
	    // No change to order list
	    if((list != null) && ("false".equals(change))) {
	    	System.out.println("order list already set. no change to catalog.");
	    	request.getRequestDispatcher("main.jsp?view=orders").forward(request, response);
	    } 
	    //Load orders from database
	    else {
	    	ArrayList<String[]> orders = new ArrayList<String[]>();
	    	String id;
	    	String time;
	    	String price;
	    	String lastname;
	    	String firstname;
	    	String employee;
	    	String status;
	    	
	    	try {
	    		System.out.print("connecting to database\n");
	    		Class.forName("com.mysql.cj.jdbc.Driver");
	    		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
			
	    		PreparedStatement pst = con.prepareStatement("SELECT order_id, order_time, order_price, customer_lastname, customer_firstname, assigned_employee, execution_status FROM orders"); 
	    		ResultSet rs = pst.executeQuery();
			
	    		//Add orders to the ArrayList
	    		while(rs.next()) {
	    			id = String.valueOf(rs.getLong("order_id"));
	    			time = rs.getString("order_time");
	    			price = String.format("%.2f", rs.getDouble("order_price"));
	    			lastname = rs.getString("customer_lastname");
	    			if(lastname == null) lastname = "";
	    			firstname = rs.getString("customer_firstname");
	    			if(firstname == null) firstname = "";
	    			
	    			//Handle null assigned_employee properly
	    			Long empId = rs.getLong("assigned_employee");
	    			if(rs.wasNull()) {
	    				employee = "Unassigned";
	    			} else {
	    				employee = String.valueOf(empId);
	    			}
	    			
	    			status = rs.getString("execution_status");
	    			if(status == null) status = "Pending";
	    			
	    			//Create order array and add to list
	    			String[] order = {id, time, price, lastname, firstname, employee, status};
	    			orders.add(order);
	    			System.out.println("Added order: " + Arrays.toString(order));
	    		}
	    		
	    		pst.close();
	    		con.close();
			
	    		System.out.println("successfully displaying all orders in array list. Total: " + orders.size());
	    		session.setAttribute("orderList", orders);
	    		session.setAttribute("changeOrderList", "false");
	    		request.getRequestDispatcher("main.jsp?view=orders").forward(request, response);
	    		
	    	} catch(Exception e) {
	    		System.out.print("ERROR in OrdersServlet doGet: unable to retrieve orders from database");
	    		e.printStackTrace();
	    		//Set empty list to prevent further errors
	    		session.setAttribute("orderList", new ArrayList<String[]>());
	    		request.getRequestDispatcher("main.jsp?view=orders").forward(request, response);
	    	}
	    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String search = request.getParameter("searchOrder");
		String btnSort = request.getParameter("btn-sort");
		String btnExecute = request.getParameter("btn-execute");
		String btnUpdateStatus = request.getParameter("btn-update-status");
		
		if(search != null) {
			System.out.println("\n\n====SEARCH BAR====="+ search);
			ArrayList<String[]> orders = new ArrayList<String[]>();
			String id;
	    	String time;
	    	String price;
	    	String lastname;
	    	String firstname;
	    	String employee;
	    	String status;
	    	
	    	//Empty search - show all orders
	    	if("".equals(search.trim())) {	
				try {
					System.out.println("\n\n====empty search bar --- displaying default...====");	
					System.out.print("connecting to database\n");
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
					
					PreparedStatement pst = con.prepareStatement("SELECT order_id, order_time, order_price, customer_lastname, customer_firstname, assigned_employee, execution_status FROM orders");
					ResultSet rs = pst.executeQuery();
					
					while(rs.next()) {
		    			id = String.valueOf(rs.getLong("order_id"));
		    			time = rs.getString("order_time");
		    			price = String.format("%.2f", rs.getDouble("order_price"));
		    			lastname = rs.getString("customer_lastname");
		    			if(lastname == null) lastname = "";
		    			firstname = rs.getString("customer_firstname");
		    			if(firstname == null) firstname = "";
		    			
		    			Long empId = rs.getLong("assigned_employee");
		    			if(rs.wasNull()) {
		    				employee = "Unassigned";
		    			} else {
		    				employee = String.valueOf(empId);
		    			}
		    			
		    			status = rs.getString("execution_status");
		    			if(status == null) status = "Pending";
		    			
		    			String[] order = {id, time, price, lastname, firstname, employee, status};
		    			orders.add(order);
		    		}
		    		pst.close();
		    		con.close();
		    		
		    		System.out.println("all orders listed");
		    		session.setAttribute("orderList", orders);
		    		session.setAttribute("orderSearch", "");
		    		session.setAttribute("changeOrderList", "false");
		    		
				}catch(Exception e) {
					System.out.print("ERROR in OrdersServlet doPost: unable to search orders from database");
					e.printStackTrace();
				}
	    	}
	    	//Search with criteria
	    	else {
				System.out.println("\n\n====SEARCH BAR====\nsearching for: " + search);
				try {
					System.out.print("connecting to database\n");
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
					
					PreparedStatement pst = con.prepareStatement("SELECT order_id, order_time, order_price, customer_lastname, customer_firstname, assigned_employee, execution_status FROM orders WHERE order_time LIKE ? OR customer_lastname LIKE ? OR customer_firstname LIKE ? OR order_price LIKE ? OR execution_status LIKE ?");
					pst.setString(1, "%" + search.trim() + "%");
					pst.setString(2, "%" + search.trim() + "%");
					pst.setString(3, "%" + search.trim() + "%");
					pst.setString(4, "%" + search.trim() + "%");
					pst.setString(5, "%" + search.trim() + "%");
					ResultSet rs = pst.executeQuery();
					
					while(rs.next()) {
						id = String.valueOf(rs.getLong("order_id"));
		    			time = rs.getString("order_time");
		    			price = String.format("%.2f", rs.getDouble("order_price"));
		    			lastname = rs.getString("customer_lastname");
		    			if(lastname == null) lastname = "";
		    			firstname = rs.getString("customer_firstname");
		    			if(firstname == null) firstname = "";
		    			
		    			Long empId = rs.getLong("assigned_employee");
		    			if(rs.wasNull()) {
		    				employee = "Unassigned";
		    			} else {
		    				employee = String.valueOf(empId);
		    			}
		    			
		    			status = rs.getString("execution_status");
		    			if(status == null) status = "Pending";
		    			
		    			String[] order = {id, time, price, lastname, firstname, employee, status};
		    			orders.add(order);
					}
					
					pst.close();
					con.close();
					
		            System.out.println("search bar array set");
		            session.setAttribute("orderList", orders);
					session.setAttribute("orderSearch", search);
		            session.setAttribute("changeOrderList", "false");
		            
				}catch(Exception e) {
					System.out.print("ERROR in OrdersServlet doPost: unable to search input text orders from database");
					e.printStackTrace();
				}
	    	}
		}
		
		//Execute Order - Update status to "Completed" and deduct inventory
		else if(btnExecute != null) {
			System.out.println("\n\n====EXECUTING ORDER====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> orders = (ArrayList<String[]>) session.getAttribute("orderList");
			int index = Integer.valueOf(request.getParameter("orderIndex"));
			String[] order = orders.get(index);
			long orderId = Long.parseLong(order[0]);
			
			try {
				System.out.println("Executing order ID: " + orderId);
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				
				//Step 1: Get all items in this order
				PreparedStatement pstItems = con.prepareStatement("SELECT product_id, quantity FROM order_items WHERE order_id = ?");
				pstItems.setLong(1, orderId);
				ResultSet rsItems = pstItems.executeQuery();
				
				boolean canExecute = true;
				ArrayList<long[]> itemsToDeduct = new ArrayList<>(); 
				
				//Step 2: Check if we have enough inventory for all items
				while(rsItems.next()) {
					long productId = rsItems.getLong("product_id");
					long orderQuantity = rsItems.getLong("quantity"); 
					
					//Check current inventory
					PreparedStatement pstCheck = con.prepareStatement("SELECT quantity FROM product WHERE product_id = ?");
					pstCheck.setLong(1, productId);
					ResultSet rsCheck = pstCheck.executeQuery();
					
					if(rsCheck.next()) {
						long currentStock = rsCheck.getLong("quantity");
						if(currentStock < orderQuantity) {
							canExecute = false;
							System.out.println("Insufficient stock for product " + productId + ". Need: " + orderQuantity + ", Have: " + currentStock);
						} else {
							itemsToDeduct.add(new long[]{productId, orderQuantity});
						}
					}
					pstCheck.close();
				}
				pstItems.close();
				
				//Step 3: If we can execute, deduct inventory and update status
				if(canExecute) {
					//Deduct inventory for each item
					for(long[] item : itemsToDeduct) {
						PreparedStatement pstUpdate = con.prepareStatement("UPDATE product SET quantity = quantity - ? WHERE product_id = ?");
						pstUpdate.setLong(1, item[1]);
						pstUpdate.setLong(2, item[0]);
						pstUpdate.executeUpdate();
						pstUpdate.close();
						System.out.println("Deducted " + item[1] + " units from product " + item[0]);
					}
					
					//Update order status to "Completed"
					PreparedStatement pstStatus = con.prepareStatement("UPDATE orders SET execution_status = 'Completed' WHERE order_id = ?");
					pstStatus.setLong(1, orderId); 
					pstStatus.executeUpdate();
					pstStatus.close();
					
					System.out.println("Order " + orderId + " executed successfully");
					session.setAttribute("successMessage", "Order executed successfully! Inventory updated.");
				} else {
					System.out.println("Cannot execute order " + orderId + " - insufficient inventory");
					session.setAttribute("errorMessage", "Cannot execute order - insufficient inventory for some items.");
				}
				
				con.close();
				session.setAttribute("changeOrderList", "true");
				response.sendRedirect("OrdersServlet");
				
			} catch(Exception e) {
				System.out.println("ERROR executing order: " + e.getMessage());
				session.setAttribute("errorMessage", "Error executing order: " + e.getMessage());
				e.printStackTrace();
				response.sendRedirect("OrdersServlet");
			}
		}
		
		//Update Order Status
		else if(btnUpdateStatus != null) {
			System.out.println("\n\n====UPDATING ORDER STATUS====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> orders = (ArrayList<String[]>) session.getAttribute("orderList");
			int index = Integer.valueOf(request.getParameter("orderIndex"));
			String newStatus = request.getParameter("newStatus");
			String[] order = orders.get(index);
			long orderId = Long.parseLong(order[0]);
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_store", "root", "admin");
				
				PreparedStatement pst = con.prepareStatement("UPDATE orders SET execution_status = ? WHERE order_id = ?");
				pst.setString(1, newStatus);
				pst.setLong(2, orderId);
				pst.executeUpdate();
				pst.close();
				con.close();
				
				System.out.println("Updated order " + orderId + " status to: " + newStatus);
				session.setAttribute("changeOrderList", "true");
				response.sendRedirect("OrdersServlet");
				
			} catch(Exception e) {
				System.out.println("ERROR updating order status: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		//Sort Orders
		else if(btnSort != null) {
			System.out.println("\n\n====SORTING ORDERS BY: " + btnSort + "====");
			@SuppressWarnings("unchecked")
			ArrayList<String[]> orders = (ArrayList<String[]>) session.getAttribute("orderList");
			
			if(orders != null) {
				Collections.sort(orders, new Comparator<String[]>() {
					public int compare(String[] o1, String[] o2) {
						switch(btnSort) {
							case "time":
								//Sort by order time (index 1)
								return o1[1].compareTo(o2[1]);
							case "time-desc":
								return o2[1].compareTo(o1[1]);
							case "customer":
								//Sort by customer last name (index 3), then first name (index 4)
								int lastNameComparison = o1[3].compareTo(o2[3]);
								if(lastNameComparison == 0) {
									return o1[4].compareTo(o2[4]);
								}
								return lastNameComparison;
							case "price":
								//Sort by price (index 2)
								return Double.valueOf(o1[2]).compareTo(Double.valueOf(o2[2]));
							case "price-desc":
								return Double.valueOf(o2[2]).compareTo(Double.valueOf(o1[2]));
							case "status":
								//Sort by execution status (index 6)
								return o1[6].compareTo(o2[6]);
							default:
								//Default sort by order_id (index 0) - FIXED: Use Long
								return Long.valueOf(o1[0]).compareTo(Long.valueOf(o2[0]));
						}
					}
				});
				
				session.setAttribute("orderList", orders);
				session.setAttribute("changeOrderList", "false");
				System.out.println("Orders sorted by: " + btnSort);
			}
			
			response.sendRedirect("OrdersServlet");
		}
		
		else {
			System.out.println("No recognized button action in OrdersServlet");
			response.sendRedirect("OrdersServlet");
		}
	}
}