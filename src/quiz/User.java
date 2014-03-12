package quiz;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

/**
 * This class is used to store information about the user for the quiz website
 * It will mainly be used to store identifying information to prevent a db
 * callback for every single user-related query.
 * @author aspanu
 *
 */
public class User {
	public final static int TYPE_ADMIN = 0;
	public final static int TYPE_USER = 1;
	private int userID;
	private String userName;
	DBConnection dbConnection;
	
	public User() {
		userID = -1;
		userName = "";
		dbConnection = null;
	} //Constructor
	
	public User(int userID, String userName, DBConnection dbConnection) {
		this.userID = userID;
		this.userName = userName;
		this.dbConnection = dbConnection;
	} //Constructor
	
	public int getUserID() {
		return userID;
	} //getUserID
	
	public String getUserName() {
		return userName;
	} //getUserName
	
	public void setUserID(int userID) {
		this.userID = userID;
	} //setUserName

	public void setUserName(String userName) throws SQLException {
		this.userName = userName;
		if (dbConnection == null)
			return;
		dbConnection.setUserName(userID, userName); //Get it into the db as well
	} //setUserName
	
	public boolean setPassword(String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (dbConnection == null)
			return false; //Didn't succeed
		String passwordHash;
		passwordHash = PasswordHash.createHash(password);
		dbConnection.setPassword(userID, passwordHash);
		
		return true;
	} //setPassword
	
	public boolean checkPassword(String password) throws SQLException {
		String correctHash = dbConnection.getPassword(userID);
		
		try {
			return PasswordHash.validatePassword(password, correctHash);
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (InvalidKeySpecException e) {
			return false;
		}
	} //checkPassword
	
} //User
