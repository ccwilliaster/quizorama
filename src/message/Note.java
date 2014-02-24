package message;

/**
 * A generic Note Message type. Users can send these to each other, contains
 * a subject and HTML-printable content.
 */
public class Note extends Message {

	public Note(Integer toUserID, Integer fromUserID, String subject, String content) {
		super(TYPE_NOTE, toUserID, fromUserID);
		
		addSubject(subject);
		addContent(content);
	}
	
}
