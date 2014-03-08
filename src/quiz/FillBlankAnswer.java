package quiz;

import java.sql.*;
import java.util.*;

public class FillBlankAnswer extends Answer {

	public FillBlankAnswer(int questionId, DBConnection db)
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

	@Override
	public ArrayList<String> getAnswerLocations() {
		ArrayList<String> locations = new ArrayList<String>();
		locations.add("answer");
		return locations;
	}

}
