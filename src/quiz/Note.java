package quiz;

import javax.servlet.http.HttpServletRequest;

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
	
	public void makeNote() {
		return;
	}
	
	/**
	 * Displays the necessary HTML form information to create a new Note
	 * @return
	 */
	public static String getCreationHTML(Integer userID) { 
		StringBuilder html = new StringBuilder();
		
		html.append("<input name=\"fromUserID\" type=\"hidden\" value=" + userID + " />");
		html.append("<input name=\"type\" type=\"hidden\" value=" + Message.TYPE_NOTE + " />");
		html.append("<input name=\"hasContent\" type=\"hidden\" value=\"true\" />");
		
		html.append( 
			"<div class=\"row\"><br>" +
			  "<div class=\"form-group\">" +
			    "<label for=\"1\" class=\"col-sm-2 control-label\">To user</label>" +
			    "<div class=\"col-sm-7\">" +
			      "<input id=\"1\" type=\"text\" class=\"form-control\"" +
			              "name=\"toUserName\" placeholder=\"Username\">" +
			    "</div>" +
			  "</div>" +
			"</div>" +
			"<div class=\"row\">" +
			  "<div class=\"form-group\">" +
			    "<label for=\"2\" class=\"col-sm-2 control-label\">Subject</label>" +
			    "<div class=\"col-sm-7\">" +
			      "<input type=\"text\" id=\"2\" class=\"form-control\" " + 
			              "name=\"subject\" placeholder=\"Subject\">" +
			    "</div>" +
			  "</div>" +
			"</div>" +
			"<div class=\"row\">" +
			  "<div class=\"form-group\">" +
			    "<label for=\"3\" class=\"col-sm-2 control-label\">Content</label>" +
			    "<div class=\"col-sm-7\">" +
			      "<textarea class=\"form-control\" id=\"3\" name=\"content\" " +
			      			"rows=\"5\"></textarea>" +
			    "</div>" + 
			  "</div>" +
			"</div>"
		);    

		// TODO: add select2.css/.js script for populating usernames in place of form
		
		return html.toString(); 
	}
	
}
