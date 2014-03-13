package quiz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class controls the connection from the quiz website to the database
 * This includes encapsulation of database calls as well as knowledge of database to 
 * point to, as well as SQL to execute. Creates an interface for other classes in this
 * package to call for specific information from the database and get result sets directly
 * This class also performs all of the security necessary for each call
 * @author aspanu
 *
 */
public class DBConnection {
	private final String db_server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private final String db_account = MyDBInfo.MYSQL_USERNAME;
	private final String db_password = MyDBInfo.MYSQL_PASSWORD;
	private final String db_database = MyDBInfo.MYSQL_DATABASE_NAME;
	private final String userTable = "users";
	private final String messagesTable = "messages";
	private final String quizTable = "quizzes";
	private final String quizQuestionTable = "quizQuestions";
	private final String questionAnswerTable = "quizAnswers";
	private final String quizHistoryTable = "quizHistory";
	private final String quizTagsTable = "quizTags";
	private final String tagsTable = "tags";
	private final String quizCategoriesTable = "quizCategories";
	private final String categoriesTable = "categories";
	private final String userQuizRatingsTable = "userQuizRatings";
	private final String friendshipTable = "friendships";
	private final String userAchievementsTable = "userAchievements";
	private Connection conn;
		
	public DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		
		conn = DriverManager.getConnection
				( "jdbc:mysql://" + db_server, db_account, db_password);
		PreparedStatement sql = conn.prepareStatement("USE " + db_database);
		sql.execute();
	} //Constructor
	
	public void close() throws SQLException {
		conn.close();
	} //close
	
	public ResultSet getQuizInformation (int quizID) throws SQLException {
		String select = "SELECT * FROM " + quizTable + " WHERE quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();
	} //getQuizInformation

	public ResultSet getQuizQuestions (int quizID) throws SQLException {
		String select = "SELECT * FROM " + quizQuestionTable + " WHERE quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();
	} //getQuizQuestions

	public ResultSet getAnswersForQuestion (int questionID) throws SQLException {
		String select = "SELECT * FROM " + questionAnswerTable + " WHERE questionID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, questionID);
		return sql.executeQuery();
	} //getAnswersForQuestion

	public boolean deleteQuiz(int quizID) throws SQLException {
		String delete = 
		"DELETE " +
		"quizzes, quizHistory, scores, userQuizRatings, quizTags, quizCategories, " +
		"quizAnswers, quizQuestions FROM " +
		"quizzes LEFT JOIN quizHistory     on quizzes.quizID = quizHistory.quizID" +
		 	   " LEFT JOIN scores          on quizzes.quizID = scores.quizID" +
			   " LEFT JOIN userQuizRatings on quizzes.quizID = userQuizRatings.quizID" +
			   " LEFT JOIN quizTags        on quizzes.quizID = quizTags.quizID" +
			   " LEFT JOIN quizCategories  on quizzes.quizID = quizCategories.quizID" +
			   " LEFT JOIN quizAnswers     on quizzes.quizID = quizAnswers.quizID" +
			   " LEFT JOIN quizQuestions   on quizzes.quizID = quizQuestions.quizID" +
		 " WHERE quizzes.quizID = ?";
		
		PreparedStatement sql = conn.prepareStatement(delete);
		sql.setInt(1, quizID);
		return sql.execute();
	} // deletes all entries in the DB which contain a reference to this quizID
	
	/**
	 * This function queries the database to get all of the user messages related to 
	 * a particular user and returns them. Usually called by the Message class when 
	 * displaying a user's mailbox
	 * @param userID User ID for which to return messages
	 * @return ResultSet of all columns and all messages
	 */
	public ResultSet getUserMessages (int userID) throws SQLException {
		String select = "SELECT * FROM " + messagesTable + " WHERE toUserID = ? OR fromUserID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, userID);
		sql.setInt(2, userID);
		return sql.executeQuery();
	} //getUserMessages

	/**
	 * Returns the messageTable entry for a specified messageID
	 */
	public ResultSet getMessage(int messageID) throws SQLException {
		String select = "SELECT * FROM " + messagesTable + " WHERE messageID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, messageID);
		return sql.executeQuery();
	}
	
	public boolean setPassword(int userID, String password) throws SQLException {
		String passwordSet = "UPDATE " + userTable + " SET password = ? "
				+ "WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(passwordSet);
		sql.setInt(2, userID);
		sql.setString(1, password);
		return sql.execute();
	} //setPassword
	
	public String getPassword(int userID) throws SQLException {
		String passwordGet = "SELECT password FORM users WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(passwordGet);
		sql.setInt(1, userID);
		ResultSet rs = sql.executeQuery();
		if (!rs.first())
			return "";
		return rs.getString(1);
	} //getPassword
	
	public String getPassword(String userName) throws SQLException {
		String userIDGet = "SELECT userID FROM users WHERE userName = ?";
		PreparedStatement sql = conn.prepareStatement(userIDGet);
		sql.setString(1, userName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		int userID = rs.getInt(1);
		
		String passwordGet = "SELECT password FROM users WHERE userID = ?";
		sql = conn.prepareStatement(passwordGet);
		sql.setInt(1, userID);
		rs = sql.executeQuery();
		if (!rs.first())
			return "";
		return rs.getString(1);
	} //getPassword

	
	public boolean addMessage (Message message) throws SQLException {
		String set = "INSERT INTO " + messagesTable + 
			"(messageType, toUserID, fromUserID, subject, content, date," + 
			" messageRead, toUserDeleted, fromUserDeleted)" +
			" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		PreparedStatement sql = conn.prepareStatement(set);
		
		sql.setInt(1, message.getType());
		sql.setInt(2, message.getToUserID());
		sql.setInt(3, message.getFromUserID());
		sql.setString(4,message.getSubject());
		sql.setString(5,message.getContent());
		sql.setTimestamp(6, new java.sql.Timestamp( message.getDate().getTime() ) );
		sql.setBoolean(7, message.getReadStatus());
		sql.setBoolean(8, false);
		sql.setBoolean(9, false);
		return sql.execute();
	} //addMessage
	
	/**
	 * Updates message to reflect that the message receiver deleted the message
	 */
	public boolean toUserDeleteMessage(Integer messageID) throws SQLException { 
		String update = "UPDATE " + messagesTable + " SET toUserDeleted = ? " + 
						"WHERE messageID = ?";
		
		PreparedStatement sql = conn.prepareStatement(update);
		sql.setBoolean(1, true);
		sql.setInt(2, messageID);
		
		return sql.execute();
	} //toUserDeleteMessage
	
	/**
	 * Updates message to reflect that the message sender deleted the message
	 */
	public boolean fromUserDeleteMessage(Integer messageID) throws SQLException { 
		String update = "UPDATE " + messagesTable + " SET fromUserDeleted = ? " + 
						"WHERE messageID = ?";
		
		PreparedStatement sql = conn.prepareStatement(update);
		sql.setBoolean(1, true);
		sql.setInt(2, messageID);
		
		return sql.execute();
	} //toUserDeleteMessage
	
	/**
	 * Deletes the entry for this messageID in messagesTable
	 */
	public boolean deleteMessage(Integer messageID) throws SQLException { 
		String delete = "DELETE FROM " + messagesTable + " WHERE messageID = ?";
		
		PreparedStatement sql = conn.prepareStatement(delete);
		sql.setInt(1, messageID);

		return sql.execute();
	} // deleteMessage
	
	/**
	 * Updates DB to reflect that this message has been read
	 */
	public boolean readMessage (Integer messageID) throws SQLException {
		String set = "UPDATE " + messagesTable + " SET messageRead = ?"
				  + " WHERE messageID = ?";

		PreparedStatement sql = conn.prepareStatement(set);
		sql.setBoolean(1, true);
		sql.setInt(2, messageID);
		
		return sql.execute();
	} //readMessage

	public boolean setUserName(int userID, String userName) throws SQLException {
		String set = "UPDATE " + userTable + " SET userName = ? "
				+ "WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(set);
		sql.setInt(2, userID);
		sql.setString(1, userName);
		return sql.execute();
	}
	
	public int addQuizHistory(int quizID, int userID, int score) throws SQLException {
		String set = "INSERT INTO " + quizHistoryTable + 
			" (quizID, userID, dateTaken, score, `completed?`, timeStamp) VALUES ( ?, ?, ?, ?, ?, ? )";
		PreparedStatement sql = conn.prepareStatement(set);
		java.util.Date utilDate = new java.util.Date();
		Date date = new Date(utilDate.getTime());
		sql.setInt(1, quizID);
		sql.setInt(2, userID);
		sql.setDate(3, date);
		sql.setInt(4, score);
		sql.setInt(5,1); //Completed = true
		sql.setDate(6, date);
		int success = sql.executeUpdate();
		return success;
	} //addQuizHistory

	public boolean isValidUserName(String userName) throws SQLException {
		String userNameGet = "SELECT COUNT(*) FROM " + userTable + " WHERE userName = ?";
		PreparedStatement sql = conn.prepareStatement(userNameGet);
		sql.setString(1, userName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getInt(1) > 0;
	} // returns whether the specified userName is valid
	
	public boolean isValidQuizName(String quizName) throws SQLException {
		String quizNameGet = "SELECT COUNT(*) FROM " + quizTable + " WHERE quizName = ?";
		PreparedStatement sql = conn.prepareStatement(quizNameGet);
		sql.setString(1, quizName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getInt(1) > 0;
	} // returns whether the specified quizName is valid
	
	public int getUserID(String userName) throws SQLException {
		String userIDGet = "SELECT userID FROM users WHERE userName = ?";
		PreparedStatement sql = conn.prepareStatement(userIDGet);
		sql.setString(1, userName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getInt(1);
	} //getUserID

	public int getQuizID(String quizName) throws SQLException {
		String userIDGet = "SELECT quizID FROM " + quizTable + " WHERE quizName = ?";
		PreparedStatement sql = conn.prepareStatement(userIDGet);
		sql.setString(1, quizName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getInt(1);
	} // returns the corresponding quizID for the specified quizName
	
	public String getQuizName(int quizID) throws SQLException {
		String userIDGet = "SELECT quizID FROM " + quizTable + " WHERE quizID = ?";
		PreparedStatement sql = conn.prepareStatement(userIDGet);
		sql.setInt(1, quizID);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getString(1);
	} // returns the corresponding quizID for the specified quizName
	
	public String getUserName(int userID) throws SQLException {
		String userNameGet = "SELECT userName FROM users WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(userNameGet);
		sql.setInt(1, userID);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getString(1);
	} // getUserName
	
	/**
	 * Returns a result set of all userIDs which are of the type User.TYPE_ADMIN
	 */
	public ResultSet getAdminUserIDs() throws SQLException {
		String adminGet     = "SELECT userID FROM types INNER JOIN userTypes" +
		 					  " using (typeID) WHERE typeID = ?";
		
		PreparedStatement sql = conn.prepareStatement(adminGet);
		sql.setInt(1, User.TYPE_ADMIN);
		return sql.executeQuery();
	}
	/**
	 * Creates a friend relationship in the database
	 */
	public boolean makeFriendship(int userID1, int userID2) throws SQLException {
		String addOneFriend = 
			"INSERT INTO " + friendshipTable + " (userID, frienduserID) VALUES (?, ?)";
		
		// Add two rows for each friendship
		PreparedStatement sqlFriendOne = conn.prepareStatement(addOneFriend);
		PreparedStatement sqlFriendTwo = conn.prepareStatement(addOneFriend);
		sqlFriendOne.setInt(1, userID1);
		sqlFriendOne.setInt(2, userID2);
		sqlFriendTwo.setInt(1, userID2);
		sqlFriendTwo.setInt(2, userID1);
		return sqlFriendOne.execute() & sqlFriendTwo.execute();
	}
	/**
	 * Removes a friend relationship in the database
	 */
	public boolean removeFriendship(int userID1, int userID2) throws SQLException {
		String removeOneFriend = "DELETE FROM " + friendshipTable + " WHERE userID = ?";
		
		// Remove two rows for each friendship
		PreparedStatement sqlFriendOne = conn.prepareStatement(removeOneFriend);
		PreparedStatement sqlFriendTwo = conn.prepareStatement(removeOneFriend);
		sqlFriendOne.setInt(1, userID1);
		sqlFriendTwo.setInt(1, userID2);
		return sqlFriendOne.execute() & sqlFriendTwo.execute();
	}
	/**
	 * Returns whether two user's are friends or not
	 * @throws SQLException 
	 */
	public boolean usersAreFriends(int userID1, int userID2) throws SQLException {
		String friendGet = "SELECT COUNT(*) FROM " + friendshipTable + " WHERE userID = ?";
		
		// Two entries per friendship
		PreparedStatement friend1 = conn.prepareStatement(friendGet);
		PreparedStatement friend2 = conn.prepareStatement(friendGet);
		friend1.setInt(1, userID1);
		friend2.setInt(1, userID2);
		ResultSet rs1 = friend1.executeQuery();
		ResultSet rs2 = friend2.executeQuery();
		rs1.first();
		rs2.first();
		return ( rs1.getInt(1) + rs2.getInt(1) ) > 1;
	}
	
	public ResultSet getUser(int userID) throws SQLException {
		String passwordGet = "select * from " + userTable + " where userID = ?;";
		PreparedStatement sql = conn.prepareStatement(passwordGet);
		sql.setInt(1, userID);
		return sql.executeQuery();
	} //getUser

	public ResultSet getAllUsers() throws SQLException {
		String passwordGet = "select * from " + userTable + " ;";
		PreparedStatement sql = conn.prepareStatement(passwordGet);
		ResultSet rs = sql.executeQuery();
		return rs;
	} //getAllUsers
	
	public int createUser(String userName, String password) throws SQLException {
		ResultSet genKey = null;
		String insert = "insert into " + userTable + " (userName, password) VALUES (?, ?);";
		PreparedStatement sql = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		sql.setString(1, userName);
		sql.setString(2, password);
		int affectedRows = sql.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
	    }
		
		genKey = sql.getGeneratedKeys();
		if (!genKey.first())
			throw new SQLException("Creating user failed, no gen key obtained.");			
		return genKey.getInt(1);
	} //createUser

	public ResultSet getHistories(int quizID) throws SQLException {
		String select = "SELECT * FROM " + quizHistoryTable + " WHERE quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();		
	} //getHistories
	
	public ResultSet getTagTypes() throws SQLException {
		String select = "SELECT * FROM " + tagsTable + ";";
		PreparedStatement sql = conn.prepareStatement(select);
		return sql.executeQuery();
	} //getTagTypes
	
	public ResultSet getCategoryTypes() throws SQLException {
		String select = "SELECT * FROM " + categoriesTable + ";";
		PreparedStatement sql = conn.prepareStatement(select);
		return sql.executeQuery();
	} //getCategoriesTypes
	
	public ResultSet searchForQuiz(String quizFilter, String tagFilter, String catFilter) throws SQLException {
		String tagAddOn = "";
		String catAddOn = "";
		if (tagFilter == null || tagFilter.equals("")) {
			tagFilter = "%";
			tagAddOn = "or b.tagID is null ";
		} //if
		if (catFilter == null || catFilter.equals("")) {
			catFilter = "%";
			catAddOn = "or c.categoryID is null ";
		} //if
		if (quizFilter == null || quizFilter.equals("")) {
			quizFilter = "%";
		}
		
		String select = "SELECT * FROM " + quizTable + " a LEFT JOIN " + quizTagsTable + " b"
				+ " ON a.quizID = b.quizID LEFT JOIN " + quizCategoriesTable + " c ON a.quizID = c.quizID "
				+ "WHERE a.quizName like ? AND (b.tagID like ? + " + tagAddOn + ") AND ( c.categoryID like ? "
				+ catAddOn + ");";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setString(1, quizFilter);
		sql.setString(2, tagFilter);
		sql.setString(3, catFilter);
		return sql.executeQuery();		
	} //searchForQuiz
	
	public ResultSet searchForUser(String userFilter) throws SQLException {
		String select = "SELECT * FROM " + userTable + " WHERE userName like ?;";
		PreparedStatement sql = conn.prepareStatement(select);
		userFilter = "%" + userFilter + "%";
		sql.setString(1, userFilter);
		return sql.executeQuery();
	} //searchForUser
	
	public ResultSet getTags(int quizID) throws SQLException {
		String select = "SELECT a.quizID, a.quizName, c.tagName FROM " + quizTable + " a LEFT JOIN "
			+ quizTagsTable + " b ON a.quizID = b.quizID LEFT JOIN "
			+ tagsTable + " c ON b.tagID = c.tagID WHERE a.quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();
	} //getTags
	
	public ResultSet getCategories(int quizID) throws SQLException {
		//returns the category name
		String select = "SELECT a.quizID, a.quizName, c.categoryName FROM " + quizTable + " a LEFT JOIN "
			+ quizCategoriesTable + " b ON a.quizID = b.quizID LEFT JOIN "
			+ categoriesTable + " c ON b.categoryID = c.categoryID WHERE a.quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();	
	} //getCategories
	
	public ResultSet getRatings(int quizID) throws SQLException {
		//returns a resultSet of all the ratings so that I can find the average rating value and the different ratingReviews.
		String select = "SELECT * FROM " + userQuizRatingsTable + " WHERE quizID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, quizID);
		return sql.executeQuery();
	} //getRatings

	public ResultSet getAnswerInfo(int questionId) throws SQLException {
		String select = "SELECT * FROM " + questionAnswerTable + " WHERE questionID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, questionId);
		return sql.executeQuery();
	} //getAnswerInfo
	
	public ResultSet getAllQuizzes() throws SQLException {
		String select = "SELECT * FROM " + quizTable + ";";
		PreparedStatement sql = conn.prepareStatement(select);
		return sql.executeQuery();
	} //getAllQuizzes

	public void addQuiz(Quiz quiz) throws SQLException {
		ResultSet genKey = null;
		String insert = "insert into " + quizTable + " (quizName, quizCreatorUserID, `singlePage?`, `randomOrder?`, `immediateCorrection?`, `practiceMode?`) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement sql = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		sql.setString(1, quiz.getQuizName());
		sql.setInt(2, quiz.getquizCreatoruserID());
		sql.setBoolean(3, quiz.getSinglePage());
		sql.setBoolean(4, quiz.getRandomOrder());
		sql.setBoolean(5, quiz.getImmediateCorrection());
		sql.setBoolean(6, quiz.getPractiveMode());
		int affectedRows = sql.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Adding quiz failed, no rows affected.");
	    }
		
		genKey = sql.getGeneratedKeys();
		if (!genKey.first())
			throw new SQLException("Adding quiz failed, no gen key obtained.");
		
		quiz.setQuizID(genKey.getInt(1)); //Add the quizID to this new quiz that we have
	} //addQuiz

	public int addQuestion(String questionText, int qType,
			int nextQuestionNum, int quizID) throws SQLException {
		ResultSet genKey = null;
		String insert = "insert into " + quizQuestionTable + " (quizID, questionTypeID, question, questionNumber) VALUES (?, ?, ?, ?);";
		PreparedStatement sql = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		sql.setInt(1, quizID);
		sql.setString(3, questionText);
		sql.setInt(2, qType);
		sql.setInt(4, nextQuestionNum);
		int affectedRows = sql.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Adding quiz failed, no rows affected.");
	    }
		
		genKey = sql.getGeneratedKeys();
		if (!genKey.first())
			throw new SQLException("Adding quiz failed, no gen key obtained.");
		return genKey.getInt(1);
	} //addQuestion
	
	public int addAnswer(String answerText, int quizID, int questionID) throws SQLException {
		ResultSet genKey = null;
		String insert = "insert into " + quizQuestionTable + " (questionID, quizID, answer) VALUES (?, ?, ?);";
		PreparedStatement sql = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		sql.setInt(1, questionID);
		sql.setInt(2, quizID);
		sql.setString(3, answerText);
		int affectedRows = sql.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Adding quiz failed, no rows affected.");
	    }
		
		genKey = sql.getGeneratedKeys();
		if (!genKey.first())
			throw new SQLException("Adding quiz failed, no gen key obtained.");
		return genKey.getInt(1);
	} //addAnswer
	
	public ResultSet getRatingsByUserID(int userID) throws SQLException {
        String select = "SELECT * FROM " + userQuizRatingsTable + " WHERE userID = ?";
        PreparedStatement sql = conn.prepareStatement(select);
        sql.setInt(1, userID);
        return sql.executeQuery();
    } //getRatingsByUserID

	public ResultSet getHistoriesByUserID(int userID) throws SQLException {
        String select = "SELECT * FROM " + quizHistoryTable + " WHERE userID = ?";
        PreparedStatement sql = conn.prepareStatement(select);
        sql.setInt(1, userID);
        return sql.executeQuery();        
    } //getHistoriesByUserID	

	public boolean addQuizRating(int quizID, int userID, int rating) 
	throws SQLException {
		String set = "INSERT INTO " + userQuizRatingsTable + 
			         " (userID, quizID, ratingValue) VALUES ( ?, ?, ?)";
		
		PreparedStatement sql = conn.prepareStatement(set);
		sql.setInt(1, userID);
		sql.setInt(2, quizID);
		sql.setInt(3, rating);

		return sql.execute();
	} //addQuizRating
	
	public boolean updateQuizRating(int ratingID, int rating) 
	throws SQLException {
		// Updates the ratingValue of the rating with ID ratingID
		String update = "UPDATE " + userQuizRatingsTable + 
		 			    " SET ratingValue = ? WHERE ratingID = ?";
		
		PreparedStatement sql = conn.prepareStatement(update);
		sql.setInt(1, rating);
		sql.setInt(2, ratingID);

		return sql.execute();
	} //updateQuizRating
	public ResultSet getQuizzesCreatedByUserID(int userID) throws SQLException {
		String select = "SELECT * FROM " + quizTable + " WHERE quizCreatoruserID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, userID);
		return sql.executeQuery();		
	} 
	public ResultSet getAllRatings() throws SQLException {
		String select = "SELECT * FROM " + userQuizRatingsTable;
		PreparedStatement sql = conn.prepareStatement(select);
		return sql.executeQuery();
	} 
	private static final int NUM_RECENT_MESSAGES = 3;
	public ArrayList<Message> getRecentMessages(int userID) throws SQLException{
		ResultSet rs = getUserMessages(userID);
		ArrayList<Message> messages = Message.loadMessages(rs, null, userID, null);
		ArrayList<Message> recent = new ArrayList<Message>();
		ArrayList<Message> result = new ArrayList<Message>();
		for (int i = 0; i < messages.size(); i++) {
			int recentIndex = 0;
			while(recentIndex < recent.size() && 
					recent.get(recentIndex).getDate().compareTo(messages.get(i).getDate()) < 0) {
				recentIndex++;
			}
			recent.add(recentIndex , messages.get(i));
		}
		for (int i = 0; i < recent.size(); i++) {
			if (i >= NUM_RECENT_MESSAGES) break;
			result.add(recent.get(i));
		}
		return result;
	}
	public ArrayList<Integer> getFriends(int userID) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ResultSet rs = getAllUsers();
		while (rs.next()) {
			int possibleFriendID = rs.getInt("userID");
			if (possibleFriendID != userID && usersAreFriends(userID, possibleFriendID)) {
				result.add(possibleFriendID);
			}
		}
		return result;
	} //getFriends
	
	public ResultSet getUserAchievements(int userID) throws SQLException {
		String select = "SELECT * FROM " + userAchievementsTable + " WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, userID);
		ResultSet rs = sql.executeQuery();
		return rs;
	} //getUserAchievements
	
	public boolean setAchievement(int userID, int achievementID, String description) throws SQLException{
		ResultSet genKey = null;
		String insert = "insert into " + userAchievementsTable + " (userID, achievementID) VALUES (?, ?);";
		PreparedStatement sql = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		sql.setInt(1, userID);
		sql.setInt(2, achievementID);
		int affectedRows = sql.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Creating achievement failed, no rows affected.");
	    }
		
		genKey = sql.getGeneratedKeys();
		if (!genKey.first())
			throw new SQLException("Creating achievement failed, no gen key obtained.");			
		return true;
	}

	public int getUserType(int userID) throws SQLException {
			String userNameGet = "SELECT userType FROM users WHERE userID = ?";
			PreparedStatement sql = conn.prepareStatement(userNameGet);
			sql.setInt(1, userID);
			ResultSet rs = sql.executeQuery();
			rs.first();
			return rs.getInt(1);
	}
}
