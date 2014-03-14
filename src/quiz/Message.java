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
		Boolean messageRead, toUserDeleted, fromUserDeleted;
		Integer messageID;
		String subject, content;
		Date date;
		
		// Potentially initialize a new message for each row in ResultSet
		while ( messageQuery.next() ) {	
			// Fetch variables by name, no magic numbers / column indices
			messageID       = (Integer) messageQuery.getObject("messageID");
			messageType     = (Integer) messageQuery.getObject("messageType");
			toUserID        = (Integer) messageQuery.getObject("toUserID");
			fromUserID      = (Integer) messageQuery.getObject("fromUserID");
			subject         = (String)  messageQuery.getObject("subject");
			content         = (String)  messageQuery.getObject("content");
			date            = (Date)    messageQuery.getObject("date");
			messageRead	    = (Boolean) messageQuery.getObject("messageRead");
			toUserDeleted   = (Boolean) messageQuery.getObject("toUserDeleted");
			fromUserDeleted = (Boolean) messageQuery.getObject("fromUserDeleted");
			
			// Construct a Message only if it passes possible filters
			if ( (validToUserID != null && toUserID != validToUserID) ||
				 (validToUserID != null && toUserDeleted) ) {
				continue; // not TO this user, or TO user deleted
			}  
			if ( (validFromUserID != null && fromUserID != validFromUserID) ||
				 (validFromUserID != null && fromUserDeleted ) ) {
				continue; // not FROM this user, or FROM user deleted
			}
			if (validTypes != null && !validTypes.contains(messageType) ) {
				continue; // not correct type
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
		
		StringBuilder html = new StringBuilder();
		html.append( 
		"<div class='row'><br>" +
		  "<div class='col-md-8'>" +
		    "<h6>Sent " + printDate() + "</h6>" +
		    "<div class='panel panel-default'>" +
			  "<div class='panel-body' >" +
		    	"<dl class='dl-horizontal'>" +
				  "<dt>Sent from</dt> <dd>" + conn.getUserName(fromUserID)   + "</dd>" +
			      "<dt>Sent to</dt>   <dd>" + conn.getUserName(toUserID) + "</dd>" +
			      "<dt>Subject</dt>   <dd>" + subject + "</dd>" +
			      "<dt>Content</dt>   <dd>" + content + "</dd>" +  
				"</dl> " +
			  "</div>" +
			  "<div class='panel-footer text-right'>" 
		);
		if (messageType == TYPE_NOTE) {
			html.append(
				  "<form class='btn-group btn-group-sm' action='NewMessageServlet' method='POST'>" +
				    "<input name='type' value=" + TYPE_NOTE + " type='hidden'>" +
			        "<input name='toUserName' value=" + conn.getUserName(fromUserID) + " type='hidden'>" +
			        "<input name='subject'  value='Re: " + subject + "' type='hidden'>" +
			        "<button class='btn btn-default' type='submit'>Reply</button>" +
				  "</form>"
			);
		}
		html.append(
				  "<form class='btn-group btn-group-sm' action='DeleteMessageServlet' method='POST'>" +
				    "<input name='messageID' value=" + messageID + " type='hidden'>" +
				    "<button class='btn btn-danger' type='submit'>Delete</button>" +
				  "</form>" +
			  "</div>" +
		    "</div>" +
		  "</div>" +
		"</div>"
		); 	
		return html.toString();
	}
	
	/**
	 * Helper method which wraps the input String in an HTML strong tag if this
	 * Message has not been read and makeBold is true
	 * @param text text to be wrapped in <strong></strong>, or not
	 */
	private String strong(String text, boolean makeBold) {
		if (!messageRead && makeBold) { return "<strong>" + text + "</strong>"; }
		return text;
	}
	
	/**
	 * Returns a Message preview in the form of an HTML table row with contents: 
	 * to, from, preview, date
	 * HTML tags are removed from the preview, and Messages that have not been 
	 * read are emphasized
	 * @return
	 */
	public String displayAsTableRow(DBConnection conn, int idx, int requestingUserID) 
	throws SQLException {
		// Determine how long the subject / content preview will be
		String contentNoHTML = content.replaceAll("\\<[^>]*>", "");
		int lenSubject = Math.min(subject.length(), NCHAR_PREV);
		int lenContent = Math.min(contentNoHTML.length(), NCHAR_PREV - lenSubject);
		
		boolean userIsReceiver = requestingUserID == toUserID;
		
		StringBuilder html = new StringBuilder();
		html.append(
		"<tr class=\"clickableRow\" id=" + idx + " >" +
		  "<input type='hidden' id=" + idx + " value=\"" + this.displayAsHTML(conn) + "\" />" +
		  "<input type='hidden' id=\"messageID" + idx + "\" value=\"" + messageID + "\" />" + 
		  "<input type='hidden' id=\"fromUserID" + idx + "\" value=\"" + fromUserID + "\" />" +
		  "<td>" + strong( conn.getUserName(fromUserID), userIsReceiver ) + "</td>" +
		  "<td>" + strong( conn.getUserName(toUserID),   userIsReceiver ) + "</td>" +
		  "<td>" + strong( subject.substring(0, lenSubject), userIsReceiver ) + 
		         " - " + contentNoHTML.substring(0, lenContent) + "... </td>" +
		  "<td>" + strong( printShortDate(), userIsReceiver ) + "</td>" +
		"</tr>"
		);
		return html.toString();
	}
	/**
	 * Returns the message in an alert form with the fromUser and 
	 * @return
	 */
	public String displayAsAlert(DBConnection connection) throws SQLException {
		StringBuilder html = new StringBuilder();
		html.append(
		"<div class='container'" +
		  "<div class='col-md-4'" +
		    "<div class='alert alert-danger alert-dismissable' >" + 
		      "<button type='button' class='close' data-dismiss='alert' >x</button>" +
			  "<h4 class='alert-heading'>Admin:" + connection.getUserName(fromUserID) + "</h4>" + 
			  content +
			"</div>" +
		  "</div>" +
		"</div>");
		return html.toString();
	}
	/**
	 * Return the message as a compact list group item
	 */
	public String displayCompact(DBConnection connection) throws SQLException {
		StringBuilder html = new StringBuilder();
		html.append(
		"<li class='list-group-item'>" +
		  "<h4 class='list-group-item-heading'>" + connection.getUserName(fromUserID) + "</h4>" +
		  "<p class='list-group-item-text'" + subject + "</p>" +
		"</li>");
		return html.toString();
	}
}
