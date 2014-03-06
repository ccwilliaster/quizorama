package quiz;

import java.sql.SQLException;

public class MultipleChoiceQuestion extends Question {

	public MultipleChoiceQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		super(questionId, questionText, questionType, questionNum, quizId, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new MultipleChoiceAnswer(questionId, db);
	}

}
