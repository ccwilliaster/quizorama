package quiz;

import java.io.IOException;

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
			//DBConnection connection = session.getAttribute("DBConnection");
			//User user = session.getAttribute("user");
			boolean isAdmin = true; //user.isAdmin();
			String navtab = request.getParameter("navtab"); // inbox or sent mail
			
			if (navtab == "inbox" || navtab == null) { // default inbox
				
				
			} else { // sent mail
				
				
			}
			
		} catch (Exception e) {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
		
		// get servletcontext, and User from servletcontext
		// get userID, if admin from User
		// get DBConnection from servletcontext
		//     pass connection to a new function
		//     
		//         request should have a "navtab" attribute = inbox, sent
		//         depending on this attribute, set tab, call server and filter messaagen on
		//             need to get list inbox / toUser 
		//             get list sent / fromUser list
		//             (either needs filtering for type
		//                - std: friend, challenge, note
		//				  - admin: friend, challenge, note, announcements, quiz flag)
		//
		//         sort the returned list of message objects
		//		   display them in a table with links
		//         
		//         (have new method for announcements, but will actually have a lot of redundant to announcements)
		//     (could just re-load every time it's called)
		//    
		//	   (Message should have static utility sort, list function return top n messaegs!)
		
		
		//   call connection.getAllUserMessages()
		//   Messages.loadMessages will parse this
		
		
	}

	
	
	
	
}
