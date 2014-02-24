package message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class Message {

	// Possible message types
	public static final int TYPE_NOTE         = 0;
	public static final int TYPE_FRIEND       = 1;
	public static final int TYPE_CHALLENGE    = 2;
	public static final int TYPE_ANNOUNCEMENT = 3;
	public static final int TYPE_QUIZ_FLAG    = 4;
	
	// Instance variables, match fields in database message table
	protected int messageType;
	protected Integer messageID, toUserID, fromUserID, messageRead;
	protected String subject, content; 
	protected Date date;
	
	public Message(int messageType, Integer toUserID, Integer fromUserID) {
		this.messageType = messageType;
		this.fromUserID  = fromUserID;
		this.toUserID    = toUserID;
		
		messageID   = null;
		subject     = null;
		content     = null;
		messageRead = 0;
		date        = new Date();
	}
	
	/**
	 * Alternate constructor for re-making a Message from a database entry
	 * (all fields known/provided at creation)
	 */
	public Message(Integer messageID, int messageType, Integer toUserID, 
			       Integer fromUserID, String subject, String content, 
			       Date date, Integer messageRead) {
		this.messageID   = messageID;
		this.messageType = messageType;
		this.toUserID    = toUserID;
		this.fromUserID  = fromUserID;
		this.messageRead = messageRead;
		this.subject     = subject;
		this.content     = content;
		this.date        = date;
	}
	
	/**
	 * Setter method to add a subject to the Message
	 * @param subject simple subject String
	 */
	public void addSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * Setter method to add HTML-printable content to the Message
	 * @param content HTML-printable content
	 */
	public void addContent(String content) {
		this.content = content;
	}	
	
	/**
	 * Static convenience method for constructing Messages from a SQL message 
	 * ResultSet query. Returns a list of Message objects, one for each row in 
	 * the ResultSet with Message attributes corresponding to column values. 
	 * Messages of a particular type can be filtered by omitting them from the
	 * validTypes set.
	 * 
	 * Note: specialized child Message types are not made, but these parent
	 * Messages can still be filtered based on their messageType
	 * @param messageQuery 
	 * @param validType Messages of a type not contained in this set are 
	 * filtered. Alternatively, if this is null, all message types are returned.
	 * @return list of Messages made from the values in the ResultSet
	 */
	public static ArrayList<Message> 
	loadMessages(ResultSet messageQuery, Set<Integer> validTypes) {
		ArrayList<Message> messages = new ArrayList<Message>();
		
		Message currMessage;
		int messageType;
		Integer messageID, toUserID, fromUserID, messageRead;
		String subject, content;
		Date date;
		
		// Potentially initialize a new message for each row in ResultSet
		try {
			while ( messageQuery.next() ) {
				
				// Fetch variables by name, no magic numbers / col indices
				messageID   = (Integer) messageQuery.getObject("messageID");
				messageType = (Integer) messageQuery.getObject("messageType");
				toUserID    = (Integer) messageQuery.getObject("toUserID");
				fromUserID  = (Integer) messageQuery.getObject("fromUserID");
				subject     = (String)  messageQuery.getObject("subject");
				content     = (String)  messageQuery.getObject("content");
				date        = (Date)    messageQuery.getObject("date");
				messageRead	= (Integer) messageQuery.getObject("messageRead");
				
				// Initialize and add message if its type is valid
				if (validTypes == null || validTypes.contains(messageType) ) {
					currMessage = new Message(messageID, messageType, toUserID, 
											  fromUserID, subject, content, date, 
											  messageRead);
					
					messages.add( currMessage );
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	/**
	 * Prints a String representation of the date the message was created in 
	 * the format: 'yyyy-MM-dd hh:mm a'
	 * @return pretty print date message was created
	 */
	public String printDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		return dateFormat.format( date );
	} 
	
	/**
	 * Updates the messageRead attribute of the Message to reflect that
	 * the message has been opened by the toUserID User
	 */
	public void setMessageRead() {
		if (messageRead == 0) {
			messageRead = 1;
			// TODO make sure db knows about this
			// DBConnecton.updateMessage( this ); ?
		}
	}
	
	/**
	 * Getter that returns the type of the Message
	 * @return message type
	 */
	public Integer getMessageType() { return messageType; }
	
	/**
	 * Getter that returns the messageID of the Message
	 * @return unique message ID (or null if not set in database)
	 */
	public Integer getMessageID() { return messageID; }
	
	/**
	 * Getter that returns the userID of the User the Message was sent to
	 * @return userID of recipient User
	 */
	public Integer getToUserID() { return toUserID; }
	
	/**
	 * Getter that returns the userID of the author of the Message
	 * @return userID of the Message author User
	 */
	public Integer getFromUserID() { return fromUserID; }
	
	/**
	 * Getter that returns the Message subject String
	 * @return subject String
	 */
	public String getSubject() { return subject; }
	
	/**
	 * Getter that returns the HTML-printable content String
	 * @return message Content
	 */
	public String getContent() { return content; }
	/**
	 * Getter that returns the Message Date object. Use printDate() if a 
	 * String format is desired.
	 * @return Message Date object, reflecting date message was created
	 */
	public Date getDate() { return date; }
	
	/**
	 * Getter that returns whether the Message has been read by the recipient 
	 * (1) or not (0)
	 * @return whether the recipient User has read this Message
	 */
	public Integer getMesssageRead() { return messageRead; }
}
