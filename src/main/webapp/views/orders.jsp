<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Management</title>
<link rel="stylesheet" href="css/orders-style.css">
</head>
<body>
<h1>Order Management</h1>

<%-- Display success/error messages --%>
<%
String successMessage = (String) session.getAttribute("successMessage");
String errorMessage = (String) session.getAttribute("errorMessage");
if(successMessage != null) {
    session.removeAttribute("successMessage");
%>
    <div class="message success-message"><%=successMessage%></div>
<%
}
if(errorMessage != null) {
    session.removeAttribute("errorMessage");
%>
    <div class="message error-message"><%=errorMessage%></div>
<%
}
%>

<%-- Search and Sort Controls --%>
<div class="controls">
    <form class="search-form" method="post" action="OrdersServlet">
        <%
        String searchText = (String) session.getAttribute("orderSearch");
        String text = searchText;
        if("".equals(searchText) || searchText==null){
            text = "Search Orders (ID, Customer, Status, etc.)";
            searchText = "";
        }
        %>
        <input type="text" name="searchOrder" class="searchOrder" <%= "".equals(searchText) ? "placeholder" : "value"%>="<%=text%>">
        <button type="submit" class="btn search-btn">Search</button>
    </form>
    
    <form class="sort-form" method="post" action="OrdersServlet">
        <label for="sort-select">Sort by:</label>
        <select name="btn-sort" id="sort-select" onchange="this.form.submit()">
            <option value="default">Order ID</option>
            <option value="time">Order Time (Oldest First)</option>
            <option value="time-desc">Order Time (Newest First)</option>
            <option value="customer">Customer Name</option>
            <option value="price">Price (Low to High)</option>
            <option value="price-desc">Price (High to Low)</option>
            <option value="status">Status</option>
        </select>
    </form>
</div>

<%-- Order List --%>
<div class="list-container" id="list-container">
<%
@SuppressWarnings("unchecked")
ArrayList<String[]> list = (ArrayList<String[]>) session.getAttribute("orderList");
if(list != null && list.size() > 0){
    int i = 0;
    for (String[] order: list){
        //order array: [id, time, price, lastname, firstname, employee, status]
%>
        <div class="list-item">
            <div class="item-info">
                <div class="order-id-info">
                    <h4>Order #<%=order[0]%></h4>
                </div>
                
                <div class="customer-info">
                    <h4>Customer: <%=order[4]%> <%=order[3]%></h4>
                    <p>Time: <%=order[1]%></p>
                </div>
                
                <div class="price-info">
                    <h3>$<%=order[2]%></h3>
                </div>
                
                <div class="status-info">
                    <h4 class="status-<%=order[6].toLowerCase().replace(" ", "-")%>">
                        Status: <%=order[6]%>
                    </h4>
                    <p>Employee: <%=order[5]%></p>
                </div>
                
                <div class="action-info">
                    <%-- Execute Order Button (only show if not completed) --%>
                    <% if(!"Completed".equalsIgnoreCase(order[6])) { %>
                    <form method="post" action="OrdersServlet" style="display:inline;">
                        <input type="hidden" name="orderIndex" value="<%=i%>">
                        <button class="btn execute-btn" name="btn-execute" type="submit" 
                                onclick="return confirm('Execute this order? This will deduct inventory and mark as completed.')">
                            Execute Order
                        </button>
                    </form>
                    <% } %>
                    
                    <%-- Update Status Button --%>
                    <button class="btn status-btn" onclick="openStatusPopup(<%=i%>, '<%=order[6]%>')">
                        Update Status
                    </button>
                    
                    <%-- View Details Button --%>
                    <form method="post" action="OrderDetailsServlet" style="display:inline;">
                        <input type="hidden" name="orderId" value="<%=order[0]%>">
                        <button class="btn details-btn" type="submit">View Details</button>
                    </form>
                </div>
            </div>
        </div>
<%
        i++;
    }
} else {
%>
    <div class="no-orders">
        <h3>No orders found</h3>
        <p>There are currently no orders in the system.</p>
    </div>
<%
}
%>
</div>

<%-- Status Update Popup --%>
<div class="overlay" id="statusOverlay">
    <div class="popup-container">
        <div class="popup-content">
            <h2>Update Order Status</h2>
            <form method="post" action="OrdersServlet" id="statusForm">
                <input type="hidden" name="orderIndex" id="statusOrderIndex">
                <div class="popup-inputs">
                    <div class="status-options">
                        <label><input type="radio" name="newStatus" value="Pending"> Pending</label><br>
                        <label><input type="radio" name="newStatus" value="Processing"> Processing</label><br>
                        <label><input type="radio" name="newStatus" value="Completed"> Completed</label><br>
                        <label><input type="radio" name="newStatus" value="Cancelled"> Cancelled</label><br>
                    </div>
                </div>
                <div class="popup-buttons">
                    <button type="submit" name="btn-update-status" class="btn confirm-btn">Update Status</button>
                    <button type="button" class="btn cancel-btn" onclick="closeStatusPopup()">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function openStatusPopup(orderIndex, currentStatus) {
    document.getElementById('statusOrderIndex').value = orderIndex;
    
    //Select the current status radio button
    const radioButtons = document.querySelectorAll('input[name="newStatus"]');
    radioButtons.forEach(radio => {
        if(radio.value === currentStatus) {
            radio.checked = true;
        }
    });
    
    document.getElementById('statusOverlay').style.display = 'flex';
}

function closeStatusPopup() {
    document.getElementById('statusOverlay').style.display = 'none';
}

//Close popup when clicking outside
document.getElementById('statusOverlay').addEventListener('click', function(e) {
    if(e.target === this) {
        closeStatusPopup();
    }
});
</script>

</body>
</html>