package message;

/**
 * A FriendRequest message, used when one user wants to 'friend' another user.
 * Automatically generates message content based on to/from user identifiers.
 */
public class FriendRequest extends Message {

	public FriendRequest(Integer fromUserID, Integer toUserID) {
		super(TYPE_FRIEND, fromUserID, toUserID);
		
		addSubject("Someone wants to be your friend!");
		generateFriendContent();
	}

	/**
	 * Generates standardized 'friend request' message content based on the
	 * sending and recipient users
	 */
	private void generateFriendContent() {
		// TODO
		// need to replace userIDs with userNames?
		// add accept friend request button/link
		
		StringBuilder content = new StringBuilder();
		content.append("<h1>Someone wants to be your friend!</h1>");
		content.append("<p><a href=\"userpage.jsp?userID=" + fromUserID + ">"); 
		content.append(fromUserID + "</a> has requested to be your friend! </p>");
		content.append("<p>TODO: add accept friend request button here");
		
		addContent( content.toString() );
	}
}
