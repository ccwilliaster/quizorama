package quiz;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddTagServlet
 */
@WebServlet("/AddTagServlet")
public class AddTagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTagServlet() {
        super();
        //Do nothing
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Return to the homepage, oops!
		String quizIDText = (String) request.getAttribute("quizID");
		if (quizIDText == null) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
			requestDispatcher.forward(request, response);
			return;
		} //if
		int quizID = Integer.parseInt(quizIDText);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
		requestDispatcher.forward(request, response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Check to see whether we have created a new tag or just added one that already exists.
		// If the former, then add that tag to the tag system before adding it in quizTags, if the latter, add in quizTags
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
		int tagID = Integer.parseInt(request.getParameter("tagAdd"));
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		String newTag = request.getParameter("newTag");
		if ( tagID < 1 && (newTag == null || !newTag.equals(""))) {
			//Need to make a new tag and return the tag ID
			try {
				tagID = dbConnection.addNewTag(newTag);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
				requestDispatcher.forward(request, response);
				return;
			} //catch
		} //if
		else if (tagID < 1 && (newTag == null || newTag.equals(""))) {
			//We have both an empty newTag and no tag selection, so they gave up. Send back to quizSummary.
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
			requestDispatcher.forward(request, response);
			return;
		} //else if
		
		try {
			dbConnection.addTag(quizID, tagID);
		} catch (SQLException e) {
			e.printStackTrace();
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
			requestDispatcher.forward(request, response);
			return;
		} //catch
		
		//Now, we can go back to quizSummary, satisfied
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
		requestDispatcher.forward(request, response);
		return;
	} //doPost

} //addTagServlet
