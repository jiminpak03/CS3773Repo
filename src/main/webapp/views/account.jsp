<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Account</title>
</head>
<body>
	<%-- gets user information --%>
	<%
		String employeeId = (String) session.getAttribute("id").toString();
		String employeeName = (String) session.getAttribute("name");
		String username = (String) session.getAttribute("username");
		String employeeEmail = (String) session.getAttribute("email");
	%>
	
	<h2>ACCOUNT PAGE</h2>
	
	<div class="user-info">
		<h4>Employee ID: <%=employeeId %></h4>
		<h4>Employee name: <%=employeeName %></h4>
		<h4>Username: <%=username %></h4>
		<h4>Email: <%=employeeEmail %></h4>
	</div>
	
	<form class="btn-logout" method="post" action="LogoutServlet">
		<button type="submit">Logout</button>
	</form>
	
</body>
</html>