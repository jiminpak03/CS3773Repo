
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Internal Portal</title>
<link rel="stylesheet" type="text/css" href="css/main-style.css?v=3">	
</head>
<body>

	<%-- Switches to login.jsp display if user is not logged in --%>
	<%
		String username = (String) session.getAttribute("username");
		if (username == null){
			response.sendRedirect("login.jsp");
		}
	%>
	
	<%-- Side-bar navigation menu--%>
	<div class="sidebar">
		<div class="sidebar-store">
			<a href="main.jsp?view=default" style="font-family:'cursive', Pacifico;">Fresh Finds</a>
		</div>
		<div class="sidebar-tabs">
			<a href="main.jsp?view=home">Home</a>
			<a href="ProductsServlet">Product Catalog</a>
			<a href="main.jsp?view=orders">Orders</a>
			<a href="main.jsp?view=account">Account</a>
		</div>
	</div>
	
	<%-- Loads a different page based on the 'view' clicked --%>
	<div class="main">
		<%
			String view = request.getParameter("view");
			if (view == null) view = "default";
			
			switch (view){
				case "home":
		%>
					<jsp:include page="views/home.jsp"/>
		<%
					break;
				case "products":
		%>
					<jsp:include page="views/products.jsp"/>
		<%
					break;
				case "orders":
		%>
					 <jsp:include page="views/orders.jsp"/>
		<%
					break;
				case "account":
		%>
					<jsp:include page="views/account.jsp"/>
		<% 
					break;
				default:
		%>
					<jsp:include page="views/home.jsp"/>
		<% 		
			}
		%>
	</div>
	
</body>
</html>