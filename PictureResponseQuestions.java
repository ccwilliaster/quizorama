package quiz;

import java.sql.SQLException;

public class PictureResponseQuestions extends Questions {

	public PictureResponseQuestions(int questionId, String questionText,
			DBConnection db) throws SQLException {
		super(questionId, questionText, db);
	}

	@Override
	public void addAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new PictureResponseAnswers(questionId, db);
	}
	
	@Override
	public String showQuestion() {
		String html = "<img src='" + questionText + "' alt='Picture not found' width='20'/>";
		return html;
	}

}
