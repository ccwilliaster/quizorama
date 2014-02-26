package quizController;

import javax.servlet.http.HttpServletRequest;

/**
 * Class only for testing
 */
public class Quiz {
	private int ct;
	
	public Quiz() { ct = 0; }
	
	public void startQuiz() { return; }
	
	public String getName() { return "test quiz"; }
	
	public String getNextHTML() {
		if (ct == 0) { 
			return "first question";
		} else if (ct == 1) {
			return "last question (also second)";
		} else {
			return null;
		}	
	}
	
	public void sendAnswers( HttpServletRequest request ) { ct++; }
	
}