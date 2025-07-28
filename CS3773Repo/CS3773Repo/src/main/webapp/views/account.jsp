<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Account</title>
<link rel="stylesheet" href="css/account-style.css">
</head>
<body>
	<%-- gets user information --%>
	<%
		String employeeId = (String) session.getAttribute("id").toString();
		String employeeName = (String) session.getAttribute("name");
		String username = (String) session.getAttribute("username");
		String employeeEmail = (String) session.getAttribute("email");
	%>
	
	<h1>My Account</h1>
	<div class="body">
		<div class="user">
			<div class="user-info">
				<img src="images/user-icon.jpg" class="user-icon">
				<div class="user-contact">
					<h4>Employee ID: <%=employeeId %></h4>
					<h4>Employee name: <%=employeeName %></h4>
					<h4>Username: <%=username %></h4>
					<h4>Email: <%=employeeEmail %></h4><br><br>
				</div>
			</div>
			<form class="btn-logout" method="post" action="LogoutServlet">
				<button class="btn light" type="submit">Logout</button>
			</form>
		</div>
	</div>
	
</body>
</html>