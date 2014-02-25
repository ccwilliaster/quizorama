package quiz;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User newUser;
	private User filledInUser;

	
	@Before
	public void setUp() throws Exception {
		newUser = new User();
		String userName = "FilledInUser";
		DBConnection dbConnection = null;
		filledInUser = new User(1,userName,dbConnection);
		String password = "Whatup";
		filledInUser.setPassword(password);
	}

	@Test
	public void testUser() {
		assertEquals(1,filledInUser.getUserID());
		assertEquals(-1,newUser.getUserID());
		assertEquals("FilledInUser", filledInUser.getUserName());
		filledInUser.setUserName("Yes");
		assertEquals("Yes", filledInUser.getUserName());
	}

}
