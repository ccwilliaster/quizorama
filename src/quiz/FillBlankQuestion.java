package quiz;

import java.sql.SQLException;

public class FillBlankQuestion extends Question {

	public FillBlankQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		super(questionId, questionText, questionType, questionNum, quizId, db);
	}

	@Override
	public void addAnswers(DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new FillBlankAnswer(questionId, db);
	}


	
}
