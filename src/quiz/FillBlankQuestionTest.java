package quiz;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FillBlankQuestionTest {

	@Test
	public void fillBlankTest1() throws SQLException, ClassNotFoundException {
		DBConnection db = new DBConnection();
		try {
			Question q = null;
			//Question q = new FillBlankQuestion(5, db);
			assertEquals("<p>One of President Lincoln's most famous speeches was the _____________ address.</p>", q.showQuestion());
			assertEquals("<input type='text' name='answer'/>", q.showAnswerOptions());
			ArrayList<String> guesses = new ArrayList<String>();
			guesses.add("Gettysburg");
			assertEquals(1, q.checkAnswers(guesses));
			assertEquals(1, q.possiblePoints());
			assertEquals("<p>Gettysburg</p>", q.showAnswer());
			List<String> formNames = new ArrayList<String>(); 
			formNames.add("answer");
			assertEquals(formNames, q.getAnswerLocations());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
