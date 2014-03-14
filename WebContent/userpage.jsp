<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="quiz.*,java.util.*,java.sql.*" %>
<% 
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
	<tag:navbar session="<%= session %>" activeTab="home" />
	<div class="container">
		<div class="jumbotron">
<% if (userIDError) { %>
	<h1>This Page Should be Called with a userID parameter specified!</h1>
<% } else if (!userOwnPage) { %>
	<h1><%= connection.getUserName(userPageID) %></h1>
	<% if (user == null) { %>
		<a href="homepage.jsp">Log In</a>
	<% } else if (connection.usersAreFriends(user.getUserID(), userPageID)) { %>
		<%= FriendRequestServlet.getRemoveFriendLink("Remove Friend", user.getUserID(), userPageID) %>
	<% } else { %>
		<%= FriendRequestServlet.getMakeFriendRequestLink("Add Friend") %>
	<% } %>
	<h3>Recent Scores</h3>
	<table>
		<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
		<% for(int i = 0; i < recentScores.size(); i++) { %>
			<tr><%= recentScores.get(i) %></tr>
		<% } %>
	</table>
	<h3>Recent Quizzes Created By <%= connection.getUserName(userPageID) %></h3>
	<table>
		<tr><th>Quiz</th><th>User</th></tr>
		<% for(int i = 0; i < recentQuizzesByUser.size(); i++) { %>
			<tr><%= recentQuizzesByUser.get(i) %></tr>
		<% } %>
	</table>
	<h3>Achievements</h3>
	<ul>
		<% ArrayList<String> names = Achievements.getAchievementNames(userPageID, connection); %>
		<% for (int i = 0; i < names.size(); i++) { %>
			<li><%=names.get(i) %></li>
		<% } %>
	</ul>
<% } else { %>
	<h1>Welcome Back, <%= user.getUserName() %></h1>
	<ul>
		<li>
			<h3>Announcements</h3>
			<% Set<Integer> announcementType = new HashSet<Integer>();
			announcementType.add(Message.TYPE_ANNOUNCEMENT);
			ResultSet allUserMessages = connection.getUserMessages(userPageID);
			ArrayList<Message> usersAnnouncements = Message.loadMessages( allUserMessages, announcementType, userPageID, null ); %>
			<a href="userMessages.jsp">See All Messages</a>
			<% 	for (Message message: usersAnnouncements) {
					out.println("<div class=\"container\">");
					out.println("<br>");
					out.println(message.displayAsHTML(connection));
					out.println("<br>");
					out.println("</div>"); 
				}	%>
		</li>
		<li>
			<h3>Popular Quizzes</h3>			
			<table>
				<tr><th>Quiz</th><th>Quiz Creator</th></tr>
				<% for(int i = 0; i < popQuizzes.size(); i++) { %>
					<tr><%= popQuizzes.get(i) %></tr>
				<% } %>
			</table>
		</li>
		<li>
			<h3>Recent Quizzes Created</h3>
			<table>
				<tr><th>Quiz</th><th>User</th></tr>
				<% for(int i = 0; i < recentQuizzes.size(); i++) { %>
					<tr><%= recentQuizzes.get(i) %></tr>
				<% } %>
			</table>
		</li>
		<li>	
			<h3>Recent Scores</h3>
			<table>
				<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
				<% for(int i = 0; i < recentScores.size(); i++) { %>
					<tr><%= recentScores.get(i) %></tr>
				<% } %>
			</table>
		</li>
		<li>	
			<h3>Recent Quizzes You Created</h3>
			<table>
				<tr><th>Quiz</th><th>User</th></tr>
				<% for(int i = 0; i < recentQuizzesByUser.size(); i++) { %>
					<tr><%= recentQuizzesByUser.get(i) %></tr>
				<% } %>
			</table>
		</li>
		<li>
			<h3>Achievements Earned:</h3>
				<ul>
					<% ArrayList<String> names = Achievements.getAchievementNames(userPageID, connection); %>
					<% for (int j = 0; j < names.size(); j++) { %>
						<li><%=names.get(j) %></li>
					<% } %>
				</ul>
		</li>
		<li><h3>Recent Messages Received</h3>
			<a href="userMessages.jsp">See All Messages</a>
			<% 	for (Message message: recentMessages) {
					out.println("<div class=\"container\">");
					out.println("<br>");
					out.println(message.displayAsHTML(connection));
					out.println("<br>");
					out.println("</div>"); 
				}	%>
		</li>
		<li><h3>Friend Activity</h3>
			<ul>
				<% for(int i = 0; i < friends.size(); i++) { %>
					<li><a class="btn btn-default btn-xs" href="userpage.jsp?userID=<%=friends.get(i)%>"><%=connection.getUserName(friends.get(i)) %></a>
							<%=FriendRequestServlet.getRemoveFriendLink("Remove Friend", userPageID, friends.get(i)) %>
						<ul>
							<li>Recent Scores
								<table>
									<tr><th>Score</th><th>Quiz</th><th>User</th></tr>
									<% ArrayList<String> friendRecentScores = QuizHistory.getRecentScores(friends.get(i), null, connection); %>
									<% for(int j = 0; j < friendRecentScores.size(); j++) { %>
										<tr><%= friendRecentScores.get(j) %></tr>
									<% } %>
								</table>
							</li>
							<li>Recent Quizzes Created
								<table>
									<tr><th>Quiz</th><th>User</th></tr>
									<% ArrayList<String> friendRecentQuizzes = QuizHistory.getRecentQuizCreations(friends.get(i), connection); %>
									<% for(int j = 0; j < friendRecentQuizzes.size(); j++) { %>
										<tr><%= friendRecentQuizzes.get(j) %></tr>
									<% } %>
								</table>
							</li>
							<li>Achievements Earned
								<ul>
									<% ArrayList<String> friendNames = Achievements.getAchievementNames(friends.get(i), connection); %>
									<% for (int j = 0; j < friendNames.size(); j++) { %>
										<li><%=friendNames.get(j) %></li>
									<% } %>
								</ul>
							</li>
						</ul>
					</li>
				<% } %>
			</ul>
		</li>
	</ul>
<% } %>
	</div>
	<a href="CreateQuiz.jsp">Create A New Quiz!</a>
</div>
</body>
</html>