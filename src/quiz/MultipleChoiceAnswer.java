package quiz;

import java.sql.*;
import java.util.*;

public class MultipleChoiceAnswer extends Answer {

	public MultipleChoiceAnswer(int questionId, DBConnection db)
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

	@Override
	public ArrayList<String> getAnswerLocations() {
		ArrayList<String> locations = new ArrayList<String>();
		locations.add("choices");
		return locations;
	}

}
