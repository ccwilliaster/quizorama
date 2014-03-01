package quiz;

import java.io.IOException;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { /* not implemented */ }

	/**
	 * Directs
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try { 
			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			//DBConnection connection = context.getAttribute("DBConnection");
			//User user               = (User) session.getAttribute("user");
			Integer userID          =  0; //(Integer) user.getUserID;
			String messageType      = request.getParameter("type");
			String hasContent       = request.getParameter("hasContent");
			
			// Three possibilities:
			if (messageType == null) { // get type from user
				
				System.out.println("dropdown");
				String html = getTypeDropDown(true); //TODO: make add announcement option only for admins
				request.setAttribute("html", html);
				request.getRequestDispatcher("createMessage.jsp").forward(request, response);

			} else if (hasContent == null) { // know type, get content from user
				Integer messageIntType = Integer.parseInt(messageType);
				forwardMessageContent(request, response, messageIntType, userID);
				
			} else { // create message with content, re-direct to userMessages.jsp
				Integer messageIntType = Integer.parseInt(messageType);
				forwardCreateMessage(request, response, userID, messageIntType); // TODO add connection arg
			}
			
		} catch (InputMismatchException badinput) { // direct to input form again
			request.setAttribute("error", "<h1>Invalid input, please re-enter</h1>");
			request.getRequestDispatcher("createMessage.jsp").forward(request, response);
			
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
	forwardMessageContent(HttpServletRequest request, HttpServletResponse response,
					 Integer messageType, Integer userID) 
	throws ServletException, IOException {
	    
		String html = "";
		System.out.println("making new message type:" + messageType);
	
		if (messageType == Message.TYPE_NOTE) {
			html = Note.getCreationHTML(userID); // TODO: pass all user names/IDs
			
		} else if (messageType == Message.TYPE_FRIEND) {
			html = FriendRequest.getCreationHTML(userID); // TODO: pass all user names/IDs
			
		} else if (messageType == Message.TYPE_CHALLENGE) {
			html = Challenge.getCreationHTML(userID, null); // TODO: pass all user AND quiz names/IDs
			
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
	 */
	private void 
	forwardCreateMessage(HttpServletRequest request, HttpServletResponse response,
						 Integer userID, Integer messageType) // TODO add DBConnection connection parameter
	throws ServletException, IOException {
		System.out.println("creating message from " + userID + " of type " + messageType);
		
		if (messageType == Message.TYPE_NOTE) {
			//newMessage = makeNote(request, connection);
			request.setAttribute("newMessage", 
								 "Your note was sent successfully");
			
		} else if (messageType == Message.TYPE_FRIEND) {
			System.out.println(request.getParameter("content"));
			//newMessage = new FriendRequest(request, connection);
			request.setAttribute("newMessage", 
					             "Your friend request was sent successfully");
			
		} else if (messageType == Message.TYPE_ANNOUNCEMENT) {
			//int numSent = makeAnnouncements(request, connection);
			request.setAttribute("newMessage", 
             					 "Your announcement was sent successfully");
			
		} else if (messageType == Message.TYPE_CHALLENGE) {
			//newMessage = new Challenge(request, connection);
			request.setAttribute("newMessage", 
			 					 "Your challenge was sent successfully");
			
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		request.getRequestDispatcher("userMessages.jsp").forward(request, response);
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
