package message;


/**
 * A Challenge Message type, used when one user wants to challenge another user
 * to take a particular quiz. Message content is automatically generated based
 * on user identifiers and Quiz attributes.
 */
public class Challenge extends Message {

	public Challenge(Integer toUserID, Integer fromUserID, int quizID) {
		super(TYPE_CHALLENGE, toUserID, fromUserID);
		
		addSubject("Someone wants to test your quiz skills!");
		generateChallengeContent(quizID);
	}
	
	/**
	 * Generates standardized 'challenge' message content, based on recipient 
	 * and sending user identifiers and quizID.
	 * @param quizID
	 */
	private void generateChallengeContent(int quizID) {
		// TODO
		// needs link to fromUserID's page, and to the quizID
		// could add more information about the quiz
		// 	e.g., categories, tags, etc.
		StringBuilder content = new StringBuilder();
		content.append("<h1>Someone challenged you to take a quiz!</h1>");
		content.append("<p><a href=\"userpage.jsp?userID=" + fromUserID + ">");
		content.append(fromUserID + "</a> challenged you to take the quiz "); 
		content.append("<a href=\"quiz.jsp?quizID=" + quizID + ">" + quizID + "</a></p>");
		
		addContent( content.toString() );
	}
	
}
