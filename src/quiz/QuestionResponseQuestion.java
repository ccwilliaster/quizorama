package quiz;

import java.sql.*;

public class QuestionResponseQuestion extends Question {

	public QuestionResponseQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		super(questionId, questionText, questionType, questionNum, quizId, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db) throws NumberFormatException, SQLException {
		answers = new QuestionResponseAnswer(questionId, db);		
	}

}
