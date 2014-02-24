package message;

public class Announcement extends Message {
	
	// TODO: add static method to make several from a list of users?
	//       could be useful since all announcements are sent to all users ?
	//       only issue is syncing with db
	
	public Announcement(Integer toUserID, Integer fromUserID, String announcement) {
		super(TYPE_ANNOUNCEMENT, toUserID, fromUserID);
		addContent(announcement);
	}
}
