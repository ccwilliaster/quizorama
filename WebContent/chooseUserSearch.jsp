<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="quiz.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="java.sql.*" %> 
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User search results</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="users" />
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Follow links below to view a user's page</h2>
			<h2><small>Search results:</small></h2>
			<div class="row">
				<div class="col-md-8">
					<div class="thumbnail">
						<br><br>
<% 
		DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
		String userFilter = request.getParameter("userNameFilter");
		ResultSet users = dbConnection.searchForUser(userFilter);
		Map<Integer, String> userMap = new HashMap<Integer, String>();
		try {
			users.beforeFirst();
			while (users.next()) {
				String userName = users.getString("userName");
				int userId = users.getInt("userID");
				userMap.put(userId, userName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (userMap.size() > 0) {
			for (Integer userId : userMap.keySet()) {
				out.println("<div class='row text-center'><div class='col-md-8'>");
				out.println("<a class='btn btn-danger btn-sm' href='userpage.jsp?userID=" + userId + "'>");
				out.println(userMap.get(userId) + "</a></div></div>");
			}
		} else {
			out.println("<div class='text-center' style='font-size:large'>");
			out.println("<em>Sorry! No users were found for the query '");
			out.println("<span style='color:#d9534f'>" + userFilter + "</span>'</em>");
			out.println("<br>You can try another search <a class='btn btn-primary btn-xs' ");
			out.println("href='userSearch.jsp'>here</a>");
		}
		out.println("<br><br>");
%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>