<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
	<%
		String employeeName = (String) session.getAttribute("name");
	%>
	
	<h2>HOME PAGE</h2>
	<h3>Welcome back, <%=employeeName %></h3>
</body>
</html>