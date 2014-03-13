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

<form action="chooseQuizSearch.jsp" method="get">
	Quiz Name Filter: <input name="quizFilter"> <br><br>
	Select a tag, if applicable: 
	<select name="tagFilter">
		<option value="-1" selected>Any Tag</option>
		<% //Get the DBConnection to give a resultSet of the tagTypes to populate options
		DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
		ResultSet rs = dbConnection.getTagTypes();
		rs.beforeFirst();
		while(rs.next()) {
			out.println("<option value=\"" + rs.getInt("tagID") + "\">" + rs.getString("tagName") + "</option>");
		} //while
		%>	
	</select>
	<br><br>
	Select a category, if applicable:
	<select name="catFilter">
		<option value="-1" selected>Any Category</option>
		<% //Get the DBConnection to give a resultSet of the tagTypes to populate options
		rs = dbConnection.getCategoryTypes();
		rs.beforeFirst();
		while(rs.next()) {
			out.println("<option value=\"" + rs.getInt("categoryID") + "\">" + rs.getString("categoryName") + "</option>");
		} //while
		%>
	</select> <br><br>
	<input class="btn btn-default" type="submit" value="Create Quiz"> <br><br>
</form>
</body>
</html>