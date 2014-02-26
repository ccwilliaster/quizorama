package quizController;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class QuizControllerServlet
 */
@WebServlet("/QuizControllerServlet")
public class QuizControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizControllerServlet() { super(); }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { /* not implemented */ }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		
		// Get Context, HttpSession, and Quiz
		ServletContext context = getServletContext();
		HttpSession session    = request.getSession();
		Quiz currQuiz          = (Quiz) session.getAttribute("quiz");
			
		// First time servlet is being called, initialize Quiz and add to session
		if (currQuiz == null) {
			currQuiz = new Quiz(); // DBConnection.getQuiz( request.getAttribute("quizID") );
			session.setAttribute("quiz", currQuiz);
			currQuiz.startQuiz();
			
		} else { // Submit the results from liveQuiz.jsp
			currQuiz.sendAnswers( request );
		}
		
		String html = currQuiz.getNextHTML();
		
		// no more questions, direct to results summary
		if (html == null) { 
			session.setAttribute("quiz", null);     // remove for next servlet call
			request.setAttribute("quiz", currQuiz); // add for summary display
			
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("quizResults.jsp");
			dispatch.forward(request, response);
		
		} else { // otherwise show Quiz html
			session.setAttribute("html", html);
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("liveQuiz.jsp");
			dispatch.forward(request, response);
		}
	}
}
