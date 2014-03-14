<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*, java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Search quizzes</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Search for available quizes here!</h2>
			<h2><small>You can search by name as well as by quiz categories and tags</small></h2>
			<div class="row">
				<div class="col-md-10">
					<div class="thumbnail">
						<br><br>
						<form class="form-horizontal" role="form" action="chooseQuizSearch.jsp" method="get">
							<div class="form-group">
								<label for="quizFilter" class="col-md-3 control-label">Quiz name filter:</label>
								<div class="col-md-4">
									<input name="quizFilter" id="quizFilter" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label for="catFilter" class="col-md-3 control-label">Optional category filter:</label>
								<div class="col-md-4">
									<select class="form-control" name="catFilter" id="catFilter">
										<option value="-1" selected>Any Category</option>
									<% //Get the DBConnection to give a resultSet of the tagTypes to populate options
									   DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
									   ResultSet rs = dbConnection.getTagTypes();
									   rs.beforeFirst();
									   while(rs.next()) {
									   	out.println("<option value=\"" + rs.getInt("tagID") + "\">" + rs.getString("tagName") + "</option>");
									   } //while
									%>
									</select>
								</div>
							</div>	
							<div class="form-group">
								<label for="tagFilter" class="col-md-3 control-label">Optional tag filter:</label>
								<div class="col-md-4">
									<select class="form-control" name="tagFilter" id="tagFilter">
										<option value="-1" selected>Any Tag</option>
									<% //Get the DBConnection to give a resultSet of the tagTypes to populate options
									   rs = dbConnection.getTagTypes();
									   rs.beforeFirst();
									   while(rs.next()) {
									   	out.println("<option value=\"" + rs.getInt("tagID") + "\">" + rs.getString("tagName") + "</option>");
									   } //while
									%>
									</select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-offset-3 col-md-3">
									<input class="btn btn-primary" type="submit" value="Search Quizzes">
								</div>
							</div>
						</form>
						<br><br>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>