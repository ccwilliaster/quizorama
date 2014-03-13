package quiz;

import java.sql.*;
import java.util.*;

public abstract class Answer {
	protected List<String> answers;
	protected final int questionId;
	protected int numAnswers;
	private int[] answerIds;
	protected ArrayList<String> locations;
	//private final int answerNums[];
	
	public Answer(int questionId, DBConnection db) throws NumberFormatException, SQLException {
		this.questionId = questionId;
		answers = new ArrayList<String>();
		ResultSet rs = db.getAnswerInfo(questionId);
		rs.last();
		numAnswers = rs.getRow();
		answerIds = new int[numAnswers];
		locations = new ArrayList<String>();
		rs.first();
		
		for (int i = 0; i < numAnswers; i++) {
			answerIds[i] = rs.getInt("answerId");
			answers.add(rs.getString("answer"));
			rs.next();
		}
	}
	
	public abstract int checkAnswers(List<String> answers);
	
	public abstract String getAnswersHtml();
	
	public abstract int possiblePoints();

	public ArrayList<String> getAnswerLocations() {
		return locations;
	}
	
	public String showAnswer() {
		if (answers == null) return null;
		String html = "<p>" + answers.get(0) + "</p>";
		return html;
	}
	
	public String showAnswerOptions() {
		String html = "";
		if (answers.size() > 1) {
			html += "<p>Correct answers are: ";
		} else if (answers.size() == 1) {
			html += "<p>Correct answer is: ";
		}
		boolean first = true;
		for (String answer : answers) {
			if (!first) {
				html += ", ";
			}
			html += answer;
			first = false;
		}
		html += "</p>";
		return html;
	}
	
	
}
