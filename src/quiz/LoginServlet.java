package quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // Do nothing special
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.html");
		requestDispatcher.forward(request, response);
		return;
	} //doGet

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String account = request.getParameter("userName");
		String password = request.getParameter("password");
		
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
		
		generateWelcomePage(response,account);
		
	} //doPost

	protected static void generateWelcomePage(HttpServletResponse response, String account) throws IOException {
		response.setContentType("text/html; charset=UTF-8"); 

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />"); 
		out.println("<title>Welcome " + account + "</title>"); 
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Welcome " + account + "</h1>"); 
		out.println("</body>"); 
		out.println("</html>");

	} //generateWelcomePage
	
}
