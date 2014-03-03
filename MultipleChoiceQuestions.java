package quiz;

import java.sql.SQLException;

public class MultipleChoiceQuestions extends Questions {

	public MultipleChoiceQuestions(int questionId, String questionText,
			DBConnection db) throws SQLException {
		super(questionId, questionText, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new MultipleChoiceAnswers(questionId, db);
	}

}
