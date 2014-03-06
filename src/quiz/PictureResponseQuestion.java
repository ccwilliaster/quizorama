package quiz;

import java.sql.SQLException;

public class PictureResponseQuestion extends Question {

	public PictureResponseQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		super(questionId, questionText, questionType, questionNum, quizId, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new PictureResponseAnswer(questionId, db);
	}
	
	@Override
	public String showQuestion() {
		String html = "<img src='" + questionText + "' alt='Picture not found' width='20'/>";
		return html;
	}

}
