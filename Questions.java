package quiz;

import java.sql.*;
import java.util.*;

public abstract class Questions {
	final int questionId;
	String questionText;
	Answer answers;
	
	public Questions(int questionId, String questionText, DBConnection db) throws SQLException {
		ResultSet rs = db.getQuestionInfo(questionId);
		this.questionId = questionId;
		rs.first();
		questionText = rs.getString("question");
	}
	
	public abstract void addAnswers(int questionId, DBConnection db) throws NumberFormatException, SQLException;
	
	public String showQuestion() {
		String html = "<p>" + questionText + "</p>";
		return html;
	}
	
	public String showAnswer() {
		return answers.getAnswersHtml();
	}
	
	public int checkAnswers(List<String> guesses) {
		return answers.checkAnswers(guesses);
	}
	
	public int possiblePoints() {
		return answers.possiblePoints();
	}
	
}
