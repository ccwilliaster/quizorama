package quiz;

import java.sql.*;
import java.util.*;

public class QuestionResponseAnswer extends Answer {

	
	public QuestionResponseAnswer(int questionId, DBConnection db)
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
		String location = "answer-" + questionId;
		locations.add(location);
		String html = "<input type='text' name='" + location + "'/>";
		return html;
	}

	@Override
	public int possiblePoints() {
		return 1;
	}

}
