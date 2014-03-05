<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="quiz.*,java.util.*,java.sql.*" %>
<% 
	// Get message to display
	User user = (User) session.getAttribute("user");
	DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
	int userID = user.getUserID();
	List<Message> messages = new ArrayList<Message>();

	ResultSet rs = dbConnection.getUserMessages(userID);
	messages = Message.loadMessages(rs,null,null,null);
%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title> All of <%= user.getUserName() %>'s messages </title>
</head>
<body>
	<%
		for (Message message: messages) {
			out.println("<div class=\"container\">");
			out.println("<br>");
			out.println(message.displayAsHTML());
			out.println("<br>");
			out.println("</div>");
		}	
	%>
</body>
</html>