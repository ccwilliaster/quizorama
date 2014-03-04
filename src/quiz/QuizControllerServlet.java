package quiz;

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
		
		try { // Get Context, HttpSession, and Quiz
			ServletContext context  = getServletContext();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			HttpSession session     = request.getSession();
			Quiz currQuiz           = (Quiz) session.getAttribute("quiz");
				
			if (currQuiz == null) { // first time servlet is being called
				Integer quizID = Integer.parseInt(request.getParameter("quizID"));
				currQuiz = new Quiz(quizID, connection);
				session.setAttribute("quiz", currQuiz);
				currQuiz.startQuiz();
				
			} else { // Submit the results from previous liveQuiz.jsp
				currQuiz.sendAnswers( request );
			}
			
			if ( currQuiz.hasMoreHTML() ) { // more HMTL to be displayed
				
				String html = currQuiz.getNextHTML();
				request.setAttribute("html", html);
				RequestDispatcher dispatch = 
					request.getRequestDispatcher("liveQuiz.jsp");
				dispatch.forward(request, response);
				
			} else { // direct to results summary
				
				session.setAttribute("quiz", null);     // remove for next servlet call
				request.setAttribute("quiz", currQuiz); // add for summary display
				
				RequestDispatcher dispatch = 
					request.getRequestDispatcher("quizResults.jsp");
				dispatch.forward(request, response);	
			}
			
		} catch (Exception e) {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}
