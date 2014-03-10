<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quiz.*, java.util.*, java.text.DecimalFormat" %> 
<!DOCTYPE html>

<%
	DecimalFormat percent = new DecimalFormat("##0.#");
	DBConnection connection;
	Integer quizID = null;
	Integer userRating = null;
	String userType, userName;
	ArrayList<String> categories;
	ArrayList<String> tags;

	//Get DBConnection, user, and relevant quiz info
	connection         = (DBConnection) application.getAttribute("DBConnection");
	User user          = (User) session.getAttribute("user");
	int userID         = user.getUserID();
	
	if (request.getParameterMap().containsKey("quizID")) { // error handling
		 quizID     = Integer.parseInt( request.getParameter("quizID") );
	} else {
		request.getRequestDispatcher("error.jsp").forward(request, response); 
	}
	
	//Quiz quiz          = new Quiz(quizID, connection);
	String quizName    = "Test Quiz"; //quiz.getquizName();
	String quizSummary = "This quiz is so great! I really don't know how else to " +
						 "describe it. So maybe I'll just keep saying how great it " +
						 "is even though it doesn't really exist"; //quiz.getQuizSummary();
	Integer creatorID  = 3; //quiz.getquizCreatoruserID(); 
	String creatorName = "quiz creator"; //connection.getUserName(creatorID);
	double numStars    = 4; //Math.floor( quiz.getRating() ); // no half stars
	Integer numQuestions = 10; //quiz.getNumQuestions();
	double avgScore    = 9; //quiz.getAverageScore();
	categories         = new ArrayList<String>() {{ add("cat1"); add("cat2"); }}; //quiz.getCategories();
	tags               = new ArrayList<String>() {{ 
		add("tag1"); add("tag2"); add("tag3"); add("tag4"); add("tag5");
	}}; //quiz.getTags();
	
	// Figure out some user properties which toggle displays 
	if (userID == -1) { // guest
		userType = "guest";
		userName = "Guest";
	} else {
		userName   = user.getUserName();
		userRating = 2; //quiz.getUserRating(userID); // null if no rating
		
		if ( true /* user.isAdmin() */ ) { userType = "admin"; }
		else { userType = "standard"; }
	}

	// TODO: pull from quizHistory
	ArrayList<String> topScores    = new ArrayList<String>() {{ 
		add("<td>95%</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=3'>chris</a></td>"); 
		add("<td>90%</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=4'>chris2</a></td>");
	}}; 
	ArrayList<String> recentScores = new ArrayList<String>();
	ArrayList<String> userScores   = new ArrayList<String>();
%>
<%! // Helper functions
	public final int NUM_SCORES = 10; // number of scores to display

	/*
	 * Displays scores as a table
	 */
	public String printPerformance(ArrayList<String> performanceRows) {
		StringBuilder html = new StringBuilder();
		
		if ( performanceRows.size() == 0 ) { // no scores
			return "<h4><em>No scores to display</em></h4>";
		} 
		
		html.append(  // Table header
		"<table class='table table-hover table-condensed'>" +
		"<thead><tr><th>#</th><th>Score</th><th>User</th></tr></thead><tbody>"
		);
		
		for (int i=1; i <= performanceRows.size(); i++) { // add scores as rows
			if (i > NUM_SCORES) { break; }
			html.append("<tr><td>" + i + "</td>" + performanceRows.get(i-1) + "</tr>");
		}
		
		html.append("</tbody></table>"); // close tags
		return html.toString();
	}
	
	/*
	 * Displays quiz rating as glyphicon font stars
	 */
	public String getAvgRating(double numStars) {
		StringBuilder html = new StringBuilder();
		html.append( getStars(numStars,   true)  );
		html.append( getStars(5-numStars, false) );
		return html.toString();
	}
	
	/*
	 *Prints labels with the specified bootstrap label class, e.g., label-primary
	 * and of the specified height, e.g., h4
	 */
	public String printLabels(ArrayList<String> labels, String labelClass) {
		StringBuilder html = new StringBuilder();
		String label;
		for (int i=0; i < labels.size(); i++) {
			label = labels.get(i);
			html.append( "<span class='label " + labelClass + "'>" + label + "</span>");
			if ((i+1) % 5 == 0) { html.append("<br>"); }
		}
		return html.toString();
	}
	/*
	 * Helper for generating the specified number of glyphicon stars, filled or
	 * unfilled.
	 */
	public String getStars(double number, boolean filled) {
		String type;
		if (filled) { type = "glyphicon-star"; }
		else        { type = "glyphicon-star-empty"; }
			
		StringBuilder html = new StringBuilder();
		for (int i=1; i <= number; i++) {
			html.append("<span class='glyphicon " + type + "'></span>");
		}
		return html.toString();
	}
	
	/*
	 * Returns a dropdown menu for adding a rating, based on user information
	 */ 
	public String getRatingDropdown(String userType, int userID, int quizID, Integer userRating) {
		//if ( userType.equals("guest") ) return ""; // guests cannot rate
		
		StringBuilder html = new StringBuilder();
		html.append(
			"<button type='button' class='btn btn-primary btn-md dropdown-toggle' data-toggle='dropdown' >" +
				"<span class='caret' ></span>" +
				"<span class='sr-only' >Toggle Dropdown</span>" +
			"</button>" +
			"<ul class='dropdown-menu' role='menu'>" +
				"<li role='presentation' class='dropdown-header'>Rate quiz</li>" +
				"<li>" + 
				  "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=1'>" + 
				   getStars(1, true) + "</a>" + 
				"</li>" +
				"<li>" + 
				  "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=2'>" + 
				   getStars(2, true) + "</a>" + 
				"</li>" +
				"<li>" + 
				  "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=3'>" + 
				   getStars(3, true) + "</a>" + 
				"</li>" +
				"<li>" + 
				  "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=4'>" + 
				   getStars(4, true) + "</a>" + 
				"</li>" +
				"<li>" + 
				  "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=5'>" + 
				   getStars(5, true) + "</a>" + 
				"</li>" +
			"</ul>"
		);
			
		return html.toString();
	}
