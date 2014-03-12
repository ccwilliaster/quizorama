package quiz;

import java.sql.*;
import java.util.*;

public abstract class Question {
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

		addAnswers(questionId, db);
	}
	
	public abstract void addAnswers(int questionId, DBConnection db) throws NumberFormatException, SQLException;
	
	public void addAnswers( DBConnection db) throws NumberFormatException, SQLException {
		addAnswers(questionId, db);
	} //addAnswers
	
	
	public String showQuestion() {
		String html = "<p>" + questionText + "</p>";
		return html;
	}
	
	public String showAnswerOptions() {
		return answers.getAnswersHtml();
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

}
