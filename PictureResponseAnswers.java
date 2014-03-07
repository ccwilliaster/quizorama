package quiz;

import java.sql.SQLException;
import java.util.List;

public class PictureResponseAnswers extends Answer {

	public PictureResponseAnswers(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		super(questionId, db);
	}

	@Override
	public int checkAnswers(List<String> answers) {
		int score = 0;
		for (String answer : answers) {
			if (this.answers.contains(answer)) {
				score = possiblePoints();
			}
		}
		return score;
	}

	@Override
	public String getAnswersHtml() {
		String html = "<input type='text' name='answer'/>";
		return html;
	}

	@Override
	public int possiblePoints() {
		return 1;
	}

}
