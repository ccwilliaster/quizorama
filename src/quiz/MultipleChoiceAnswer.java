package quiz;

import java.sql.*;
import java.util.*;

public class MultipleChoiceAnswer extends Answer {
	
	String correctAnswer = "";

	public MultipleChoiceAnswer(int questionId, DBConnection db)
			throws NumberFormatException, SQLException {
		super(questionId, db);
		for (int i = 0; i < numAnswers; i++) {
			String answer = answers.get(i);
			if (!answer.isEmpty()) {
				if (answer.charAt(0) == '!') {
					answer = answer.substring(1); //Get rid of the incorrect answers
					answers.set(i, answer);
				} else {
					correctAnswer = answer;
				} //else
			} //if
		} //for
	} //Constructor

	@Override
	public int checkAnswers(List<String> answers) {
		int score = 0;
		for (String answer : answers) {
			if (correctAnswer.equals(answer)) {
				score = possiblePoints();
			}
		}
		return score;
	}

	@Override
	public String getAnswersHtml() {
		String html = "";
		Collections.shuffle(answers);
		for (String answer : answers) {
			String location = "choices-" + questionId;
			locations.add(location);
			html += "<p><input type='radio' name='" + location + "' value='" + answer + "'>  " + answer + "</input></p>";
		}
		return html;
	}

	@Override
	public int possiblePoints() {
		return 1;
	}

	@Override
	public String showAnswerOptions() {
		String html = "Correct answer is: ";
		html += correctAnswer;
		return html;
	}
	
}
