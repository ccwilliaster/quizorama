package quiz;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RateQuizServlet
 */
@WebServlet("/RateQuizServlet")
public class RateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateQuizServlet() { super(); }

	/**
	 * Forwards to doPost()
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { 
		doPost(request, response); 
	}

	/**
	 * Adds a quiz rating to the database and returns to the 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try{
			System.out.println("in dopost");
			ServletContext context  = getServletContext();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");

			int quizID = Integer.parseInt( request.getParameter("quizID") );
			int userID = Integer.parseInt( request.getParameter("userID") );
			int rating = Integer.parseInt( request.getParameter("rating") );

			Quiz quiz        = new Quiz(quizID, connection);
			Integer ratingID = quiz.getUserRatingID(userID);
			
			// Determine if user has already rated this Quiz
			if (ratingID == null) {
				connection.addQuizRating(quizID, userID, rating);
			} else {
				connection.updateQuizRating(ratingID, rating);
			}

			request.getRequestDispatcher("quizSummary.jsp").forward(request, response);
			
		}  catch (Exception e) {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
