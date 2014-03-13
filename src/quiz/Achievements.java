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
	public static boolean finishedQuiz(int userID, int quizID, int score, DBConnection connection) throws SQLException{
		ArrayList<Score> allScores = QuizHistory.getHistories(null, quizID, connection);
		boolean isHighestScore = true;
		for (int i = 0; i < allScores.size(); i++) {
			if (allScores.get(i).getScore() >= score) {
				isHighestScore = false;
				break;
			}
		}
		ArrayList<Score> userScores = QuizHistory.getHistories(userID, quizID, connection);
		int numQuizzesTaken = userScores.size();
		if (isHighestScore) {
			connection.setAchievement(userID, I_AM_THE_GREATEST, 
					"Congrats, you got the highest score so far on this quiz!");
		}
		if (numQuizzesTaken == 10) {
			connection.setAchievement(userID, QUIZ_MACHINE, 
					"Congrats, you have taken ten quizzes!");
		}
		return isHighestScore || numQuizzesTaken == 10;
	}
	
	/* This method should be called after a quiz is created. IMPORTANT: IT MUST BE CALLED
	 * 	AFTER THE DB IS UPDATED WITH THIS QUIZ CREATION. The method returns a boolean describing
	 * 	whether or not the user gained an achievement.
	 */
	public static boolean wroteQuiz(int userID, DBConnection connection) throws SQLException {
		boolean achievementEarned = false;
		ArrayList<QuizCreation> quizzes = QuizHistory.getQuizzes(userID, connection);
		int quizzesCreated = quizzes.size();
		if (quizzesCreated == 1) {
			connection.setAchievement(userID, AMATEUR_AUTHOR, 
					"Congrats, you have authored a quiz!");
			achievementEarned = true;
		} else if (quizzesCreated == 5) {
			connection.setAchievement(userID, PROLIFIC_AUTHOR, 
					"Congrats, you have authored five quizzes!");
			achievementEarned = true;
		} else if (quizzesCreated == 10) {
			connection.setAchievement(userID, PRODIGIOUS_AUTHOR, 
					"Congrats, you have authored ten quizzes!");
			achievementEarned = true;
		}
		return achievementEarned;
	}
	
	/* This method returns a list of achievement IDs that the user has gained.
	 */
	public static ArrayList<Integer> getAchievements(int userID, DBConnection connection) throws SQLException {
		ResultSet rs = connection.getuserAchievements(userID);
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
