<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="../css/bootstrap.css" rel="stylesheet">
	<title>Test Read Message</title>
</head>
<body>
	<div class="container">
	  <br>

	<%
		// Create test messages and display them
		Note testNote = new Note(0, 1, "Really great note!", "Interesting note content!");
		FriendRequest testFriend = new FriendRequest(0, 1);
		Challenge testChallenge  = new Challenge(0, 1, 100);
		Announcement testAnnouncement = new Announcement(0, 1, "Important admin announcement");
	
		out.println("<h5>Note:</h5><br>");
		out.println( testNote.displayAsHTML() );
		
		out.println("<h5>Friend request:</h5><br>");
		out.println( testFriend.displayAsHTML() );
		
		out.println("<h5>Quiz Challenge:</h5><br>");
		out.println( testChallenge.displayAsHTML() );
		
		out.println("<h5>Announcement:</h5><br>");
		out.println( testAnnouncement.displayAsHTML() );
		
	%>
	  <br>
	</div>
</body>
</html>