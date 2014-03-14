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
	<title>Create a new Quiz!</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Welcome to the Quiz Creator!</h2>
			<h4>Here, you will have a chance to create a new quiz.</h4>
			<h3><small>Please fill out the form below to begin creating your quiz!</small></h3>
			<form class="form-horizontal" role="form" action="QuizCreateServlet" method="post">
				<div class="form-group">
					<input name="origin" type="hidden" value="CreateQuiz.jsp" >
					<label for="quizName" class="col-sm-2 control-label">Quiz Name:</label>
					<div class="col-md-10">
						<input name="quizName" id="quizName">
					</div>
				</div>	
				<div class="form-group">
  					<div class="col-sm-offset-2 col-md-10">
	      				<div class="checkbox">
				        <label>
				          	<input name="quizParams" value="random" type="checkbox">
							Randomize question order?
				        </label>
				      	</div>
			    	</div>
			  	</div>
				<div class="form-group">
  					<div class="col-sm-offset-2 col-md-10">
	      				<div class="checkbox">
				        <label>
				          	<input name="quizParams" value="one-page" type="checkbox"> 
							Show all questions on one page?
				        </label>
				      	</div>
			    	</div>
			  	</div>
				<div class="form-group">
  					<div class="col-sm-offset-2 col-md-10">
	      				<div class="checkbox">
				        <label>
				          	<input name="quizParams" value="practice-mode" type="checkbox"> 
							Can be taken in practice mode?
				        </label>
				      	</div>
			    	</div>
			  	</div>
			  	<div class="form-group">
  					<div class="col-sm-offset-2 col-md-10">
	      				<div class="checkbox">
				        <label>
				          	<input name="quizParams" value="immediate-correction" type="checkbox"> 
				          	Should answers be displayed immediately after question?
				        </label>
				      	</div>
			    	</div>
			  	</div>
			  	<div class="form-group">
  					<div class="col-sm-offset-2 col-md-10">
  						<input class="btn btn-primary" type="submit" value="Create Quiz">
  					</div>
  				</div>
			</form>
		</div>
	</div>
</body>
</html>