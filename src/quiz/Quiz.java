package quiz;

import javax.servlet.http.HttpServletRequest;

/**
 * Fake Quiz class for testing QuizControllerServlet
 */
public class Quiz {
	private int ct;
	
	public Quiz() { ct = 0; }
	
	public void startQuiz() { return; }
	
	public String getName() { return "test quiz"; }
	
	public boolean hasMoreHTML() {
		if (ct > 1) return false;
		return true;
	}
	
	public String getNextHTML() {
		if (ct == 0) { 
			return "first question";
		} else if (ct == 1) {
			return "last question (also second)";
		} else {
			return "error! should not be called";
		}	
	}
	
	public void sendAnswers( HttpServletRequest request ) { ct++; }
	
}