package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ServletContext
 *
 */
@WebListener
public class LifeCycleSetup implements ServletContextListener {
	
	/**
     * Default constructor. 
     */
    public LifeCycleSetup() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	ServletContext servletContext = servletContextEvent.getServletContext();
    	
    	DBConnection dbConnection = null;
		try {
			dbConnection = new DBConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    			
		servletContext.setAttribute("DBConnection", dbConnection);
		System.out.println("DBConnection made");
    } //contextInitialized

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	ServletContext servletContext = servletContextEvent.getServletContext();
    	
    	DBConnection dbConnection= (DBConnection) servletContext.getAttribute("DBConnection");
    	try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("DBConnection destroyed");
    } //contextDestroyed
	
}