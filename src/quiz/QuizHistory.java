package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class QuizHistory {
	
	private static final int NUM_TOP_SCORES = 3;
	private static final int NUM_RECENT_SCORES = 3;
	private static final int NUM_POP_SCORES = 3;
	
	/* Returns an arrayList containing all of the ratings associated with this 
	 * quiz ID.
	 */
	public static ArrayList<Rating> getRatingsByQuiz(int quizID, DBConnection connection) throws SQLException{
		ArrayList<Rating> result = new ArrayList<Rating>();
		ResultSet ratings = connection.getRatings(quizID);
		ratings.beforeFirst();
		while(ratings.next()) {
			int userID = ratings.getInt("userID");
			int ratingValue = ratings.getInt("ratingValue");
			String review = ratings.getString("ratingReview");
			Rating currentRating = new Rating(quizID, userID, ratingValue, review);
			result.add(currentRating);
		}
		return result;
	}
	/* Returns an arrayList containing all of the ratings associated with this 
	 * user ID.
	 */
	public static ArrayList<Rating> getRatingsByUser(int userID, DBConnection connection) throws SQLException {
		ArrayList<Rating> result = new ArrayList<Rating>();
		ResultSet ratings = connection.getRatingsByUserID(userID);
		ratings.beforeFirst();
		while(ratings.next()) {
			int quizID = ratings.getInt("quizID");
			int ratingValue = ratings.getInt("ratingValue");
			String review = ratings.getString("ratingReview");
			Rating currentRating = new Rating(quizID, userID, ratingValue, review);
			result.add(currentRating);
		}
		return result;
	}
	/* Returns an arrayList of scores associated with this user ID. */
	public static ArrayList<Score> getHistories(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
		ArrayList<Score> result = new ArrayList<Score>();
		ResultSet histories = (userID == null) ? connection.getHistories(quizID) : connection.getHistoriesByUserID(userID);
		histories.beforeFirst();
		while (histories.next()) {
			quizID = histories.getInt("quizID");
			userID = histories.getInt("userID");
			int score = histories.getInt("score");
			java.util.Date date = (java.util.Date) histories.getObject("dateTaken");
			Score currentScore = new Score(quizID, userID, score, date);
			result.add(currentScore);
		}
		return result;
	}
	/* Gets the NUM_RECENT_SCORES number of recent scores associated with this user
	 * ID. Returns an arrayList of the string format specified by Chris.
	 */
	public static ArrayList<String> getRecentScores(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Score> recentScores = new ArrayList<Score>();
		ArrayList<Score> scores = getHistories(userID, quizID, connection);
		for (int i = 0; i < scores.size(); i++) {
			int recentScoreIndex = 0;
			while(recentScoreIndex < recentScores.size() && 
					recentScores.get(recentScoreIndex).getDate().compareTo(scores.get(i).getDate()) > 0) {
				recentScoreIndex++;
			}
			recentScores.add(recentScoreIndex , scores.get(i));
		}
		for (int i = 0; i < recentScores.size(); i++) {
			if (i >= NUM_RECENT_SCORES) break;
			int score = recentScores.get(i).getScore();
			String format = "<td>" + score + "</td><td>" + connection.getQuizName(recentScores.get(i).getQuizID()) + 
			"</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
			userID + "'>" + connection.getUserName(userID) + "</a></td>";
			result.add(format);
		}
		return result;
	}
	/* Gets the NUM_TOP_SCORES number of recent scores associated with this user
	 * ID. Returns an arrayList of the string format specified by Chris.
	 */
	public static ArrayList<String> getTopScores(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> topScores = new ArrayList<Integer>();
		ArrayList<Score> scores = getHistories(userID, quizID, connection);
		for (int i = 0; i < scores.size(); i++) {
			int topScoreIndex = 0;
			while(topScoreIndex < topScores.size() && topScores.get(topScoreIndex) > scores.get(i).getScore()) {
				topScoreIndex++;
			}
			topScores.add(topScoreIndex ,scores.get(i).getScore());
		}
		for (int i = 0; i < topScores.size(); i++) {
			if (i >= NUM_TOP_SCORES) break;
			int score = topScores.get(i);
			String format = "<td>" + score + "</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" 
							+ userID + "'>" + connection.getUserName(userID) + "</a></td>";
			result.add(format);
		}
		return result;
	}
	/* Returns a list of QuizCreation stats based on the userID if it is specified. If the userID is 
	 * 	null, then a list of QuizCreation stats is returned represented all of the quizzes ever created.
	 */
	public static ArrayList<QuizCreation> getQuizzes(Integer userID, DBConnection connection) throws SQLException {
		ArrayList<QuizCreation> result = new ArrayList<QuizCreation>();
		ResultSet quizzes = (userID == null) ? connection.getAllQuizzes() : connection.getQuizzesCreatedByUserID(userID);
		quizzes.beforeFirst();
		while (quizzes.next()) {
			int quizID = quizzes.getInt("quizID");
			userID = quizzes.getInt("quizCreatoruserID");
			java.util.Date createDate = (java.util.Date) quizzes.getObject("quizCreation");
			String quizName = quizzes.getString("quizName");
			QuizCreation currentCreation = new QuizCreation(quizID, userID, createDate, quizName);
			result.add(currentCreation);
		}
		return result;
	}
	
	/* Returns an HTML printable string of recently created quizzes. If a userID is given, then the
	 * 	returned quiz stats will be for that userID. If the userID is null, then this method will just
	 * 	return stats on the most recently created quizzes created by anyone.
	 */
	public static ArrayList<String> getRecentQuizCreations(Integer userID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<QuizCreation> recent = new ArrayList<QuizCreation>();
		ArrayList<QuizCreation> all = getQuizzes(userID, connection);
		for (int i = 0; i < all.size(); i++) {
			int recentIndex = 0;
			while(recentIndex < recent.size() && 
					recent.get(recentIndex).getCreateDate().compareTo(all.get(i).getCreateDate()) > 0) {
				recentIndex++;
			}
			recent.add(recentIndex , all.get(i));
		}
		for (int i = 0; i < recent.size(); i++) {
			if (i >= NUM_RECENT_SCORES) break;
			QuizCreation curr = recent.get(i);
			String format = "<td>" + curr.getQuizName() + "</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" 
					+ curr.getUserID() + "'>" + connection.getUserName(curr.getUserID()) + "</a></td>";
			result.add(format);
		}
		return result;
	}
	/* Returns an HTML printable string of the most popular quizzes in the DB.
	 */
	public static ArrayList<String> getPopularQuizzes(DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet allRatings = connection.getAllRatings();
		allRatings.beforeFirst();
		HashMap<Integer, Integer> popQuizzes = new HashMap<Integer, Integer>();
		while(allRatings.next()) {
			int quizID = allRatings.getInt("quizID");
			if (popQuizzes.containsKey(quizID)) continue;
			int avgRating = getRating(quizID, connection);
			popQuizzes.put(quizID, avgRating);
		}
		ArrayList<Integer> popList = new ArrayList<Integer>();
		Set<Integer> keySet = popQuizzes.keySet();
		for (Integer quizID : keySet) {
			int popIndex = 0;
			while(popIndex < popList.size() && 
					popList.get(popIndex) > popQuizzes.get(quizID)) {
				popIndex++;
			}
			popList.add(popIndex , quizID);
		}
		for (int i = 0; i < popList.size(); i++) {
			if (i >= NUM_POP_SCORES) break;
			int quizID = popList.get(i);
			ResultSet quizInfo = connection.getQuizInformation(quizID);
			String format = "<td>" + quizInfo.getString("quizName") + "</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" 
							+ quizInfo.getInt("quizCreatoruserID") + "'>" + connection.getUserName(quizInfo.getInt("quizCreatoruserID")) + "</a></td>";
			result.add(format);
		}
		return result;
	}
	
	/** Duplicated these methods in case you want to get these when displaying 
	 * 		quiz histories.
	 */
	public static ArrayList<String> getCategories(int quizID, DBConnection connection) throws SQLException {	
		ArrayList<String> result = new ArrayList<String>();
		ResultSet categories = connection.getCategories(quizID);
		categories.beforeFirst();
		while (categories.next()) {
			result.add(categories.getString("categoryName"));
		}
		return result;
	} 
	public static ArrayList<String> getTags(int quizID, DBConnection connection) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet tags = connection.getTags(quizID);
		tags.beforeFirst();
		while(tags.next()) {
			result.add(tags.getString("tagName"));
		}
		return result;
	} 
	public static int getRating(int quizID, DBConnection connection) throws SQLException{	
		int numValues = 0;
		int total = 0;
		ResultSet ratings = connection.getRatings(quizID);
		ratings.beforeFirst();
		while (ratings.next()) {
			total += ratings.getInt("ratingValue");
			numValues++;
		}
		return numValues == 0 ? 0 : total / numValues;
	}
	
}
