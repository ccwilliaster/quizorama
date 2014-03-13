package quiz;

import java.sql.SQLException;

public class QuizFlag extends Message {

	public QuizFlag(int toUserID, int fromUserID, 
	         String fromUserName, String quizName, int quizID) {
		
		super(TYPE_QUIZ_FLAG, toUserID, fromUserID);
		addSubject("Quiz " + quizName + " flagged by " + fromUserName);
		addContent( generateFlagContent(quizID, quizName, fromUserID, fromUserName) );
	}
	
	/**
	 * Generates the content of the QuizFlag Message, based on the User who
	 * flagged the Quiz, and the Quiz flagged.
	 * @param quizID
	 */
	private String generateFlagContent(int quizID, String quizName, int fromUserID, 
			String fromUserName) {
		 
		return  "User <a class='btn btn-default btn-xs' href='userpage.jsp?userID=" + 
				fromUserID + "'>" + fromUserName + "</a> flagged the quiz " +
			 	"<a class='btn btn-default btn-xs' href='quizSummary.jsp?quizID=" + 
			 	quizID + "'>" + quizName + "</a> as inappropriate. Take action by " +
			 	"following the link";
	}
	
	/**
	 * Static method to generate a new QuizFlag Message and add
	 * it to the database via the DBConnection
	 */
	public static void makeQuizFlag(int quizID, String quizName, int fromUserID, 
			int toUserID, String fromUserName, DBConnection connection) throws
			SQLException {
		
		QuizFlag qf = new QuizFlag(toUserID, fromUserID, fromUserName, quizName, quizID);
		connection.addMessage(qf);
	}
}
