package quiz;

import java.sql.*;
import java.util.*;

public abstract class Question implements Comparable {
	public static final int QTYPE_QR = 0;
	public static final int QTYPE_FB = 1;
	public static final int QTYPE_MC = 2;
	public static final int QTYPE_PR = 3;
	
	final int questionId;
	String questionText;
	final int questionType;
	final int questionNum;
	final int quizId;
	Answer answers;
	
	public Question(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		this.questionId = questionId;
		this.questionText = questionText;
		this.questionType = questionType;
		this.questionNum = questionNum;
		this.quizId = quizId;

		addAnswers(db);
	}
	
	public abstract void addAnswers(DBConnection db) throws NumberFormatException, SQLException;	
	
	public String showQuestion() {
		String html = "<p>" + questionText + "</p>";
		html += answers.getAnswersHtml();
		return html;
	}
	
	public String showAnswerOptions() {
		return answers.showAnswerOptions();
	}

	/**
	 * @return the questionId
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @return the questionType
	 */
	public int getQuestionType() {
		return questionType;
	}

	/**
	 * @return the questionNum
	 */
	public int getQuestionNum() {
		return questionNum;
	}

	/**
	 * @return the quizId
	 */
	public int getQuizId() {
		return quizId;
	}

	public int checkAnswers(ArrayList<String> guesses) {
		return answers.checkAnswers(guesses);
	}
	
	public int possiblePoints() {
		return answers.possiblePoints();
	}
	
	public ArrayList<String> getAnswerLocations() {
		return answers.getAnswerLocations();
	}
	
	public String showAnswer() {
		return answers.showAnswer();
	}
	
	@Override
	public int compareTo(Object o) {
		Question other = (Question)o;
		return questionNum - other.questionNum;
	}
	
}
