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
	
}
