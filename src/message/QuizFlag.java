package message;

public class QuizFlag extends Message {

	public QuizFlag(Integer toUserID, Integer fromUserID, int quizID) {
		super(TYPE_QUIZ_FLAG, toUserID, fromUserID);
		addSubject("Quiz " + quizID + " flagged");
		generateFlagContent(quizID);
	}
	
	/**
	 * Generates the content of the QuizFlag Message, based on the User who
	 * flagged the Quiz, and the Quiz flagged.
	 * @param quizID
	 */
	private void generateFlagContent(int quizID) {
		
		StringBuilder content = new StringBuilder();
		
		content.append("<p><a href=\"userpage.jsp?userID=" + fromUserID + ">");
		content.append(fromUserID + "</a> flagged the quiz "); 
		content.append("<a href=\"quiz.jsp?quizID=" + quizID + ">" + quizID );
		content.append("</a> as inappropriate. Take action by following the link</p>");
		
		addContent( content.toString() );
	}
}
