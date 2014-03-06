package quiz;

import java.sql.*;
import java.util.*;

public abstract class Answer {
	protected List<String> answers;
	private final int questionId;
	private int numAnswers;
	private int[] answerIds;
	
	public Answer(int questionId, DBConnection db) throws NumberFormatException, SQLException {
		answers = new ArrayList<String>();
		this.questionId = questionId;
		ResultSet rs = db.getAnswerInfo(questionId);
		rs.last();
		numAnswers = rs.getRow();
		answerIds = new int[numAnswers];
		
		rs.first();
		
		for (int i = 0; i < numAnswers; i++) {
			answerIds[i] = Integer.parseInt(rs.getString("answerId"));
			answers.add(rs.getString("answer"));
			rs.next();
		}
	}
	
	public abstract int checkAnswers(List<String> answers);
	
	public abstract String getAnswersHtml();
	
	public abstract int possiblePoints();

	public abstract ArrayList<String> getAnswerLocations();
	
	public String showAnswer() {
		if (answers == null) return null;
		String html = "<p>" + answers.get(0) + "</p>";
		return html;
	}
	
	
	
}
