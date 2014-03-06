package quiz;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MultipleChoiceQuestionTest {

	@Test
	public void multipleChoiceTest1() throws SQLException, ClassNotFoundException {
		DBConnection db = new DBConnection();
		try {
			Question q = null;
			//Question q = new MultipleChoiceQuestion(6, db);
			assertEquals("<p>How many continents are there?</p>", q.showQuestion());
			System.out.println(q.showAnswerOptions());
			
			//assertEquals("<input type='radio' name='choices'/>1", q.showAnswerOptions());
			ArrayList<String> guesses = new ArrayList<String>();
			guesses.add("7");
			assertEquals(1, q.checkAnswers(guesses));
			assertEquals(1, q.possiblePoints());
			assertEquals("<p>7</p>", q.showAnswer());
			List<String> formNames = new ArrayList<String>(); 
			formNames.add("choices");
			assertEquals(formNames, q.getAnswerLocations());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
