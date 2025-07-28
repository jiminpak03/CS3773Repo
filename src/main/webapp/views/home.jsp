<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<link rel="stylesheet" href="css/home-style.css">
</head>
<body>
	<%
	    String name = (String) session.getAttribute("name");
	    if (name == null) {
	        name = "User";
	    }
	%>
	
	<div class="dashboard-container">
	    <h2>Welcome, <%= name %>!</h2>
	    <p>Select a section below or use the sidebar to navigate the portal.</p>
	
	    <div class="card-grid">
	        <div class="card">
	            <h3>🛒 Products</h3>
	            <p>Browse and manage all store products.</p>
	            <a href="ProductsServlet" class="btn green">Go to Products</a>
	        </div>
	        <div class="card">
	            <h3>📦 Orders</h3>
	            <p>View and process current and past orders.</p>
	            <a href="main.jsp?view=orders" class="btn orange">Go to Orders</a>
	        </div>
	        <div class="card">
	            <h3>👤 Account</h3>
	            <p>View and update your profile information.</p>
	            <a href="main.jsp?view=account" class="btn purple">Go to Account</a>
	        </div>
	    </div>
	</div>
</body>
</html>