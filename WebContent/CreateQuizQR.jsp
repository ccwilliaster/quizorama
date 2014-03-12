<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a new QuestionResponse Question!</title>
</head>
<body>

<h1>Please write your question</h1>
<br>
Please fill in both the question and answer to create a question. You will not be able to go back and edit these.<br>
<form action="QuizCreateServlet" method="post">
	<input name="origin" type="hidden" value="<%= request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1) %>" >
	Question: <input name="question"><br>
	Response: <input name="response"><br>
	Other responses allowed? <input name="otherResponsesCheck" type="checkbox" value="yes"><br>
	Pipe (|) delimited list of other responses: <input name="otherResponses"><br>
	<input class="btn btn-default" type="submit" value="Create Question"><br>
</form>
</body>
</html>