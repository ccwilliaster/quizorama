<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a new Quiz!</title>
</head>
<body>

<h1>Welcome to the Quiz Creator!</h1>

Here, you will have a chance to create a new quiz. <br>
<br>
Please fill out the form below to begin creating your quiz!
<br><br><br>
<form action="QuizCreateServlet" method="post">
	<input name="origin" type="hidden" value="CreateQuiz.jsp" >
	Quiz Name: <input name="quizName"> <br><br>
	<input name="random" type="checkbox"> Randomize question order? <br><br>
	<input name="one-page" type="checkbox"> Show all questions on one page? <br><br>
	<input name="practice-mode" type="checkbox"> Can be taken in practice mode? <br><br>
	<input name="immediate-correction" type="checkbox"> Should this quiz be corrected immediately? <br><br>
	<input class="btn btn-default" type="submit" value="Create Quiz">
</form>
</body>
</html>