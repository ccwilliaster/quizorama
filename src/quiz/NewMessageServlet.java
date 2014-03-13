package quiz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class NewMessageServlet
 */
@WebServlet("/NewMessageServlet")
public class NewMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewMessageServlet() { super(); }

	/**
	 * Re-directs to doPost
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {  
		doPost(request, response);
	}

	/**
	 * Directs
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		Integer messageIntType  = null;
		Integer userID          = null;
		DBConnection connection = null;
		
		try { 
			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			connection = (DBConnection) context.getAttribute("DBConnection");
			User user               = (User) session.getAttribute("user");
			userID                  = user.getUserID();
			String messageType      = request.getParameter("type");
			String hasContent       = request.getParameter("hasContent");
						
			// Three possibilities:
			if (messageType == null) { // get type from user
				
				String html = getTypeDropDown(true); 
				request.setAttribute("html", html);
				request.getRequestDispatcher("createMessage.jsp").forward(request, response);

			} else if (hasContent == null) { // know type, get content from user
				messageIntType = Integer.parseInt(messageType);
				gotoGetMessageContent(request, response, messageIntType, userID, connection);
				
			} else { // create message with content, re-direct to userMessages.jsp
				messageIntType = Integer.parseInt(messageType);
				gotoCreateMessage(request, response, userID, messageIntType, connection);
			}
		
		} catch (InputMismatchException badinput) { // direct to input form again
			try {
				request.setAttribute("error", 
									 "<h3 style='color:#d9534f'>Invalid input, please re-enter</h3>");
				gotoGetMessageContent(request, response, messageIntType, userID, connection);
			
			} catch (Exception e) { 
				e.printStackTrace();
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}
		} catch (Exception e) { 
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
	
	/**
	 * Helper method to display correct type of HTML Message input for user,
	 * redirects to createMessage.jsp
	 */
	private void 
	gotoGetMessageContent(HttpServletRequest request, HttpServletResponse response,
					 Integer messageType, Integer userID, DBConnection connection) 
	throws ServletException, IOException, SQLException {
	    
		String html = "";
	
		if (messageType == Message.TYPE_NOTE) {
			html = Note.getCreationHTML(userID, request); 
			
		} else if (messageType == Message.TYPE_FRIEND) {
			html = FriendRequest.getCreationHTML(userID, connection); 
			
		} else if (messageType == Message.TYPE_CHALLENGE) {
			html = Challenge.getCreationHTML(userID, null); 
			
		} else if (messageType == Message.TYPE_ANNOUNCEMENT) {
			html = Announcement.getCreationHTML(userID);
			
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("html", html);
		request.getRequestDispatcher("createMessage.jsp").forward(request, response);
	}
	
	/**
	 * Helper method to create correct type of Message, redirects to userMessages.jsp
	 * Catches bad input by throwing InputMismatchException
	 */
	private void 
	gotoCreateMessage(HttpServletRequest request, HttpServletResponse response,
						 Integer userID, Integer messageType, DBConnection connection)
	throws ServletException, IOException, SQLException, InputMismatchException {
		
		if (messageType == Message.TYPE_NOTE) {
			Note.makeNote(request, connection);
			request.setAttribute("messageUpdate", 
								 "Your note was sent successfully");
			
		} else if (messageType == Message.TYPE_FRIEND) {
			FriendRequest.makeFriendRequest(request, connection);
			request.setAttribute("messageUpdate", 
					             "Your friend request was sent successfully");
			
		} else if (messageType == Message.TYPE_ANNOUNCEMENT) {
			int numSent = Announcement.makeAnnouncements(request, connection);
			request.setAttribute("messageUpdate", 
             					 "Announcement successfully sent to " + numSent + " users");
			
		} else if (messageType == Message.TYPE_CHALLENGE) {
			Challenge.makeChallenge(request, connection);
			request.setAttribute("messageUpdate", 
			 					 "Your challenge was sent successfully");
			
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		
		request.getRequestDispatcher("/ReadMessagesServlet").forward(request, response);
	}
	
	/**
	 * Helper method to construct a dropdown message to select new message type.
	 * If the user is an admin, he/she can also create announcements
	 * @param isAdmin whether the user has admin privileges
	 */
	private String getTypeDropDown(boolean isAdmin) {
		StringBuilder dropdown = new StringBuilder();
		dropdown.append(
				"<div class=\"form-group\">" +
			      "<label for=\"1\">New message type</label><br>" +
				  "<div class=\"row\">" +
			        "<div class=\"col-md-4\">" +
 			   	      "<select class=\"form-control\" id=\"1\" name=\"type\">" + 
		 		        "<option value=" + Message.TYPE_NOTE + ">Note</option>" +
		 		        "<option value=" + Message.TYPE_FRIEND + ">Friend Request</option>" +
		 		        "<option value=" + Message.TYPE_CHALLENGE + ">Quiz Challenge</option>"
		);
		
		if (isAdmin) { // Admin can also create announcements
			dropdown.append("<option value=" + 
							Message.TYPE_ANNOUNCEMENT + ">Site Announcement</option>");
		}
		dropdown.append("</select></div></div>");
		
		return dropdown.toString();
	}
	
}
