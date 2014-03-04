<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="quiz.*" %>
<% 
	// Get message to display
	Message message = (Message) request.getAttribute("message");
	// DBConnection connection = application.getAttribute("DBConnection");
	// message.setMessageRead(connection);	
%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title> <%= message.getSubject() %> </title>
</head>
<body>
	<div class="container">
	  <br>
	    <%= message.displayAsHTML() %>
	  <br>
	</div>
</body>
</html>