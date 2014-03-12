package quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO: Remove all of the print stack traces

/**
 * Servlet implementation class QuizCreateServlet
 */
@WebServlet("/QuizCreateServlet")
public class QuizCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String RANDOM_PARAM = "random";
	private static final String ONEPAGE_PARAM = "one-page";
	private static final String PRACTICE_PARAM = "practice-mode";
	private static final String IMMEDIATECORR_PARAM = "immediate-correction";
	private static final String QTYPE_QR = "questionResponse";
	private static final String QTYPE_FB = "fillInBlank";
	private static final String QTYPE_MC = "multipleChoice";
	private static final String QTYPE_PR = "pictureResponse";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizCreateServlet() {
        super();
        //Do nothing special
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Redirect to the CreateQuiz.jsp webpage
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//If we are coming from the CreateQuiz, then make a new Quiz, add it to the session
		// and then go into question creation mode.
		
		if (request.getParameter("origin").equals("CreateQuiz.jsp")) {
			Quiz quiz = null;
			try {
				quiz = createNewQuiz(request);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
				requestDispatcher.forward(request, response);
			} //catch
			
			
			
			//Stash the quiz in the session object
			request.getSession().setAttribute("Quiz", quiz);
			
			//Allow a user to select the next type of question
			askForNextQuestion(response);
			
		} //if
		else if(request.getParameter("origin").equals("QuizCreateServlet")) {
			//We have just chosen a type of question, so let's re-direct the user to that question type:
			String questionType = request.getParameter("questionType");
			if (questionType.equals(QTYPE_QR)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizQR.jsp");
				requestDispatcher.forward(request, response);
			} //if
			else if (questionType.equals(QTYPE_FB)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizFB.jsp");
				requestDispatcher.forward(request, response);
			} //else if
			else if (questionType.equals(QTYPE_MC)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizMC.jsp");
				requestDispatcher.forward(request, response);
			} //else if
			else if (questionType.equals(QTYPE_PR)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizPR.jsp");
				requestDispatcher.forward(request, response);
			} //else if
			else {
				//We have chosen not to add any more questions. Finalize the quiz and send the user back to homepage
			} //Else

		} //else if
		else if(request.getParameter("origin").equals("CreateQuizQR.jsp")) {
			String questionText = request.getParameter("question");

			Question question = null;
			try {
				question = createQuestion(request, questionText);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}
			
			String answerText = request.getParameter("response");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText.concat("|" + request.getParameter("otherResponses"));
			}
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}

			//Last thing:
			askForNextQuestion(response);
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizFB.jsp")) {
			String questionPreText = request.getParameter("pre");
			String questionPostText = request.getParameter("post");
			String questionText = questionPreText + " _________ " + questionPostText;

			Question question = null;
			try {
				question = createQuestion(request, questionText);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}
			
			String answerText = request.getParameter("blank");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText.concat("|" + request.getParameter("otherResponses"));
			}
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}
			
			//Last thing:
			askForNextQuestion(response);
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizMC.jsp")) {
			String questionText = request.getParameter("question");
			questionText.concat("|" + request.getParameter("mc1"));
			questionText.concat("|" + request.getParameter("mc2"));
			questionText.concat("|" + request.getParameter("mc3"));
			questionText.concat("|" + request.getParameter("mc4"));

			Question question = null;
			try {
				question = createQuestion(request, questionText);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}

			String answerText = request.getParameter("answer");
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}
			
			//Last thing:
			askForNextQuestion(response);
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizPR.jsp")) {
			String questionText = request.getParameter("question");

			Question question = null;
			try {
				question = createQuestion(request, questionText);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}
			
			String answerText = request.getParameter("response");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText.concat("|" + request.getParameter("otherResponses"));
			}
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
			}

			//Last thing:
			askForNextQuestion(response);
		} //else if
		
		
		else {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
			requestDispatcher.forward(request, response);
		} //else
	}

	private void createAnswer(HttpServletRequest request, String answerText, Question question) throws SQLException {
		
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

		//Getting the quiz
		Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
		
		String[] multiAnswers = answerText.split("|");
		for (String thisAnswer : multiAnswers) {
			dbConnection.addAnswer(thisAnswer, question.getQuestionId(), quiz.getQuizID());
		} //for
		
		//Populate all of the answers that we just put into the db to the question
		question.addAnswers(dbConnection); 
	}

	private Question createQuestion(HttpServletRequest request, String questionText) throws SQLException {
		//Create a question and then add it to the list of questions that exists in the quiz
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

		//Getting the quiz
		Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
		int questionID = dbConnection.addQuestion(questionText, Question.QTYPE_QR, quiz.getNextQuestionNum(), quiz.getQuizID());

		Question question = new QuestionResponseQuestion(questionID, questionText, Question.QTYPE_QR, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
		quiz.addQuestion(question);
		return question;
	}

	private void askForNextQuestion(HttpServletResponse response) throws IOException {

		//Create a very simple page asking the user what the next question type will be
		
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />"); 
		out.println("<title>Create a question...</title>"); 
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>What type of question would you like to create?</h1>");
		out.println("<form action=\"QuizCreateServlet\" method=\"post\" >");
		out.println("<input name=\"origin\" type=\"hidden\" value=\"QuizCreateServlet\" >");
		out.println("Question Type: <select name=\"questionType\"> <option value=\"done\">No More Questions!</option>");
		out.println("<option value=\"" + QTYPE_QR + "\">Question Response Question</option>");
		out.println("<option value=\"" + QTYPE_MC + "\">Multiple Choice Question</option>");
		out.println("<option value=\"" + QTYPE_FB + "\">Fill-In-The-Blank Question</option>");
		out.println("<option value=\"" + QTYPE_PR + "\">Picture Response Question</option></select>");
		out.println("<button>Go!</button>");
		out.println("</form>");
	}

	private Quiz createNewQuiz(HttpServletRequest request) throws SQLException {
		System.out.println("In the new quiz creation.");
		String quizName = (String) request.getParameter("quizName");
		ServletContext servletContext = this.getServletContext();
		DBConnection dbConnection = (DBConnection) servletContext.getAttribute("DBConnection");
		User user = (User) request.getSession().getAttribute("user");
		int userID = user.getUserID();
		
		String[] checkBoxes = request.getParameterValues("quizParams");
		
		boolean singlePage = false;
		boolean randomOrder = false;
		boolean immediateCorrection = false;
		boolean practiceMode = false;
		
		for (int i = 0; i < checkBoxes.length; i++ ) {
			if (checkBoxes[i].equals(RANDOM_PARAM)) {
				randomOrder = true;
			} //if
			else if (checkBoxes[i].equals(ONEPAGE_PARAM)) {
				singlePage = true;
			} //else if
			else if (checkBoxes[i].equals(PRACTICE_PARAM)) {
				practiceMode = true;
			} //else if
			else if (checkBoxes[i].equals(IMMEDIATECORR_PARAM)) {
				immediateCorrection = true;
			} //else if
			
		} //for
				
			return new Quiz(quizName, userID, singlePage,randomOrder,immediateCorrection, practiceMode, dbConnection);
	}

}
