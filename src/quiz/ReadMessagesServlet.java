package quiz;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReadMessagesServlet
 */
@WebServlet("/ReadMessagesServlet")
public class ReadMessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * not implemented 
     * @see HttpServlet#HttpServlet()
     */
    public ReadMessagesServlet() { super(); }

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
		try { 
			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			User user = (User) session.getAttribute("user");

			// Filter messages based on user type
			Set<Integer> validTypes = new HashSet<Integer>() {{ 
				add( Message.TYPE_NOTE );
				add( Message.TYPE_FRIEND );
				add( Message.TYPE_CHALLENGE );
			}};
			if (true /* user.isAdmin() */) {
				validTypes.add( Message.TYPE_ANNOUNCEMENT );
				validTypes.add( Message.TYPE_QUIZ_FLAG );
			}
			
			// Fetch messages from DBConnection, then filter based on navtab
			ResultSet allMessages = connection.getUserMessages( user.getUserID() );
			ArrayList<Message> filtMessages;		
			String navtab = request.getParameter("navtab"); // inbox or sent mail
			
			if (navtab == "inbox" || navtab == null) { // default inbox
				request.setAttribute("navtab", "inbox");
				filtMessages = Message.loadMessages(allMessages, validTypes, user.getUserID(), null);
			
			} else { // sent mail
				request.setAttribute("navtab", "sent");
				filtMessages = Message.loadMessages(allMessages, validTypes, null, user.getUserID());	
			}
			Collections.sort( filtMessages ); // sort by date
			request.getRequestDispatcher("userMessages.jsp").forward(request, response);
			
		} catch (Exception e) {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	
	
	
	
}
