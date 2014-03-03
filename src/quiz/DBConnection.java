package quiz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private final String messagesTable = "message";
	private final String quizTable = "quizzes";
	private final String quizQuestionTable = "quizQuestions";
	private final String questionAnswerTable = "quizAnswers";
	private final String quizHistoryTable = "quizHistory";
	private final String quizTagsTable = "quizTags";
	private final String tagsTable = "tags";
	private final String quizCategoriesTable = "quizCategories";
	private final String categoriesTable = "categories";
	private final String userQuizRatingsTable = "userQuizRatings";
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

	/**
	 * This function queries the database to get all of the user messages related to 
	 * a particular user and returns them. Usually called by the Message class when 
	 * displaying a user's mailbox
	 * @param userID User ID for which to return messages
	 * @return ResultSet of all columns and all messages
	 */
	public ResultSet getUserMessages (int userID) throws SQLException {
		String select = "SELECT * FROM " + messagesTable + " WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(select);
		sql.setInt(1, userID);
		return sql.executeQuery();
	} //getUserMessages

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
		String set = "INSERT INTO " + messagesTable + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
		PreparedStatement sql = conn.prepareStatement(set);
		sql.setInt(1, message.getID());
		sql.setInt(2, message.getType());
		sql.setInt(3, message.getToUserID());
		sql.setInt(4, message.getFromUserID());
		sql.setString(5,message.getSubject());
		sql.setString(6,message.getContent());
		sql.setDate(7, message.getDate());
		sql.setInt(8,message.getReadStatus());
		return sql.execute();
	} //addMessage
	
	public boolean updateMessage (Message message) throws SQLException {
		String set = "UPDATE " + messagesTable + " SET messageID = ?"
				+ ", messageType = ?, toUserID = ?, fromUserID = ?"
				+ ", subject = ?, content = ?, date = ?, messageRead = ?"
				+ " WHERE messageID = ?";
				
		PreparedStatement sql = conn.prepareStatement(set);
		sql.setInt(1, message.getID());
		sql.setInt(2, message.getType());
		sql.setInt(3, message.getToUserID());
		sql.setInt(4, message.getFromUserID());
		sql.setString(5,message.getSubject());
		sql.setString(6,message.getContent());
		sql.setDate(7, message.getDate());
		sql.setInt(8,message.getReadStatus());
		sql.setInt(9, message.getID());
		return sql.execute();
		// message.getMessageID() for which entry
	} //updateMessage

	public boolean setUserName(int userID, String userName) throws SQLException {
		String set = "UPDATE " + userTable + " SET userName = ? "
				+ "WHERE userID = ?";
		PreparedStatement sql = conn.prepareStatement(set);
		sql.setInt(2, userID);
		sql.setString(1, userName);
		return sql.execute();
	}
	
	public boolean addQuizHistory(int quizID, int userID, int score) throws SQLException {
		String set = "INSERT INTO " + quizHistoryTable + " (quizID, userID, dateTaken, score, completed?, timeStamp) VALUES ( ?, ?, ?, ?, ?, ? )";
		PreparedStatement sql = conn.prepareStatement(set);
		java.util.Date utilDate = new java.util.Date();
		Date date = new Date(utilDate.getTime());
		sql.setInt(1, quizID);
		sql.setInt(2, userID);
		sql.setDate(3, date);
		sql.setInt(4, score);
		sql.setInt(5,1); //Completed = true
		sql.setDate(6, date);
		return sql.execute();
	} //addQuizHistory

	public int getUserID(String userName) throws SQLException {
		String userIDGet = "SELECT userID FROM users WHERE userName = ?";
		PreparedStatement sql = conn.prepareStatement(userIDGet);
		sql.setString(1, userName);
		ResultSet rs = sql.executeQuery();
		rs.first();
		return rs.getInt(1);
	} //getUserID

	
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
		PreparedStatement sql = conn.prepareStatement(insert);
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
	
	
}
