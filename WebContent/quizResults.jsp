<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% Quiz quiz = (Quiz) request.getAttribute("quiz"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= quiz.getName() + " results" %></title>
</head>
<body>
	<% 
		out.println( "You just finished " + quiz.getName() + "!!!");
	%>
</body>
</html>