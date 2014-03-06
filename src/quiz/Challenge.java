package quiz;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 * A Challenge Message type, used when one user wants to challenge another user
 * to take a particular quiz. Message content is automatically generated based
 * on user identifiers and Quiz attributes.
 */
public class Challenge extends Message {

	public Challenge(int toUserID, int fromUserID, 
			         String fromUserName, String quizName, int quizID) {	
		super(TYPE_CHALLENGE, toUserID, fromUserID);
		String content = 
			generateChallengeContent(fromUserID, fromUserName, quizID, quizName);
		
		addSubject("Someone wants to test your quiz skills!");
		addContent( content );
	}
	
	/**
	 * Static method to generate a new Challenge Message from a request and add
	 * it to the database via the DBConnection
	 * @param request request expected to contain the parameters specified by the
	 * Challenge.getCreationHTML() method
	 * @param connection connection to database, has addMessage() method
	 * @throws SQLException
	 */
	public static void makeChallenge(HttpServletRequest request, DBConnection connection) 
	throws SQLException {

		String toUserName   = request.getParameter("toUserName");
		Integer toUserID    = connection.getUserID( toUserName );
		Integer fromUserID  = Integer.parseInt(request.getParameter("fromUserID"));
		String fromUserName = connection.getUserName(fromUserID);
		String quizName     = request.getParameter("quizName");
		Integer quizID      = 0; // TODO need method for converting quizName to quizID 
		
		Challenge newChallenge = 
			new Challenge(toUserID, fromUserID, fromUserName, quizName, quizID);
		connection.addMessage(newChallenge);
	}
	
	/**
	 * Generates standardized 'challenge' message content, based on recipient 
	 * and sending user identifiers and quizID.
	 * @param quizID
	 */
	private static String 
	generateChallengeContent(int fromUserID, String fromUserName, int quizID, String quizName) {
		// TODO: update to quizName
		//       it would be nice to have getUserButton() and getQuizButton() methods
		
		String content = 
			"<h4>Someone challenged you to a quiz!</h4> User <a class='btn " +
			"btn-default btn-xs' href='userpage.jsp?userID=" + fromUserID + "'>" +
		 	fromUserName + "</a> suggested you take the quiz " +
		 	"<a class='btn btn-default btn-xs' href='quizSummary.jsp?quizID=" + 
		 	quizID + "'>" + quizName + "</a> Follow the link to find out more!<br>";

		return content;
	}
	
	/**
	 * Displays the necessary HTML form information to create a new Challenge
	 * @return
	 */
	public static String getCreationHTML(Integer userID, String quizName) { 
		StringBuilder html = new StringBuilder();

		html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
		html.append("<input name='type' type='hidden' value=" + Message.TYPE_CHALLENGE + " />");
		html.append("<input name='hasContent' type='hidden' value='true' />");
		
		html.append( 
			"<div class='row'><br>" +
			  "<div class='form-group'>" +
			    "<label for='2' class='col-sm-2 control-label'>Quiz</label>" +
			    "<div class='col-sm-4'>" +
			      "<input id='2' type='text' class='form-control'"); 
		
		if (quizName == null) {
			html.append("name='quizName' placeholder='Quiz name'>");
		} else {
			html.append("name='quizName' readonly value=" + 
					    quizName + " placeholder=" + quizName + " >");
		}
		html.append(
				"</div>" +
			  "</div>" +
			"</div>" +
			"<div class='row'>" +
			  "<div class='form-group'>" +
			    "<label for='1' class='col-sm-2 control-label'>Friend to challenge</label>" +
				"<div class='col-sm-4'>" +
				  "<input id='1' type='text' class='form-control'" +
				              "name='toUserName' placeholder='Username'>" +
				"</div>" +
			  "</div>" +
			"</div>"
		);
		return html.toString(); 
	}
}