%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style>
		.label { margin: 2px 2px; }
	</style>
	<title><%= quizName %> summary</title>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-7">
					<h1><%= quizName %>
						<a class="btn btn-default btn-sm" <% out.println("href=userpage.jsp?userID=" + creatorID ); %> >
						by <%= creatorName %></a>
						
					</h1>
					<br>
					<div class="row">
						<p>
							<%= quizSummary %>
							<br><span class="badge"><%= numQuestions %> questions</span>
							<form action="QuizControllerServlet" >
								<input type="hidden" name="quizID" value=<%= quizID %> />
								<% 
								if ( userType.equals("guest") ) {
									out.println("<input class='disabled btn btn-primary' type='submit' value='Login to take quiz' />");
								} else {
									out.println("<input class='btn btn-primary' type='submit' value='Take quiz' />");
								}
								%>
							</form>
							
						</p>	
					</div>
				</div>
				<br><br>
				<div class="col-md-5">
      				<dl class="dl-horizontal text-left">
      					<dt>average rating</dt>
      					<dd class="text-center">

      						<div class="btn-group">
	      						<button type="button" class="btn btn-primary">
	      							<%= getAvgRating(numStars) %>
	      						</button>
	      						<%= getRatingDropdown( userType, userID, quizID, userRating ) %>
	      					</div>
	      					<% if (userRating != null) {
      							out.println("<br><span class='badge'>You rated " + userRating + " / 5</span>");
      						} %>
	      					
      					</dd>
      					<dt>average score</dt>
      					<dd>
      						<h3><span class="label label-primary">
      							<%= percent.format(100.0*avgScore/numQuestions) + " %" %>
      						</span>
      						</h3><br>
      					</dd>
      					<dt>categories</dt>
      					<dd><%= printLabels(categories, "label-warning") %><br><br></dd>
      					<dt>tags</dt>
      					<dd><%= printLabels(tags, "label-success") %></dd>
					</dl> 
      				

				</div>	
			</div>
			<br>
			<div class="row text-center">
				<div class="col-md-4">
	   				<div class="thumbnail">
	      				<div class="caption">
	        				<h3>Top scores</h3>
	        				<%= printPerformance( topScores ) %>
	        			</div>
	       			</div>
				</div>
				<div class="col-md-4">
	   				<div class="thumbnail">
	      				<div class="caption">
	        				<h3>Recent scores</h3>
	        				<%= printPerformance( recentScores ) %>
	        			</div>
	       			</div>
				</div>
				<div class="col-md-4">
	   				<div class="thumbnail">
	      				<div class="caption">
	        				<h3><%= userName + "'s scores" %></h3>
	        				<%= printPerformance( userScores ) %>
	        			</div>
	       			</div>
				</div>
			</div>
		</div>
	</div>	
</body>
</html>