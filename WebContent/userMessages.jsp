<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="quiz.*,java.util.*,java.sql.*" %>
<% 
	ArrayList<Message> messages;
	DBConnection connection;
	String title = "Guest messages";
	
	//Get DBConnection, user, messages to display, and mailbox type
	connection        = (DBConnection) application.getAttribute("DBConnection");
	User user         = (User) session.getAttribute("user");
	messages          = (ArrayList<Message>) request.getAttribute("messages");
	String mailType   = (String) request.getAttribute("navtab"); // inbox vs sent
	String msgUpdate  = (String) request.getAttribute("messageUpdate"); // if new message just made	
	
	// Error checking
	if (messages == null) { messages = new ArrayList<Message>(); }
	if (user.getUserID() == -1) { 
		msgUpdate = "Login or make an account to view messages."; 
	} else {
		title = user.getUserName() + "'s messages";
	}

%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script>
		jQuery(document).ready(function($) { 
			$('#backButton').hide(); // no back button to start
			
			$(".clickableRow").hover(function() { 
				$(this).css('cursor', 'pointer');
			}); // display hand on row mousevoer
			
			$(".clickableRow").click(function() { 
				var idx     = $(this).attr("id");
				var msgHTML = $(this).find('#' + idx).val();
				
				$('#messageTable').hide();
				$('#messageHTML').html( msgHTML );
				$('#backButton').show();
	      	}); // Toggle messageTable off, messageHTML on
			
			$("#backButton").click(function() {
				$('#backButton').hide();
				$('#messageTable').show();
				$('#messageHTML').html( "" );
	      	}); // Toggle messageTable on, messageHTML off
	});
	</script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%= title %></title>
</head>
<body>
	<!-- Display inbox vs sent based on navtab attr -->
	<div class="container">
		<% 
		out.println("<h2>" + title + "</h2>");
		if ( !(msgUpdate == null) ) { // if message update happened, notify user
			out.println("<h5><span style='color:gray;'>" + msgUpdate + "</span></h5>");
		} 
		out.println("</h2>");
		%>
		<ul class="nav nav-tabs">
			<%
			if ( "inbox".equals(mailType) || mailType == null ) {
				out.println("<li class=\"active\"><a class=\"disabled\">Inbox</a></li>");
				out.println("<li><a href=\"ReadMessagesServlet?navtab=sent\">Sent</a></li>");
				
			} else {
				out.println("<li><a href=\"ReadMessagesServlet?navtab=inbox\">Inbox</a></li>");
				out.println("<li class=\"active\"><a class=\"disabled\">Sent</a></li>");
			}
			%>	
			<li class="dropdown">
   				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
   					New message<span class="caret"></span>
   				</a>
			    <ul class="dropdown-menu">
			    	<li>
			    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_NOTE + "\">" %>
			    		Note</a>
			    	</li>
			    	<li>
			    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_FRIEND + "\">" %>
			    		FriendRequest</a>
			    	</li>
			    	<li>
			    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_CHALLENGE + "\">" %>
			    		Quiz challenge</a>
			    	</li>
			    	<% 
			    		if (true /*user.isAdmin() */) {
			    			out.println(
			    			"<li role=\"presentation\" class=\"divider\"></li>" + 
			    			"<li><a href=\"NewMessageServlet?type=" + Message.TYPE_ANNOUNCEMENT +
			    			"\">Announcement</a></li>" 
			    			);
			    		}
			    	%>
			    </ul>
			</li>	
		</ul>
		<div id="messageHTML"></div>
		<div id="backButton"><a class="btn btn-primary">Back</a></div>
		<table class="table table-hover" id="messageTable">
			<thead><tr>
				<th width="15%">From</th>
				<th width="15%">To</th>
				<th width="50%">Preview</th>
				<th width="20%">Date</th>
			</tr></thead>
			<tbody>
			<%
			for (int idx=0; idx < messages.size(); idx++) { 
				out.println( messages.get(idx).displayAsTableRow(connection, idx) );
			}
			%>
			</tbody>	
		</table>
	</div>
</body>
</html>