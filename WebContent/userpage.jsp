<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="quiz.*,java.util.*,java.sql.*" %>
<% 
	String activeTab = "users";
	DBConnection connection = (DBConnection) application.getAttribute("DBConnection");
	ArrayList<String> recentScores = null;
	ArrayList<String> recentQuizzesByUser = null;
	ArrayList<String> popQuizzes = null;
	ArrayList<String> recentQuizzes = null;
	ArrayList<Message> recentMessages = null;
	ArrayList<Integer> friends = null;
	User user = (User) session.getAttribute("user");
	String ID = request.getParameter("userID");
	String title = "Error";
	boolean userOwnPage = false;
	boolean userIDError = true;
	int userPageID = -1;
	if (ID != null) {
		userIDError = false;
		userPageID = Integer.parseInt(ID);
		String test = "";
		recentScores = QuizHistory.getRecentScores(userPageID, null, connection);
		recentQuizzesByUser = QuizHistory.getRecentQuizCreations(userPageID, connection);
		String userPageName = connection.getUserName(userPageID);
		title = userPageName + "'s Page";
		if (user != null) {
			int userID = user.getUserID();
			userOwnPage = userPageID == userID;
			if (userOwnPage) {
				popQuizzes = QuizHistory.getPopularQuizzes(connection);
				recentQuizzes = QuizHistory.getRecentQuizCreations(null, connection);
				recentMessages = connection.getRecentMessages(userID);
				friends = connection.getFriends(userID);
				title = "Hello " + userPageName;
				activeTab = "home";
			}
		}
	}

%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%= title %></title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="<%= activeTab %>" />
	<div class="container">
		<div class="jumbotron">
