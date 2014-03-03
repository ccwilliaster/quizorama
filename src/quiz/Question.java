package quiz;

import java.util.ArrayList;

public interface Question {

	String showQuestion();

	String showAnswerOptions();

	ArrayList<String> getAnswerLocations();

	int checkAnswer(ArrayList<String> submittedAnswers);

	int possiblePoints();

}
