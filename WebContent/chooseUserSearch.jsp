<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8" %>
 <%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:navbar session="<%= session %>" activeTab="quizzes" /> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"><link href="./css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/bootstrap.js"></script>
<title>Make a friend!</title>
</head>
<body>
<div class="container">
<h1>Choose a user to befriend:</h1>
<p></p>
<p>Users: </p>
<p></p>
<ul>
<%@ page import="quiz.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="java.sql.*" %> 
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
		
		for (Integer userId : userMap.keySet()) {
			out.println("<li>");
			out.println("<input name='quizId' type='hidden' value='" + userId + "'/>");
			out.println("<a href='createMessage.jsp?type=" + Message.TYPE_FRIEND + "&toUserName=" + userMap.get(userId) + "' >" + userMap.get(userId) + "</a>");
			out.println("</li>");
		}

%>
</ul>
</div>
</body>
</html>