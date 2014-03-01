package quiz;

/**
 * A FriendRequest message, used when one user wants to 'friend' another user.
 * Automatically generates message content based on to/from user identifiers.
 */
public class FriendRequest extends Message {

	public FriendRequest(Integer fromUserID, Integer toUserID) {
		super(TYPE_FRIEND, fromUserID, toUserID);
		
		addSubject("Someone wants to be your friend!");
		addContent( generateFriendContent(fromUserID) + getAcceptButton(fromUserID) );
	}

	/**
	 * Generates standardized 'friend request' message content based on the
	 * sending and recipient users
	 */
	private static String generateFriendContent(Integer fromUserID) {
		// TODO
		// need to replace userIDs with userNames?
		// add accept friend request button/link
		
		String content = 
			"<h4>Someone wants to be your friend!</h4>" +
			"User <a class=\"btn btn-default btn-xs\" href=\"userpage.jsp?userID=" + 
			fromUserID + "\">" + fromUserID + "</a> has requested to be your " +
			"friend! Check out their page or click below to accept the request<br><br>";
		
		return content;
	}
	
	private static String getAcceptButton(Integer fromUserID) {
		return "<form action=\"FriendAcceptServlet\" method=\"post\">" +
		         "<input name=\"fromUserID\" value=" + fromUserID + " type=\"hidden\">" +
			     "<input class=\"btn btn-default\" type=\"submit\" value=\"Accept\"><br>" +
			   "</form>";
	}
	
	/**
	 * Displays the necessary HTML form information to create a new FriendRequest
	 * @return
	 */
	public static String getCreationHTML(Integer userID) { 
		StringBuilder html = new StringBuilder();
		
		html.append("<input name=\"fromUserID\" type=\"hidden\" value=" + userID + " />");
		html.append("<input name=\"type\" type=\"hidden\" value=" + Message.TYPE_FRIEND + " />");
		html.append("<input name=\"hasContent\" type=\"hidden\" value=\"true\" />");
		html.append( 
			"<div class=\"row\"><br>" +
			  "<div class=\"form-group\">" +
			    "<label for=\"1\" class=\"col-sm-2 control-label\">User to friend</label>" +
			    "<div class=\"col-sm-7\">" +
			      "<input id=\"1\" type=\"text\" class=\"form-control\"" +
			              "name=\"toUserName\" placeholder=\"Username\">" +
			    "</div>" +
			  "</div>" +
			"</div>" +
			"<div class=\"row\"><br>" +
			  "<label class=\"col-sm-2\">Preview</label>" +
			  "<div class=\"col-sm-7\">" +
			    "<div class=\"panel panel-default\">" +
			      "<div class=\"panel-body\" >" +
			        generateFriendContent(userID) + 
			        "<a class=\"disabled btn btn-default\">Accept</a><br>" +
			      "</div>" +
			    "</div>" +
			  "</div>" +
			"</div>" 
		);
		// TODO: add select2.css/.js script for populating usernames in place of form
		
		return html.toString(); 
	}
}
