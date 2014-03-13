<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Create a question...</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />
	<div class="container">
		<div class="jumbotron">
			<h3 style="color:#428bca">What type of question would you like to add?</h3>
			<form role="form" action="QuizCreateServlet" method="post">
				<div class="form-group">
					<div class="col-md-10">
						<input name="origin" type="hidden" value="QuizCreateServlet" >
						<select class="form-control" name="questionType">
							<%= (String) request.getAttribute("options") %>			
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-1">
						<button class="btn btn-primary" type="submit">Go!</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>