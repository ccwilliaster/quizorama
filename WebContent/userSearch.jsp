<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*, java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search for a quiz here</title>
</head>
<body>

<form action="chooseUserSearch.jsp" method="get">
	User Name Filter: <input name="userNameFilter"> <br><br>
	<input class="btn btn-default" type="submit" value="Create Quiz"> <br><br>
</form>
</body>
</html>