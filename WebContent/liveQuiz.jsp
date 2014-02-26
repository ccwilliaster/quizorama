<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizController.Quiz" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.css" rel="stylesheet">
<title> 
<% 
	Quiz quiz = (Quiz) session.getAttribute("quiz"); 
	out.println( quiz.getName() );
%> </title>
</head>
<body>
		
		<!-- display Quiz-generated HTML, submit results with 'Next' button -->		
		<form action="QuizControllerServlet" method="post">
		<%= session.getAttribute("html")  %>
		<input class="btn" type="submit" value="Next">
 		</form>
 		
</body>
</html>