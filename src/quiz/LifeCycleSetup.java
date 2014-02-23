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
    	String server = MyDBInfo.MYSQL_DATABASE_SERVER;
    	String account = MyDBInfo.MYSQL_USERNAME;
    	String password = MyDBInfo.MYSQL_PASSWORD;
    	String database = MyDBInfo.MYSQL_DATABASE_NAME;
    	
    	DBConnection dbConnection = new DBConnection();
    	
    	Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection
					( "jdbc:mysql://" + server, account, password);
			PreparedStatement sql = conn.prepareStatement("USE " + database);
			sql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    			
		servletContext.setAttribute("DBConnection", conn);
    } //contextInitialized

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
ServletContext servletContext = servletContextEvent.getServletContext();
    	
    	Connection conn = (Connection) servletContext.getAttribute("DBConnection");
    	try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    } //contextDestroyed
	
}
