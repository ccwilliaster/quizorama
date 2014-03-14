package quiz;

import java.io.IOException;
import java.sql.SQLException;

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
		return;
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
				return;
			} //catch
			
			if (quiz == null) {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			//Stash the quiz in the session object
			request.getSession().setAttribute("Quiz", quiz);
			
			//Allow a user to select the next type of question
			askForNextQuestion(request, response, true); 
			return;
		} //if
		else if(request.getParameter("origin").equals("QuizCreateServlet")) {
			//We have just chosen a type of question, so let's re-direct the user to that question type:
			String questionType = request.getParameter("questionType");
			if (questionType.equals(QTYPE_QR)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizQR.jsp");
				requestDispatcher.forward(request, response);
				return;
			} //if
			else if (questionType.equals(QTYPE_FB)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizFB.jsp");
				requestDispatcher.forward(request, response);
				return;
			} //else if
			else if (questionType.equals(QTYPE_MC)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizMC.jsp");
				requestDispatcher.forward(request, response);
				return;
			} //else if
			else if (questionType.equals(QTYPE_PR)) {
				//Redirect to the questionResponse jsp
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizPR.jsp");
				requestDispatcher.forward(request, response);
				return;
			} //else if
			else {
				//All questions and answers have been completed and inserted into the db.
				//Remove the quiz attribute and then redirect to the user page.
				request.getSession().removeAttribute("quiz");
				User user = (User) request.getSession().getAttribute("user");
				DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
				try {
					Achievements.wroteQuiz(user.getUserID(), dbConnection);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuizPR.jsp");
					requestDispatcher.forward(request, response);
					return;
				}
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("userpage.jsp?userID=" + user.getUserID());
				requestDispatcher.forward(request, response);
				return;
			} //Else

		} //else if
		else if(request.getParameter("origin").equals("CreateQuizQR.jsp")) {
			String questionText = request.getParameter("question");

			Question question = null;
			try {
				question = createQuestion(request, questionText, Question.QTYPE_QR);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			String answerText = request.getParameter("response");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText = answerText + "~" + request.getParameter("otherResponses");
			}
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			//Last thing:
			askForNextQuestion(request, response, false);
			return;
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizFB.jsp")) {
			String questionPreText = request.getParameter("pre");
			String questionPostText = request.getParameter("post");
			String questionText = questionPreText + " _________ " + questionPostText;

			Question question = null;
			try {
				question = createQuestion(request, questionText, Question.QTYPE_FB);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			String answerText = request.getParameter("blank");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText = answerText + "~" + request.getParameter("otherResponses");
			}
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			//Last thing:
			askForNextQuestion(request, response, false);
			return;
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizMC.jsp")) {
			String questionText = request.getParameter("question");
			Question question = null;
			try {
				question = createQuestion(request, questionText, Question.QTYPE_MC);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			String answerText = request.getParameter("mc_correct") + 
				"~!" + request.getParameter("mc1") +
				"~!" + request.getParameter("mc2") +
				"~!" + request.getParameter("mc3");
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			//Last thing:
			askForNextQuestion(request, response, false);
			return;
		} //else if
		else if(request.getParameter("origin").equals("CreateQuizPR.jsp")) {
			String questionText = request.getParameter("question");

			Question question = null;
			try {
				question = createQuestion(request, questionText, Question.QTYPE_PR);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
			
			String answerText = request.getParameter("response");
			if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
				answerText = answerText + "~" + request.getParameter("otherResponses");
			}
			
			try {
				createAnswer(request, answerText, question);
			} catch (SQLException e) {
				e.printStackTrace();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			//Last thing:
			askForNextQuestion(request, response, false);
			return;
		} //else if
		
		else {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreateQuiz.jsp");
			requestDispatcher.forward(request, response);
			return;
		} //else
	} //doPost

	private void createAnswer(HttpServletRequest request, String answerText, Question question) throws SQLException {
		
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

		//Getting the quiz
		Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
		
		String[] multiAnswers = answerText.split("~");
		for (String thisAnswer : multiAnswers) {
			dbConnection.addAnswer(thisAnswer,  quiz.getQuizID(), question.getQuestionId());
		} //for
		
		//Populate all of the answers that we just put into the db to the question
		question.addAnswers(dbConnection); 
	}

	private Question createQuestion(HttpServletRequest request, String questionText, int qType) throws SQLException {
		//Create a question and then add it to the list of questions that exists in the quiz
		DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

		//Getting the quiz
		Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
		int questionID = dbConnection.addQuestion(questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID());

		Question question = null;
		
		if (qType == Question.QTYPE_QR )
			question = new QuestionResponseQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
		else if( qType == Question.QTYPE_FB)
			question = new FillBlankQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
		else if( qType == Question.QTYPE_MC)
			question = new MultipleChoiceQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
		else if( qType == Question.QTYPE_PR)
			question = new PictureResponseQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
		else
			return null;
			
		quiz.addQuestion(question);
		return question;
	}

	private void askForNextQuestion(HttpServletRequest request, HttpServletResponse response, 
			boolean firstQuestion) throws IOException, ServletException {
		StringBuilder options = new StringBuilder();
		options.append(
			"<option value=\"" + QTYPE_QR + "\">Question Response Question</option>" +
			"<option value=\"" + QTYPE_MC + "\">Multiple Choice Question</option>" +
			"<option value=\"" + QTYPE_FB + "\">Fill-In-The-Blank Question</option>" +
			"<option value=\"" + QTYPE_PR + "\">Picture Response Question</option>");
		
		if (!firstQuestion) {
			options.append("<option value=\"done\">No More Questions!</option>");
		}
		
		request.setAttribute("options", options.toString());
		request.getRequestDispatcher("askForNextQuestion.jsp").forward(request, response);
		return;
	}

	private Quiz createNewQuiz(HttpServletRequest request) throws SQLException {
		String quizName = (String) request.getParameter("quizName");
		ServletContext servletContext = this.getServletContext();
		DBConnection dbConnection = (DBConnection) servletContext.getAttribute("DBConnection");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || dbConnection == null) {
			return null;
		}
		int userID = user.getUserID();
		
		String[] checkBoxes = request.getParameterValues("quizParams");
		
		boolean singlePage = false;
		boolean randomOrder = false;
		boolean immediateCorrection = false;
		boolean practiceMode = false;
		
		if (checkBoxes != null && checkBoxes.length > 0) {
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
		}
		return new Quiz(quizName, userID, singlePage,randomOrder,immediateCorrection, practiceMode, dbConnection);
	}

}
