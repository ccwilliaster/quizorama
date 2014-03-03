package quiz;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class DBConnectionTest {

	private DBConnection dbConnection;
	private int userId;
	private String userName;
	private String dateJoined;
	private boolean passCheck;
	
	@Before
	public void setUp() throws Exception {
		dbConnection = new DBConnection();
	}

	@Test
	public void testDBConnection() {
		ResultSet rs = null;
		try {
			rs = dbConnection.getUser(1);
		} catch (SQLException e2) {
			e2.printStackTrace();
			assertTrue(false); //Fail the test
		}

		try {
			rs.first();
			userId = rs.getInt(1);
			userName = rs.getString("userName");
			dateJoined = rs.getString(3);
		} catch (SQLException e1) {
			e1.printStackTrace();
			assertTrue(false); //Fail the test
		}

		assertEquals(1,userId);
		assertEquals("testUser", userName);
		assertEquals("2014-02-01 00:00:00.0", dateJoined);
		
		boolean passCheck2 = true, passCheck3 = true;
		try {

			String hashPass = PasswordHash.createHash("password");
			dbConnection.setPassword(1, hashPass);
			assertTrue(PasswordHash.validatePassword("password", dbConnection.getPassword(1)));
			hashPass = PasswordHash.createHash("otherpass");
			dbConnection.setPassword(2, hashPass);
			passCheck = PasswordHash.validatePassword("otherpass", dbConnection.getPassword(2));
			passCheck2 = PasswordHash.validatePassword("password", dbConnection.getPassword(2));
			passCheck3 = PasswordHash.validatePassword("otherpas", dbConnection.getPassword(2));
		} catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false); //Fail the test
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			assertTrue(false); //Fail the test
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			assertTrue(false); //Fail the test
		}
		
		assertTrue(passCheck);
		assertFalse(passCheck2);
		assertFalse(passCheck3);
	} //testDBConnection
	
	@Test
	public void testDBConn2() {
		
	} //testDBConn2

}
