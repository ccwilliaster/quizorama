package quiz;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Handles deleting a message from the database
 */
@WebServlet("/DeleteMessageServlet")
public class DeleteMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteMessageServlet() { super(); }

	/**
	 * Not implemented
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { /* not implemented */ }

	/**
	 * Deletes a message and returns user to userMessages.jsp
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try { 
			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			Integer messageID       = Integer.parseInt( request.getParameter("messageID") );
			Integer userID          = ( (User) session.getAttribute("user") ).getUserID();
			
			// Determine the type of delete update that should occur
			ResultSet rs            = connection.getMessage(messageID);
			rs.first();
			Integer toUserID        = (Integer) rs.getObject("toUserID");
			Integer fromUserID      = (Integer) rs.getObject("fromUserID");
			Boolean toUserDeleted   = (Boolean) rs.getObject("toUserDeleted");
			Boolean fromUserDeleted = (Boolean) rs.getObject("fromUserDeleted");

			if ( toUserID.equals(fromUserID) ) { // user is sender AND receiver
				connection.deleteMessage(messageID);
		    
			} else if ( toUserID.equals(userID) ) { // user is sender
				if ( fromUserDeleted ) { connection.deleteMessage(messageID); }
				else                   { connection.toUserDeleteMessage(messageID); }
			
			} else { // user is receiver
				if ( toUserDeleted ) { connection.deleteMessage(messageID); }
				else                 { connection.fromUserDeleteMessage(messageID); }
			}
			
			request.setAttribute("navtab", "inbox");
			request.setAttribute("messageUpdate", "Message deleted successfully");
			request.getRequestDispatcher("/ReadMessagesServlet").forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
