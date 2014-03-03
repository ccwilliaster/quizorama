package quiz;

import java.sql.SQLException;
import java.util.List;

public class MultipleChoiceAnswers extends Answer {

	public MultipleChoiceAnswers(int questionId, DBConnection db)
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
		return 0;
	}

	@Override
	public String getAnswersHtml() {
		String html = "";
		for (String answer : answers) {
			html += "<input type='radio' name='choices' value='" + answer + "'>" + answer + "</input><br/>";
		}
		return html;
	}

	@Override
	public int possiblePoints() {
		return 1;
	}

}
