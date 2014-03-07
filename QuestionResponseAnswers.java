package quiz;

import java.sql.*;
import java.util.*;

public class QuestionResponseAnswers extends Answer {

	
	public QuestionResponseAnswers(int questionId, DBConnection db)
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
		return "<input type='text' name='answer'/>";
	}

	@Override
	public int possiblePoints() {
		return 1;
	}


}
