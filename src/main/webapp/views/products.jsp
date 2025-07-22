<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product Catalog</title>
<link rel="stylesheet" type="text/css" href="css/products-style.css">	

</head>
<body>
	<h2>PRODUCT CATALOG</h2>
	<%-- Browse --%>
	<div class="type">
	<form class="browse-catalog" method="post" action="ProductsServlet">
			<%-- Browse by Search bar --%>
			<%  
				String searchText = (String) session.getAttribute("search");
				String text = searchText;
				if("".equals(searchText) || searchText==null){
					text = "Search Product";
					searchText = "";
				}
			%>
			<input type="text" name="searchProduct" class="searchProduct" <%= "".equals(searchText) ? "placeholder" : "value"%>="<%=text%>">
	
			<%-- Browse by Sort --%>
			<%  
				String selectedOpt = (String) session.getAttribute("sort");
				if(selectedOpt == null){
					selectedOpt = "default";
				} 
			%>
			<select name="btn-sort" class="sortProduct" onchange="this.form.submit()">
				<option value="default" <%= selectedOpt.equals("default") ? "selected" : ""%>>Sort By...</option>
				<option value="price1" <%= selectedOpt.equals("price1") ? "selected" : ""%>>Price Low to High</option>
				<option value="price2" <%= selectedOpt.equals("price2") ? "selected" : ""%>>Price High to Low</option>
				<option value="available1" <%= selectedOpt.equals("available1") ? "selected" : ""%>>Availability Low to High</option>
				<option value="available2" <%= selectedOpt.equals("available2") ? "selected" : ""%>>Availability High to low</option>
			</select>
		<br><br>
	</form>
	</div>
	
	<%-- Add a new product --%>
	<form class="catalog" method="post" action="ProductsServlet" enctype="multipart/form-data">
		<button class="btn" id="btn-add" type="button" onclick="openPopupAdd()">Add Product</button><br><br>
		<div class="overlay" id="overlay">
			<div class="popup-container" id="popup-container">
				<div class="popup-content">
					<h2>ADD A PRODUCT</h2>
					<%  
						String errorProducts = (String) session.getAttribute("errorProducts");
						if(errorProducts != null){
					%>
						<p style="color:red;"><%= errorProducts %></p>
						
					<%
						session.removeAttribute("errorProducts");
						} 
					%>
					<div class="popup-inputs">
						<div class="left-side">
							<label for="productImage">Image (JPG):</label><br>
							<input type="file" name="productImage" required><br><br>
						</div>
						<div class="middle-side">
							<label for="productName">Product Name:</label>
							<input type="text" name="productName" class="product-input" required><br><br>
							
							<label for="productQuant">Quantity:</label>
							<input type="number" name="productQuant" class="product-input" step="1" min="0" required><br><br>
						
							<label for="productDesc">Description:</label>
							<input type="text" name="productDesc" class="product-input" required>
						</div>
						<div class="right-side">
							<label for="productPrice">Price:</label>
							<input type="number" name="productPrice" class="product-input" step="0.01" min="0" required><br><br>
							
							<label for="productSale">Sale:</label>
							<input type="number" name="productSale" class="product-input" step="1" min="0" max="100"><label>%</label><br><br>
						</div>
					</div>
					<div class="bottom">
						<button class="btn" name="btn-add-confirm" type="submit">Add</button>
						<button class="btn" name="btn-cancel" type="button" onclick="closePopupAdd()">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	
	<%-- List all products --%>
	<div class="list-container" id="list-container">
		<%  
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) session.getAttribute("list");
			if(list != null){ 
				int i = 0;
				for (String[] product: list){
		%>
		<form class="alterProduct" method="post" action="ProductsServlet" enctype="multipart/form-data">
			<div class="list-item">
				<div class="item-info" style="font-family:'Georgia'; color:#343434"> 
					<div class="img-info">
						<img alt="pic" src="ImageServlet?id=<%=product[0]%>" class="img" width="130" height="130">
					</div>
					<div class="name-info" >
						<h4>Item: <%=product[1]%> </h4>
					</div>
					<div class="qty-info">
						<h4>Qty: <%=product[3]%></h4>
					</div>
					<div class="price-info">
						<% 
							String strPrice;
							String strSale;
							String strOgPrice;
							if(product[4] != null && (!product[4].isEmpty()) && Integer.parseInt(product[4]) > 0){
								int sale =  Integer.parseInt(product[4]);
								double ogPrice = Double.valueOf(product[2]);
								double price = (sale/100.0) * ogPrice;
								price = ogPrice - price;
								strPrice = String.format("%.2f", price);
								strOgPrice = "$"+product[2];
								strSale = product[4] + "% OFF";
							}
							else{
								strPrice = product[2];
								strOgPrice = "";
								strSale = "";
							}
						%>
						<h3>$<%=strPrice %></h3>
						<p style="text-decoration:line-through"><%= strOgPrice%></p>
						<p><%=strSale%></p>
					</div>
					<div class="alter-info" id="alter-btns">
						<%-- Edit button --%>
						<input type="hidden" value="<%= i %>" name="index">
						<button class="btn" name="btn-edit" type="button" onclick="openPopupEdit(<%=i%>)">Edit</button>
						<%-- Delete button --%>
						<input type="hidden" value="<%= i %>" name="index">
						<button class="btn" name="btn-delete" type="submit">Delete</button>
					</div>
				</div>
				
				<%-- Edit a product --%>
				<div class="item-btn">
					<div class="overlay" id="overlay<%= i %>">
						<div class="popup-container" id="popup-container<%= i %>">
							<div class="popup-content">
								<h2>EDIT PRODUCT</h2>
								<%  
									if(errorProducts != null){
								%>
									<p style="color:red;"><%= errorProducts %></p>
									
								<% 
									session.removeAttribute("errorProducts");
									} 
								%>
								<div class="popup-inputs">
									<div class="left-side">
										<button type="button" name="editProductImage" id="editProductImage" onclick="viewUploadButton(<%=i%>)" style="height:100%; width:100%; ">Change Image</button>
										<input type="file" name="changeProductImage" id="uploadEditImage<%=i%>" style="display: none;">
									</div>
									<div class="middle-side">
										<label for="productName">Product Name:</label>
										<input type="text" name="productName" class="product-input" value="<%=product[1]%>" required><br><br>
										<label for="productQuant">Quantity:</label>
										<input type="number" name="productQuant" class="product-input" value="<%=product[3]%>" min="0" required><br><br>
										<label for="productDesc">Description:</label>
										<input type="text" name="productDesc" class="product-input" value="<%=product[5]%>" required><br><br>
									</div>
									<div class="right-side">
										<label for="productPrice">Price:</label>
										<input type="number" name="productPrice" class="product-input" step="0.01" min="0" value="<%=product[2]%>" required><br><br>
										
										<label for="productSale">Sale:</label>
										<input type="number" name="productSale" class="product-input" min="0" max="100" value="<%=product[4]%>">
										<label>%</label><br><br>
									</div>
								</div>
								<div class="bottom">
									<button class="btn" name="btn-edit-confirm" type="submit" >Confirm</button>
									<button class="btn" name="btn-cancel" type="button" onclick="closePopupEdit(<%=i%>); closeUploadButton(<%=i%>);" >Cancel</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	<%  
				i++;
			
			}
		}
	%>
	</div>
	
	<%-- Functions --%>
	<script>
		function openPopupAdd(){
			document.getElementById("overlay").style.display = "flex";
		}
		function closePopupAdd(){
			document.getElementById("overlay").style.display = "none";
		}
		function openPopupEdit(i){
			document.getElementById("overlay" + i).style.display = "flex";
		}
		function closePopupEdit(i){
			document.getElementById("overlay" + i).style.display = "none";
		}
		function viewUploadButton(i){
			document.getElementById("uploadEditImage" + i).style.display = "flex";
		}
		function closeUploadButton(i){
			document.getElementById("uploadEditImage" + i).style.display = "none";
		}
	</script>
</body>
</html>