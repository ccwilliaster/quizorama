package quiz;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.Test;

public class QuestionResponseQuestionTest {

	@Test
	public void questionTest1() throws SQLException, ClassNotFoundException {
		DBConnection db = new DBConnection();
		try {
			Question q = null;
			//Question q = new QuestionResponseQuestion(1, db);
			assertEquals("<p>What is 1+1?</p>", q.showQuestion());
			assertEquals("<input type='text' name='answer'/>", q.showAnswerOptions());
			ArrayList<String> guesses = new ArrayList<String>();
			guesses.add("2");
			assertEquals(1, q.checkAnswers(guesses));
			assertEquals(1, q.possiblePoints());
			assertEquals("<p>2</p>", q.showAnswer());
			List<String> formNames = new ArrayList<String>(); 
			formNames.add("answer");
			assertEquals(formNames, q.getAnswerLocations());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void questionTest2() throws SQLException, ClassNotFoundException {
		DBConnection db = new DBConnection();
		try {
			Question q = null;
			//Question q = new QuestionResponseQuestion(3, db);
			assertEquals("<p>What is the capital of the United States?</p>", q.showQuestion());
			assertEquals("<input type='text' name='answer'/>", q.showAnswerOptions());
			ArrayList<String> guesses = new ArrayList<String>();
			guesses.add("D.C.");
			assertEquals(1, q.checkAnswers(guesses));
			guesses = new ArrayList<String>();
			guesses.add("DC");
			assertEquals(1, q.checkAnswers(guesses));
			guesses = new ArrayList<String>();
			guesses.add("Washington D.C.");
			guesses.add("Washington DC");
			assertEquals(1, q.checkAnswers(guesses));
			assertEquals(1, q.possiblePoints());
			assertEquals("<p>DC</p>", q.showAnswer());
			List<String> formNames = new ArrayList<String>(); 
			formNames.add("answer");
			assertEquals(formNames, q.getAnswerLocations());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}
