package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import javax.servlet.http.HttpServletRequest;

public class Announcement extends Message {
	
	public Announcement(Integer toUserID, Integer fromUserID, String announcement) {
		super(TYPE_ANNOUNCEMENT, toUserID, fromUserID);
		addSubject("IMPORTANT: ADMIN ANNOUNCEMENT");
		addContent(announcement);
	}
	
	/**
	 * Makes one new Announcement Message for all users in the database
	 */
	public static int 
	makeAnnouncements(HttpServletRequest request, DBConnection connection) 
	throws SQLException, InputMismatchException {
		
		String announcement = (String)  request.getParameter("content");
		Integer fromUserID  = Integer.parseInt( request.getParameter("fromUserID") );
		if (announcement == null || announcement.length() == 0) { 
			throw new InputMismatchException(); // invalid input
		}
		
		int count = 0;
		int currUserID;
		Announcement newAnnouncement;
		ResultSet allUsers = connection.getAllUsers();
		while ( allUsers.next() ) {
			currUserID = (Integer) allUsers.getObject("userID");
			newAnnouncement = new Announcement(currUserID, fromUserID, announcement);
			connection.addMessage(newAnnouncement);
			count++;
		}
		
		return count;
	}
	
	/**
	 * Displays the necessary HTML form information to create a new Announcement
	 * @return
	 */
	public static String getCreationHTML(Integer userID) { 
		StringBuilder html = new StringBuilder();
		
		html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
		html.append("<input name='type' type='hidden' value=" + Message.TYPE_ANNOUNCEMENT + " />");
		html.append("<input name='hasContent' type='hidden' value='true' />");
		
		html.append( 
			"<div class='row'><br>" +
			  "<div class='form-group'>" +
			    "<label for='1' class='col-sm-2 control-label'>Announcement</label>" +
			    "<div class='col-sm-7'>" +
			      "<textarea class='form-control' id='1' name='content' " +
	      			         "placeholder='Announcement' rows='5'></textarea>" +
			    "</div>" +
			  "</div>" +
			"</div>" 
		); 
		return html.toString(); 
	}
}
