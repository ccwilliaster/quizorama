<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:navbar session="<%= session %>" activeTab="" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% Quiz quiz = (Quiz) request.getAttribute("quiz");
	User user = (User) session.getAttribute("user"); 
	DBConnection db = (DBConnection) application.getAttribute("DBConnection"); %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<title><%= quiz.getQuizName() + " results" %></title>
	<style>
		.label { margin: 2px 2px; }
	</style>
</head>
<body>
<div class="container">
		<div class="jumbotron">
	 		<br>
			<%
				out.println("<div class='row'><div class='col-md-8 col-md-offset-2'>");
				out.println( "<h2 style='color:#428bca'>You just finished " + quiz.getQuizName() + "!</h3>");
				if (user != null) {
					ArrayList<String> achievements = 
						Achievements.finishedQuiz(user.getUserID(), quiz.getQuizID(), quiz.getScore(), quiz.isPracticeModeOn(), db);
					
					if (achievements.size() > 0) {
						out.println( "<h3 style='color:#5cb85c'>Congratulations! You have new achievements: </h3>");
						
						for (String achievement : achievements) {
							out.println("<span class='label label-success'>" + achievement + "</span>");
						}
					}
					if (quiz.isPracticeModeOn()) {
						out.println( "<h3 style='color:#f0ad4e'>You completed this quiz in practice mode. If you want your quiz recorded, take it for real!</h3>" );
					}
					out.println( quiz.getResultsSummary(user.getUserID()));
				} else {
					out.println( "<h3 style='color:#d9534f'>Please log in to have your quiz attempt counted!</h3>");
				}
				out.println( "<a class='btn btn-primary' href='quizSummary.jsp?quizID=" + 
						      quiz.getQuizID() + "'>Quiz Summary Page</a>");
				out.println("</div></div>");
				out.println("<div class='row'><div class='col-md-8 col-md-offset-2'>");
				out.println( "<h2 style='color:#428bca'>Here are the quiz answers: </h2>");
				
				for (Question q : quiz.getAllQuestions()) {
					out.println(q.showQuestionText());
					out.println(q.showAnswerOptions());
				}
				out.println("</div></div>");
			%>
 		</div>
 	</div>
	
</body>
</html>