package quiz;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public SessionListener() {
        //Do nothing
    }

	/**
	 * Create a new user and share it with all of the other servlets
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //Create a new user
    	// Once the login form is clicked on, this user will be updated with the user
    	// information, but for now, let's start it
    	User user = new User();
    	HttpSession httpSession = httpSessionEvent.getSession();
    	httpSession.setAttribute("user", user);
    }

	/**
	 * Destroy the attributes that were set, especially the security conscious ones
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //For security purposes, let's destroy the old user that we had
    	HttpSession httpSession = httpSessionEvent.getSession();
    	User user = (User) httpSession.getAttribute("user");
    	user = new User(); //Reset
    	httpSession.removeAttribute("user"); //Get rid of the attribute
    }
	
}
