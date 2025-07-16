<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="css/login-style.css">	
</head>
<body>
				
 	<div class="container">
 	
 		<%-- Displays error message if login info is incorrect --%>
	 	<%  
			String errorLogin = (String) session.getAttribute("errorLogin");
			if(errorLogin != null){
		%>
			<p style="color:red;"><%= errorLogin %></p>
			
		<%
			session.removeAttribute("errorLogin");
			} 
		%>
		
		<%-- Login Input Form --%>
	    <form class="form-login" method="post" action="LoginServlet">
	    	<h2 class="mb-3">LOGIN</h2>
	    	<div class="mb-3">
		    	<label for="username" class="form-label">Username:</label>
		    	<input type="text" id="username" name="username" class="form-input" required>
	    	</div>
	    	<div class="mb-3">
		    	<label for="password" class="form-label">Password:</label>
		    	<input type="text" id="password" name="password" class="form-input" required>
	    	</div>
	    	<button class="btn-login" type="submit">Login</button>
	    </form>
    </div>
    
</body>
</html>