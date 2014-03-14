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
	<title>Add a category to a Quiz</title>
</head>
<%
	DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
	int quizID = Integer.parseInt(request.getParameter("quizID"));

%>

<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />
	<div class="container">
		<div class="jumbotron">
			<% 
			if ( (String) request.getAttribute("alert") != null) {
				out.println("<h2 style='color:#d9534f'>" + 
						     (String) request.getAttribute("alert") + "</h2><br>");
			}
			%>
			<h2 style="color:#428bca">Add a quiz to a category here!</h2>
			<h2><small>Select the category to which to add this quiz. Please contact an administrator if you'd like to add a category.</small></h2>
			<div class="row">
				<div class="col-md-10">
					<div class="thumbnail">
						<br><br>
						<form class="form-horizontal" role="form" action="AddCatServlet" method="post">
							<div class="form-group">
								<label for="catAdd" class="col-md-3 control-label">Add this category:</label>
								<div class="col-md-4">
									<select class="form-control" name="catAdd" id="catAdd">
										<option value="-1" selected>Select A Category</option>
									<% //Get the DBConnection to give a resultSet of the tagTypes to populate options
									   ResultSet rs = dbConnection.getTagTypes();
									   rs.beforeFirst();
									   while(rs.next()) {
									   	out.println("<option value=\"" + rs.getInt("categoryID") + "\">" + rs.getString("categoryName") + "</option>");
									   } //while
									%>
									</select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-offset-3 col-md-3">
									<input class="btn btn-primary" type="submit" value="Add to Category">
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