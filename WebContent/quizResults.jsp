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
</head>
<body>
<div class="container">
		<div class="jumbotron">
	 		<br>
			<%
				out.println( "<p>You just finished " + quiz.getQuizName() + "!</p>");
				if (user != null) {
					ArrayList<String> achievements = Achievements.finishedQuiz(user.getUserID(), quiz.getQuizID(), quiz.getScore(), quiz.isPracticeModeOn(), db);
					if (achievements.size() > 0) {
						out.println( "<p></p><p>You have new achievements: ");
						boolean first = true;
						for (String achievement : achievements) {
							if (!first) {
								out.println(", " + achievement);
							} else {
								out.println(achievement);
							}
						}
					}
					if (quiz.isPracticeModeOn()) {
						out.println( "<p>You completed this quiz in practice mode. If you want your quiz recorded, take it for real!</p>" );
					}
					out.println( quiz.getResultsSummary(user.getUserID()));
				} else {
					out.println( "<p>Please log in to have your quiz attempt counted!</p>");
				}
				out.println( "<a href='quizSummary.jsp?quizID=" + quiz.getQuizID() + "'>Quiz Summary Page</a>");
			
			%>
 		</div>
 	</div>
	
</body>
</html>