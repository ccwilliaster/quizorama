package quiz;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String COOKIE_NAME = "userCookie";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        // Do nothing special
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
		requestDispatcher.forward(request, response);
		return;
	} //doGet

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String origin = request.getParameter("origin");
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		int userID = -1;
		
		if (origin.equals("CreateAccount")) {
			try {
				// First check if user name is taken
				if ( dbConnection.userNameInUse(userName) ) {
					request.setAttribute("error", "Sorry! That username already exits!");
					request.getRequestDispatcher("CreateAccount.jsp").forward(request, response);
					return;
				}

				String passwordHash;
				passwordHash = PasswordHash.createHash(password);

				userID = dbConnection.createUser(userName, passwordHash);
			} catch (SQLException e) {
				request.setAttribute("error", "Cannot create account with provided information");
				goToFail(request, response);
				return;
			}
			catch (NoSuchAlgorithmException ignore) { } 
			catch (InvalidKeySpecException ignore) { } //catch
			
		} //if
		else if (origin.equals("Login")) {
			//Check to see if the password is correct. 
			//If it is, then log on, if not, then kick user back to login screen
			try {
				String dbPassword = dbConnection.getPassword(userName);
				if (PasswordHash.validatePassword(password, dbPassword)) {
					userID = dbConnection.getUserID(userName);
				}
				else {
					request.setAttribute("error", "Invalid password, please try again.");
					goToFail(request, response);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("error", "An error occured, please try again.");
				goToFail(request, response);
				return;
			}
			catch (NoSuchAlgorithmException ignore) { } 
			catch (InvalidKeySpecException ignore) { } //catch

		} //else if
		else {
			goToFail(request, response);
			return;
		} //else

		//This code will run no matter which correct page called this, the user created will be different based on 
		// whether a correct password of a previous user was used or a new user was created
		User user = new User(userID, userName, dbConnection);
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("user", user);
		Cookie userCookie = new Cookie(COOKIE_NAME, PasswordHash.createCookie());
		try {
			dbConnection.createCookie(userID, userCookie.getValue());
		} catch (SQLException e) {
			e.printStackTrace();
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
			requestDispatcher.forward(request, response);
			return;
		}
		userCookie.setMaxAge(365 * 24 * 60 * 60); //One year
		response.addCookie(userCookie);

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("userpage.jsp?userID=" + userID);
		requestDispatcher.forward(request, response);
	} //doPost
	
	static void goToFail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
		requestDispatcher.forward(request, response);
		return;
	} //goToFail
	
}
