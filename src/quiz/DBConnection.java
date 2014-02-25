package quiz;

import java.sql.Connection;
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
	private final String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private final String account = MyDBInfo.MYSQL_USERNAME;
	private final String password = MyDBInfo.MYSQL_PASSWORD;
	private final String database = MyDBInfo.MYSQL_DATABASE_NAME;
	private Connection conn;
		
	public DBConnection() {
    	Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection
					( "jdbc:mysql://" + server, account, password);
			PreparedStatement sql = conn.prepareStatement("USE " + database);
			sql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	} //Constructor
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} //close
	
	public ResultSet getQuizInformation (int quizID) {
		return null;
	} //getQuizInformation

	public ResultSet getQuestionsForQuiz (int quizID) {
		return null;
	} //getQuestionsForQuiz

	public ResultSet getAnswersForQuestion (int questionID) {
		return null;
	} //getAnswersForQuestion

	public ResultSet getMessageList(int userID) {
		return null;
	} //getMessages
	
	public ResultSet getLogonHash(int userID) {
		return null;
	}
	
	public ResultSet getLogonSalt(int userID) {
		return null;
	} //getLogonSalt
	
	public boolean setPassword(int userID, String password) {
		return false;
	} //setPassword
	
	public String getPassword(int userID) {
		return null;
	} //getPassword
	
	/**
	 * This function queries the database to get all of the user messages related to 
	 * a particular user and returns them. Usually called by the Message class when 
	 * displaying a user's mailbox
	 * @param userID User ID for which to return messages
	 * @return ResultSet of all columns and all messages
	 */
	public ResultSet getUserMessages(int userID) {
		return null;
	} //getUserMessages
	
	public void addMessage (Message message) {
		// count on it having getXXX for all attributes in the Message table (I can also fill this in if you add the DBConnection on git) 
	} //addMessage
	public void updateMessage (Message message) {
		// message.getMessageID() for which entry
	} //updateMessage

	public void setUserName(int userID, String userName) {
		//TODO: Update the userName for this user
	}
	
}
