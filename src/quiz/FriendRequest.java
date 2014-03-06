package quiz;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 * A FriendRequest message, used when one user wants to 'friend' another user.
 * Automatically generates message content based on to/from user identifiers.
 */
public class FriendRequest extends Message {

	public FriendRequest(int fromUserID, int toUserID, String fromUserName) {
		super(TYPE_FRIEND, toUserID, fromUserID);
		
		addSubject("Someone wants to be your friend!");
		addContent( generateFriendContent(fromUserID, fromUserName) + 
				    getAcceptButton(fromUserID) );
	}

	/**
	 * Static method to generate a new FriendRequest Message from a request and 
	 * add it to the database via the DBConnection
	 * @param request request expected to contain the parameters specified by the
	 * FriendRequest.getCreationHTML() method
	 * @param connection connection to database, has addMessage() method
	 * @throws SQLException
	 */
	public static void makeFriendRequest(HttpServletRequest request, DBConnection connection) 
	throws SQLException {
		String toUserName   = request.getParameter("toUserName");
		Integer toUserID    = connection.getUserID( toUserName );
		Integer fromUserID  = Integer.parseInt(request.getParameter("fromUserID"));
		String fromUserName = connection.getUserName( fromUserID ); 
		
		FriendRequest newFR = new FriendRequest(fromUserID, toUserID, fromUserName);
		connection.addMessage(newFR);
	}
	
	/**
	 * Generates standardized 'friend request' message content based on the
	 * sending and recipient users
	 */
	private static String 
	generateFriendContent(Integer fromUserID, String fromUserName) {
		// TODO
		// need to replace userIDs with userNames?
		// add accept friend request button/link
		
		String content = 
			"<h4>" + fromUserName + " wants to be your friend!</h4>" +
			"User <a class='btn btn-default btn-xs' href='userpage.jsp?userID=" + 
			fromUserID + "'>" + fromUserName + "</a> has requested to be your " +
			"friend! Check out their page or click below to accept the request<br><br>";
		
		return content;
	}
	
	/**
	 * Returns an HTML button form for accepting a friend request from the user
	 * whose user ID is fromUserID. 
	 */
	private static String getAcceptButton(Integer fromUserID) {
		return "<form action='FriendAcceptServlet' method='post'>" +
		         "<input name='fromUserID' value=" + fromUserID + " type='hidden'>" +
			     "<input class='btn btn-default' type='submit' value='Accept'><br>" +
			   "</form>";
	}
	
	/**
	 * Displays the necessary HTML form information to create a new FriendRequest
	 * @return
	 */
	public static String getCreationHTML(Integer userID, DBConnection connection) 
	throws SQLException { 
		String userName = connection.getUserName(userID);
		StringBuilder html = new StringBuilder();
		
		html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
		html.append("<input name='type' type='hidden' value=" + Message.TYPE_FRIEND + " />");
		html.append("<input name='hasContent' type='hidden' value='true' />");
		html.append( 
			"<div class='row'><br>" +
			  "<div class='form-group'>" +
			    "<label for='1' class='col-sm-2 control-label'>User to friend</label>" +
			    "<div class='col-sm-7'>" +
			      "<input id='1' type='text' class='form-control'" +
			              "name='toUserName' placeholder='Username'>" +
			    "</div>" +
			  "</div>" +
			"</div>" +
			"<div class='row'><br>" +
			  "<label class='col-sm-2'>Preview</label>" +
			  "<div class='col-sm-7'>" +
			    "<div class='panel panel-default'>" +
			      "<div class='panel-body' >" +
			        generateFriendContent(userID, userName) + 
			        "<a class='disabled btn btn-default'>Accept</a><br>" +
			      "</div>" +
			    "</div>" +
			  "</div>" +
			"</div>" 
		);
		// TODO: add select2.css/.js script for populating usernames in place of form
		
		return html.toString(); 
	}
}
