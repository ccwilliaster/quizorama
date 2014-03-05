package quiz;

import java.util.ArrayList;

public abstract class Question {

	String showQuestion() {
		return null;
	}

	String showAnswerOptions() {
		return null;
	}

	ArrayList<String> getAnswerLocations() {
		return null;
	}

	int checkAnswer(ArrayList<String> submittedAnswers) {
		return 0;
	}

	int possiblePoints() {
		return 0;
	}

}
