<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="../css/bootstrap.css" rel="stylesheet">
	<title>Test QCServlet</title>
</head>
<body>
	<form action="../QuizControllerServlet" method="post">
		<input name="quizID" type="hidden" value="0" />
		<input class="btn btn-default" type="submit" value="TestQuizControllerServlet">
	</form>
</body>
</html>