<% if (userIDError) { %>
	<h1>This Page Should be Called with a userID parameter specified!</h1>
<% } else if (!userOwnPage) { %>
<div class="row">
	<div class="col-md-8">
		<h1 style="color:#428bca"><%= connection.getUserName(userPageID) %>
		<% if (user == null) { %>
			<a class="btn btn-danger" href="homepage.jsp">Log in for more info</a>
		<% } else if (connection.usersAreFriends(user.getUserID(), userPageID)) { %>
			<%= FriendRequestServlet.getRemoveFriendLink("Remove Friend", user.getUserID(), userPageID) %>
		<% } else { %>
			<%= FriendRequestServlet.getMakeFriendRequestLink("Add Friend") %>
		<% } %>
		</h1>
	</div>
</div>
	<div class="row">
		<div class="col-md-4">
 				<div class="thumbnail">
    				<div class="caption">
						<h3 style="color:#428bca">Recent scores</h3>
						<table>
							<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
							<% for(int i = 0; i < recentScores.size(); i++) { %>
								<tr><%= recentScores.get(i) %></tr>
							<% } %>
						</table>
					</div>
			
				</div>
			</div>
		<div class="col-md-4">
 			<div class="thumbnail">
    			<div class="caption">
					<h3 style="color:#428bca">Recent Quizzes Created By <%= connection.getUserName(userPageID) %></h3>
					<table>
						<tr><th>Quiz</th><th>User</th></tr>
						<% for(int i = 0; i < recentQuizzesByUser.size(); i++) { %>
							<tr><%= recentQuizzesByUser.get(i) %></tr>
						<% } %>
					</table>
				</div>
			</div>
		</div>	
		<div class="col-md-4">
 			<div class="thumbnail">
    			<div class="caption">
					<h3 style="color:#428bca">Achievements</h3>
					<ul class="list-group">
						<% ArrayList<String> names = Achievements.getAchievementNames(userPageID, connection); %>
						<% for (int i = 0; i < names.size(); i++) { %>
							<strong>
								<li class="list-group-item" style="color:#d9534f"><%=names.get(i) %></li>
							</strong>
						<% } %>
					</ul>
				</div>
			</div>
		</div>
	</div>
<% } else { %>
	<h1 style="color:#428bca">Welcome Back, <%= user.getUserName() %>!</h1><br>
		<div class="row">
			<div class="col-md-4">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#428bca">Recent messages</h3>
						<ul class="list-group">	
						<% 	for (Message message: recentMessages) {
							out.println(message.displayCompact(connection));
						}	%>
						</ul>
						<a class='btn btn-primary btn-sm' href="userMessages.jsp">See All Messages</a>
					</div>
				</div>
			</div>
			<div class="col-md-4">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#d9534f">Recent announcements</h3>
						<% Set<Integer> announcementType = new HashSet<Integer>();
							announcementType.add(Message.TYPE_ANNOUNCEMENT);
							ResultSet allUserMessages = connection.getUserMessages(userPageID);
							ArrayList<Message> usersAnnouncements = Message.loadMessages( allUserMessages, announcementType, userPageID, null ); %>
						<% 	for (Message message: usersAnnouncements) {
							out.println(message.displayAsAlert(connection));
						}	%>
					</div>
				</div>
			</div>
			<div class="col-md-4">
	 			<div class="thumbnail">
	    			<div class="caption">
						<h3 style="color:#428bca">Achievements earned</h3>
						<ul class="list-group">
							<% ArrayList<String> names = Achievements.getAchievementNames(userPageID, connection); %>
							<% for (int i = 0; i < names.size(); i++) { %>
								<strong>
									<li class="list-group-item" style="color:#d9534f"><%=names.get(i) %></li>
								</strong>
							<% } %>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#428bca">Popular quizzes</h3>
						<table>
							<tr><th>Quiz</th><th>Quiz Creator</th></tr>
							<% for(int i = 0; i < popQuizzes.size(); i++) { %>
								<tr><%= popQuizzes.get(i) %></tr>
							<% } %>
						</table>
					</div>
				</div>
			</div>
			<div class="col-md-3">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#428bca">Recently created quizzes</h3>
						<table>
							<tr><th>Quiz</th><th>User</th></tr>
							<% for(int i = 0; i < recentQuizzes.size(); i++) { %>
								<tr><%= recentQuizzes.get(i) %></tr>
							<% } %>
						</table>
					</div>
				</div>
			</div>	
			<div class="col-md-3">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#428bca">Quizzes<em><span style="color:#d9534f"> You</em></span> recently created</h3>
						<table>
							<tr><th>Quiz</th><th>User</th></tr>
							<% for(int i = 0; i < recentQuizzesByUser.size(); i++) { %>
								<tr><%= recentQuizzesByUser.get(i) %></tr>
							<% } %>
						</table>
					</div>
				</div>
			</div>	
			<div class="col-md-3">
   				<div class="thumbnail">
      				<div class="caption">
						<h3 style="color:#428bca">Recent Scores</h3>
						<table>
							<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
							<% for(int i = 0; i < recentScores.size(); i++) { %>
								<tr><%= recentScores.get(i) %></tr>
							<% } %>
						</table>
					</div>
				</div>
			</div>	
		</div>
		
<!-- FRIENDS -->
	<h2 style="color:#d9534f">Friend Activity</h2>
		<div class="panel panel-default">
			<div class="panel-body">
				<% for(int i = 0; i < friends.size(); i++) { %>
					<div class="row">
						<a class="btn btn-primary" 
						   href="userpage.jsp?userID=<%=friends.get(i)%>">
						   <%=connection.getUserName(friends.get(i)) %>
						</a>
						<%=FriendRequestServlet.getRemoveFriendLink("Remove Friend", userPageID, friends.get(i)) %>
						<div class="col-md-4">
			   				<div class="thumbnail">
			      				<div class="caption">
									<h3 style="color:#428bca">Their recent scores</h3>
									<table>
										<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
										<% ArrayList<String> friendRecentScores = QuizHistory.getRecentScores(friends.get(i), null, connection); %>
										<% for(int j = 0; j < friendRecentScores.size(); j++) { %>
											<tr><%= friendRecentScores.get(j) %></tr>
										<% } %>
									</table>
								</div>
							</div>
						</div>
						<div class="col-md-4">
			   				<div class="thumbnail">
			      				<div class="caption">
									<h3 style="color:#428bca">Recent quizzes they've made</h3>
									<table>
										<tr><th>Quiz</th><th>User</th></tr>
										<% ArrayList<String> friendRecentQuizzes = QuizHistory.getRecentQuizCreations(friends.get(i), connection); %>
										<% for(int j = 0; j < friendRecentQuizzes.size(); j++) { %>
											<tr><%= friendRecentQuizzes.get(j) %></tr>
										<% } %>
									</table>
								</div>
							</div>
						</div>
						<div class="col-md-4">
				 			<div class="thumbnail">
				    			<div class="caption">
									<h3 style="color:#428bca">Their recent achievements</h3>
									<ul class="list-group">
										<% ArrayList<String> friendNames = Achievements.getAchievementNames(friends.get(i), connection); %>
										<% for (int j = 0; j < friendNames.size(); j++) { %>
											<li><%=friendNames.get(j) %></li>
										<% } %>	
									</ul>
								</div>
							</div>
						</div>						
					</div>	
				<% } %>
			<% } %>
			</div>
		</div>
	</div>
</div>
</body>
</html>
