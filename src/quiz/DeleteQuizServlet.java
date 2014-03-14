package quiz;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteQuizServlet
 */
@WebServlet("/DeleteQuizServlet")
public class DeleteQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteQuizServlet() {
        super();
    }

	/**
	 * Re-directs to doPost
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try {
			ServletContext context  = getServletContext();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			Integer quizID          = Integer.parseInt( request.getParameter("quizID") );
			String quizName         = connection.getQuizName(quizID);
			
			connection.deleteQuiz( quizID );
			request.setAttribute("alert", "Quiz " + quizName + " deleted successfully");
			request.getRequestDispatcher("quizSearch.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
