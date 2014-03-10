<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="quiz.Message"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	// Figure out the message type
	String messageType;
	Integer messageCode = Integer.parseInt( request.getParameter("type") );
	if ( messageCode.equals(Message.TYPE_NOTE) ) {
		messageType = "note";
	} else if ( messageCode.equals(Message.TYPE_FRIEND) ) {
		messageType = "friend request";
	} else if ( messageCode.equals(Message.TYPE_CHALLENGE) ) {
		messageType = "quiz challenge";
	} else if ( messageCode.equals(Message.TYPE_ANNOUNCEMENT) ) {
		messageType = "announcement";
	} else {
		messageType = "message";
	}
	
	// See if there was any sort of error
	String error = (String) request.getAttribute("error");
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="./css/bootstrap.css" rel="stylesheet">
	<title>New <%= messageType %></title>
</head>
<body>
	
		<div class="container">
			<div class="jumbotron">
				<h2>New <%= messageType %></h2>
				<% if (error != null) out.println(error); %>
				
				<form action="NewMessageServlet" method="post">
					<%= request.getAttribute("html")  %>
					<div class="row">
						<br>
						<input class="btn btn-default" type="submit" value="Submit">
					</div>
		 		</form>
		 	</div>
		</div>
</body>
</html>