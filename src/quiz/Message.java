package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class Message implements Comparable<Message>{

	// nchar of inbox subject/content preview
	private static final int NCHAR_PREV = 40;
	
	// Possible message types
	public static final int TYPE_NOTE         = 0;
	public static final int TYPE_FRIEND       = 1;
	public static final int TYPE_CHALLENGE    = 2;
	public static final int TYPE_ANNOUNCEMENT = 3;
	public static final int TYPE_QUIZ_FLAG    = 4;
	
	// Instance variables, match fields in database message table
	protected int messageType, fromUserID, toUserID;
	protected boolean messageRead;
	protected Integer messageID;
	protected String subject, content; 
	protected Date date;
	
	// Constructor for messageCreation
	public Message(int messageType, int toUserID, int fromUserID) {
		this.messageType = messageType;
		this.fromUserID  = fromUserID;
		this.toUserID    = toUserID;
		
		messageID   = null;
		subject     = null;
		content     = null;
		messageRead = false;
		date        = new Date();
	}
	
	/**
	 * Alternate constructor for re-making a Message from a database entry
	 * (all fields known/provided at creation)
	 */
	public Message(Integer messageID, int messageType, int toUserID, 
			       int fromUserID, String subject, String content, 
			       Date date, Boolean messageRead) {
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
	 * Static helper method for constructing Messages from a SQL message 
	 * ResultSet query. Returns a list of Message objects, one for each row in 
	 * the ResultSet with Message attributes corresponding to column values. 
	 * Messages of a particular type can be filtered by omitting them from the
	 * validTypes set.
	 * 
	 * Note: specialized child Message types are not made, but these parent
	 * Messages can still be filtered based on their messageType
	 * @param messageQuery 
	 * @param validType If not null, Messages of a type not contained in this 
	 * set are filtered. 
	 * @param validToUserID if not null, Messages with a toUserID different from
	 * this value are filtered
	 * @param validFromUserID if not null, Messages with a fromUserID different
	 * from this value are filtered
	 * @return list of unfiltered Messages made from the values in the ResultSet
	 */
	public static ArrayList<Message> 
	loadMessages(ResultSet messageQuery, Set<Integer> validTypes,
				 Integer validToUserID, Integer validFromUserID) throws SQLException {

		ArrayList<Message> messages = new ArrayList<Message>();
		
		Message currMessage;
		int messageType, toUserID, fromUserID;
		Boolean messageRead;
		Integer messageID;
		String subject, content;
		Date date;
		
		// Potentially initialize a new message for each row in ResultSet
			while ( messageQuery.next() ) {	
			// Fetch variables by name, no magic numbers / column indices
			messageID   = (Integer) messageQuery.getObject("messageID");
			messageType = (Integer) messageQuery.getObject("messageType");
			toUserID    = (Integer) messageQuery.getObject("toUserID");
			fromUserID  = (Integer) messageQuery.getObject("fromUserID");
			subject     = (String)  messageQuery.getObject("subject");
			content     = (String)  messageQuery.getObject("content");
			date        = (Date)    messageQuery.getObject("date");
			messageRead	= (Boolean) messageQuery.getObject("messageRead");
			
			// Construct a Message only if it passes possible filters
			if (validToUserID != null && toUserID != validToUserID ) {
				continue;
			}  
			if (validFromUserID != null && fromUserID != validFromUserID) {
				continue;
			}
			if (validTypes != null && !validTypes.contains(messageType) ) {
				continue;
			}
			currMessage = new Message(messageID, messageType, toUserID, 
									  fromUserID, subject, content, date, 
									  messageRead);
			
			messages.add( currMessage );
		}

		return messages;
	}
	
	/**
	 * Prints a String representation of the date the message was created in 
	 * the format: 'yyyy-MM-dd hh:mm a'
	 * @return pretty print date message was created
	 */
	public String printDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
		return dateFormat.format( date );
	} 
	
	/**
	 * Prints a short String representation of the date the message was created in 
	 * the format: 'yyyy-MM-dd'
	 * @return pretty print short date message was created
	 */
	public String printShortDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format( date );
	}
	
	/**
	 * Updates the messageRead attribute of the Message to reflect that
	 * the message has been opened by the toUserID User
	 */
	public void setMessageRead(DBConnection connection) throws SQLException {
		if (messageRead == false) {
			messageRead = true;
			connection.updateMessage( this );
		}
	}
	
	/**
	 * Getter that returns the type of the Message
	 * @return message type
	 */
	public int getType() { return messageType; }
	
	/**
	 * Getter that returns the messageID of the Message
	 * @return unique message ID (or null if not set in database)
	 */
	public Integer getID() { return messageID; }
	
	/**
	 * Getter that returns the userID of the User the Message was sent to
	 * @return userID of recipient User
	 */
	public int getToUserID() { return toUserID; }
	
	/**
	 * Getter that returns the userID of the author of the Message
	 * @return userID of the Message author User
	 */
	public int getFromUserID() { return fromUserID; }
	
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
	 * or not
	 * @return whether the recipient User has read this Message
	 */
	public boolean getReadStatus() { return messageRead; }

	/**
	 * Compares another Message to this Message, the Message with the later
	 * date field is considered smaller 
	 * @param other Message to compare this Message to
	 */
	@Override
	public int compareTo(Message other) {
		return -date.compareTo( other.date );
	}
	
	/**
	 * Returns the contents of a Message as an HTML-printable String.
	 * @return
	 */
	public String displayAsHTML(DBConnection conn) throws SQLException { 
		// TODO: add reply, delete functionality
		
		StringBuilder html = new StringBuilder();
		html.append( 
		"<div class='row'><br>" +
		  "<div class='col-md-8'>" +
		    "<h6>Sent " + printShortDate() + "</h6>" +
		    "<div class='panel panel-default'>" +
			  "<div class='panel-body' >" +
		    	"<dl class='dl-horizontal'>" +
				  "<dt>Sent from</dt> <dd>" + conn.getUserName(toUserID)   + "</dd>" +
			      "<dt>Sent to</dt>   <dd>" + conn.getUserName(fromUserID) + "</dd>" +
			      "<dt>Subject</dt>   <dd>" + subject + "</dd>" +
			      "<dt>Content</dt>   <dd>" + content + "</dd>" +  
				"</dl> " +
			  "</div>" +
			  "<div class='panel-footer text-right'>" +
			    "<div class='btn-group btn-group-sm'>"
		);
		if (messageType == TYPE_NOTE) {
			html.append(
				  "<button type='button' class='btn btn-default' onclick='reply()'>" +
				    "<input name='type' value=" + TYPE_NOTE + " type='hidden'>" +
			        "<input name='toUserID' value=" + fromUserID + " type='hidden'>" +
			        "<input name='subject'  value='Re: " + subject + "' type='hidden'>" +
				    "Reply" +
				  "</button>"
			);
		}
		html.append(
				  "<button type='button' class='btn btn-danger' onclick='delete()'>" +
		            "<input name='messageID' value=" + messageID + " type='hidden'>" +
			        "Delete" +
			      "</button>" +
			    "</div>" +
			  "</div>" +
		    "</div>" +
		  "</div>" +
		"</div>"
		); 	
		return html.toString();
	}
	
	/**
	 * Helper method which wraps the input String in an HTML em tag if this
	 * Message has not been read, else simply returns the input String
	 * @param text text to be wrapped in <em></em>, or not
	 */
	private String emph(String text) {
		if (!messageRead) { return "<em>" + text + "</em>"; }
		return text;
	}
	
	/**
	 * Returns a Message preview in the form of an HTML table row with contents: 
	 * to, from, preview, date
	 * HTML tags are removed from the preview, and Messages that have not been 
	 * read are emphasized
	 * @return
	 */
	public String displayAsTableRow(DBConnection conn, int idx) throws SQLException {
		// Determine how long the subject / content preview will be
		String contentNoHTML = content.replaceAll("\\<[^>]*>", "");
		int lenSubject = Math.min(subject.length(), NCHAR_PREV);
		int lenContent = Math.min(contentNoHTML.length(), NCHAR_PREV - lenSubject);
		
		StringBuilder html = new StringBuilder();
		html.append(
		"<tr class=\"clickableRow\" id=" + idx + " >" +
		  "<input type='hidden' id=" + idx + 
		  " value=\"" + this.displayAsHTML(conn) + "\" />" +
		  "<td>" + emph( conn.getUserName(fromUserID) ) + "</td>" +
		  "<td>" + emph( conn.getUserName(toUserID) ) + "</td>" +
		  "<td>" + emph( subject.substring(0, lenSubject) ) + 
		         " - " + contentNoHTML.substring(0, lenContent) + "... </td>" +
		  "<td>" + emph( printShortDate() ) + "</td>" +
		"</tr>"
		);
		return html.toString();
	}
}
