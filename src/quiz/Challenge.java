package quiz;

/**
 * A Challenge Message type, used when one user wants to challenge another user
 * to take a particular quiz. Message content is automatically generated based
 * on user identifiers and Quiz attributes.
 */
public class Challenge extends Message {

	public Challenge(Integer toUserID, Integer fromUserID, Integer quizID) {
		super(TYPE_CHALLENGE, toUserID, fromUserID);
		
		addSubject("Someone wants to test your quiz skills!");
		addContent( generateChallengeContent(fromUserID, quizID) );
	}
	
	/**
	 * Generates standardized 'challenge' message content, based on recipient 
	 * and sending user identifiers and quizID.
	 * @param quizID
	 */
	private static String generateChallengeContent(int fromUserID, int quizID) {
		// TODO: update to quizName
		//       it would be nice to have getUserButton() and getQuizButton() methods
		String content = 
			"<h4>Someone challenged you to a quiz!</h4> User <a class=\"btn " +
			"btn-default btn-xs\" href=\"userpage.jsp?userID=" + fromUserID + "\">" +
		 	fromUserID + "</a> suggested you take the quiz " +
		 	"<a class=\"btn btn-default btn-xs\" href=\"quizSummary.jsp?quizID=" + 
		 	quizID + "\">" + quizID + "</a> Follow the link to find out more!<br>";

		return content;
	}
	
	/**
	 * Displays the necessary HTML form information to create a new Challenge
	 * @return
	 */
	public static String getCreationHTML(Integer userID, String quizName) { 
		StringBuilder html = new StringBuilder();

		html.append("<input name=\"fromUserID\" type=\"hidden\" value=" + userID + " />");
		html.append("<input name=\"type\" type=\"hidden\" value=" + Message.TYPE_CHALLENGE + " />");
		html.append("<input name=\"hasContent\" type=\"hidden\" value=\"true\" />");
		
		html.append( 
			"<div class=\"row\"><br>" +
			  "<div class=\"form-group\">" +
			    "<label for=\"2\" class=\"col-sm-2 control-label\">Quiz</label>" +
			    "<div class=\"col-sm-4\">" +
			      "<input id=\"2\" type=\"text\" class=\"form-control\""); 
		
		if (quizName == null) {
			html.append("name=\"quizName\" placeholder=\"Quiz name\">");
		} else {
			html.append("name=\"quizName\" readonly value=" + 
					    quizName + " placeholder=" + quizName + " >");
		}
		html.append(
				"</div>" +
			  "</div>" +
			"</div>" +
			"<div class=\"row\">" +
			  "<div class=\"form-group\">" +
			    "<label for=\"1\" class=\"col-sm-2 control-label\">Friend to challenge</label>" +
				"<div class=\"col-sm-4\">" +
				  "<input id=\"1\" type=\"text\" class=\"form-control\"" +
				              "name=\"toUserName\" placeholder=\"Username\">" +
				"</div>" +
			  "</div>" +
			"</div>"
		);
		return html.toString(); 
	}
}
