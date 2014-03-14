package quiz;

import java.sql.SQLException;

public class PictureResponseQuestion extends Question {

	public PictureResponseQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
		super(questionId, questionText, questionType, questionNum, quizId, db);
	}

	@Override
	public void addAnswers(DBConnection db)
			throws NumberFormatException, SQLException {
		answers = new PictureResponseAnswer(questionId, db);
	}
	
	@Override
	public String showQuestion() {
		String html = "<p>Identify the picture:</p>";
		html += ("<p><img src='" + questionText + "' alt='Picture not found' width='200'/></p>");
		html += answers.getAnswersHtml();
		return html;
	}
	
	@Override
	public String showQuestionText() {
		String html = "<p>Identify the picture:</p>";
		html += ("<p><img src='" + questionText + "' alt='Picture not found' width='200'/></p>");
		return html;
	}
	

}
