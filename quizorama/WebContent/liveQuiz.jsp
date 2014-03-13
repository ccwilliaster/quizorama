<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:navbar session="<%= session %>" activeTab="" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="./css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/bootstrap.js"></script>
<link href="css/bootstrap.css" rel="stylesheet">
<title> 
<% 
	Quiz quiz = (Quiz) session.getAttribute("quiz"); 
	out.println( quiz.getQuizName() );
%> </title>
</head>
<body>
		
		<!-- display Quiz-generated HTML, submit results with 'Next' button -->		
	<div class="container">
		<div class="jumbotron">
	 		<br>
			<form action="QuizControllerServlet" method="post">
			<%= request.getAttribute("html")  %>
			<div><input class="btn btn-default" type="submit" value="Next" /></div>
	 		</form>
 		</div>
 	</div>
 		
</body>
</html>