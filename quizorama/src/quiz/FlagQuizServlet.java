package quiz;

import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FlagQuizServlet
 */
@WebServlet("/FlagQuizServlet")
public class FlagQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlagQuizServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try {

			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			User user               = (User) session.getAttribute("user");
			String userName         = user.getUserName();
			int userID              = user.getUserID();
			int quizID              = Integer.parseInt( request.getParameter("quizID") );
			String quizName         = request.getParameter("quizName");
			ResultSet rsAdminIDs    = connection.getAdminUserIDs();

			Integer currAdminID;
			while ( rsAdminIDs.next() ) {
				currAdminID = ( (Number) rsAdminIDs.getObject(1) ).intValue();
				QuizFlag.makeQuizFlag(quizID, quizName, userID, currAdminID, userName, connection);
			}
			
			request.setAttribute("flagNote", "Flagged");
			request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID).forward(request, response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
