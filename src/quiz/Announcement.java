package quiz;

import java.util.InputMismatchException;

import javax.servlet.http.HttpServletRequest;

public class Announcement extends Message {
	
	public Announcement(Integer toUserID, Integer fromUserID, String announcement) {
		super(TYPE_ANNOUNCEMENT, toUserID, fromUserID);
		addSubject("announcement");
		addContent(announcement);
	}
	
//	public static int 
//	makeAnnouncements(HttpServletRequest request, DBConnection connection) 
//	throws InputMismatchException {
//		
//      do not send to sending user?
	
//		ArrayList<Integer> allUserIDs = connection.getAllUserIDs();
//		String announcement = (String)  request.getAttribute("announcement");
//		Integer fromUserID  = (Integer) request.getAttribute("userID");
//		if (announcement == null) { 
//			throw new InputMismatchException();
//		}
//		
//		Announcement newAnnouncement;
//		for (Integer toUserID : allUserIDs) {
//			newAnnouncement = new Announcement(toUserID, fromUserID, announcement);
//		
//		}
//		return allUserIDs.size();
//	}
	
	/**
	 * Displays the necessary HTML form information to create a new Announcement
	 * @return
	 */
	public static String getCreationHTML(Integer userID) { 
		StringBuilder html = new StringBuilder();
		
		html.append("<input name=\"fromUserID\" type=\"hidden\" value=" + userID + " />");
		html.append("<input name=\"type\" type=\"hidden\" value=" + Message.TYPE_ANNOUNCEMENT + " />");
		html.append("<input name=\"hasContent\" type=\"hidden\" value=\"true\" />");
		
		html.append( 
			"<div class=\"row\"><br>" +
			  "<div class=\"form-group\">" +
			    "<label for=\"1\" class=\"col-sm-2 control-label\">Announcement</label>" +
			    "<div class=\"col-sm-7\">" +
			      "<textarea class=\"form-control\" id=\"1\" name=\"content\" " +
	      			         "placeholder=\"Announcement\" rows=\"5\"></textarea>" +
			    "</div>" +
			  "</div>" +
			"</div>" 
		); 
		return html.toString(); 
	}
}
