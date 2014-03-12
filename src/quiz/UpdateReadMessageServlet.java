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
 * Servlet implementation class UpdateReadMessageServlet
 */
@WebServlet("/UpdateReadMessageServlet")
public class UpdateReadMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateReadMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Updates a message as having been read, if the current user did not send the
	 * message (the concept of 'new message' should only apply to the inbox)
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		try {
			ServletContext context  = getServletContext();
			HttpSession session     = request.getSession();
			DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
			Integer messageID       = Integer.parseInt( request.getParameter("messageID") );
			Integer fromUserID      =  Integer.parseInt( request.getParameter("fromUserID") );
			Integer requestUserID   = ( (User) session.getAttribute("user") ).getUserID();
			
			if (fromUserID != requestUserID) { // reading a message only applies to recipients
				connection.readMessage(messageID);
			}
			
		} catch (Exception ignored) {}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// not implemented
	}

}
