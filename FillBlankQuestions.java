package quiz;

import java.sql.SQLException;

public class FillBlankQuestions extends Questions {

	public FillBlankQuestions(int questionId, String questionText,
			DBConnection db) throws SQLException {
		super(questionId, questionText, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new FillBlankAnswers(questionId, db);
	}

}
