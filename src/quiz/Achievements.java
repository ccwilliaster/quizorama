package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Achievements {
	
	public static final int AMATEUR_AUTHOR = 1;
	public static final int PROLIFIC_AUTHOR = 2;
	public static final int PRODIGIOUS_AUTHOR = 3;
	public static final int QUIZ_MACHINE = 4;
	public static final int I_AM_THE_GREATEST = 5;
	public static final int PRACTICE_MAKES_PERFECT = 6;
	public static final String[] achievementNames = new String[] {"", "Amateur Author", 
								"Prolific Author", "Prodigious Author", "Quiz Machine",
								"I am the Greatest", "Practice Makes Perfect"};
	
	/* This method should be called whenever a quiz is finished. IMPORTANT: IT MUST BE CALLED
	 * 	AFTER THE DB HAS BEEN UPDATED WITH THIS QUIZ TAKE. It returns a boolean where or not
	 * 	the user gained an achievement. The DB is updated if the user did gain an achievement.
	 */
	//NEED TO ADD FOR PRACTICE MODE
	public static ArrayList<String> finishedQuiz(int userID, int quizID, int score, boolean practice, DBConnection connection) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Score> allScores = QuizHistory.getHistories(null, quizID, connection);
		boolean isHighestScore = true;
		for (int i = 0; i < allScores.size(); i++) {
			if (allScores.get(i).getScore() >= score) {
				isHighestScore = false;
				break;
			}
		}
		ArrayList<Score> userScores = QuizHistory.getHistories(userID, quizID, connection);
		int numQuizzesTaken = userScores.size() + 1;
		if (isHighestScore) {
			result.add(achievementNames[I_AM_THE_GREATEST]);
			connection.setAchievement(userID, I_AM_THE_GREATEST, 
					"Congrats, you got the highest score so far on this quiz!");
		}
		if (numQuizzesTaken == 10) {
			result.add(achievementNames[QUIZ_MACHINE]);
			connection.setAchievement(userID, QUIZ_MACHINE, 
					"Congrats, you have taken ten quizzes!");
		}
		if (practice) {
			result.add(achievementNames[PRACTICE_MAKES_PERFECT]);
			connection.setAchievement(userID, PRACTICE_MAKES_PERFECT, 
					"Congrats, you have taken a quiz in practice mode!");
		}
		return result;
	}
	
	/* This method should be called after a quiz is created. IMPORTANT: IT MUST BE CALLED
	 * 	AFTER THE DB IS UPDATED WITH THIS QUIZ CREATION. The method returns a boolean describing
	 * 	whether or not the user gained an achievement.
	 */
	public static ArrayList<String> wroteQuiz(int userID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<QuizCreation> quizzes = QuizHistory.getQuizzes(userID, connection);
		int quizzesCreated = quizzes.size();
		if (quizzesCreated == 1) {
			result.add(achievementNames[AMATEUR_AUTHOR]);
			connection.setAchievement(userID, AMATEUR_AUTHOR, 
					"Congrats, you have authored a quiz!");
		} else if (quizzesCreated == 5) {
			result.add(achievementNames[PROLIFIC_AUTHOR]);
			connection.setAchievement(userID, PROLIFIC_AUTHOR, 
					"Congrats, you have authored five quizzes!");
		} else if (quizzesCreated == 10) {
			result.add(achievementNames[PRODIGIOUS_AUTHOR]);
			connection.setAchievement(userID, PRODIGIOUS_AUTHOR, 
					"Congrats, you have authored ten quizzes!");
		}
		return result;
	}
	
	/* This method returns a list of achievement IDs that the user has gained.
	 */
	public static ArrayList<Integer> getAchievements(int userID, DBConnection connection) throws SQLException {
		ResultSet rs = connection.getUserAchievements(userID);
		ArrayList<Integer> result = new ArrayList<Integer>();
		while(rs.next()) {
			int achievementID = rs.getInt("achievementID");
			result.add(achievementID);
		}
		return result;
	}
	
	/* This method returns a list of achievement names that the user has gained.
	 */
	public static ArrayList<String> getAchievementNames(int userID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> IDs = getAchievements(userID, connection);
		for (int i = 0; i < IDs.size(); i++) {
			result.add(achievementNames[IDs.get(i)]);
		}
		return result;
	}

}
