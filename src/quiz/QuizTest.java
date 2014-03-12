package quiz;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

public class QuizTest {
	
	@Test
	public void quiz1Test() throws ClassNotFoundException, SQLException {
		DBConnection db = new DBConnection();
		Quiz q = new Quiz(1, db);
		assertEquals("Test1", q.getQuizName());
	}
	
}
