package quiz;

import java.sql.*;

public class QuestionResponseQuestions extends Questions {

	public QuestionResponseQuestions(int questionId, String questionText,
			DBConnection db) throws SQLException {
		super(questionId, questionText, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db) throws NumberFormatException, SQLException {
		answers = new QuestionResponseAnswers(questionId, db);		
	}

}
