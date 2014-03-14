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
	<title>Create a new Picture Response Question!</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />	
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Please write your question</h2>
				<h4>Please fill in both the picture link and answer to create a question.</h4>
				<h5 style="color:#d9534f">You will not be able to go back and edit these!</h5>
				<form class="form-horizontal" role="form" action="QuizCreateServlet" method="post">
					<input name="origin" type="hidden" 
					 value="<%= request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1) %>" >
					<div class="form-group">
						<label for="question" class="col-sm-3 control-label">Image link:</label>
						<div class="col-sm-7">
							<input name="question" id="question" class="form-control">
						</div>
					</div>	
					<div class="form-group">
						<label for="response" class="col-sm-3 control-label">Best response:</label>
						<div class="col-sm-7">
							<input name="response" id="response" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-10">
				   			<div class="checkbox">
						        <label>
						          	<input name="otherResponsesCheck" type="checkbox" value="yes">
									Are other responses allowed?
						        </label>
				      		</div>
				    	</div>
				    </div>
				    <div class="form-group">
						<label for="post" class="col-sm-3 control-label">Tilda (~) delimited list of other responses:</label>
						<div class="col-sm-7">
							<input class="form-control" name="otherResponses">
						</div>
					</div>	
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-7">
							<input class="btn btn-primary" type="submit" value="Create question">
						</div>
			  		</div>
				</form>
			</div>
		</div>
	</body>
</html>