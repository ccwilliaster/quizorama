package quiz;

import java.util.Date;
import java.util.Random;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/* Questions, Order, Answer checking (after quiz or each question),
 * overall score, overall time
 */
public class Quiz {
	
	private static final int QUESTION_RESPONSE = 1;
	private static final int FILL_BLANK = 2;
	private static final int MULTIPLE_CHOICE = 3;
	private static final int PICTURE_RESPONSE = 4;

	/** Quiz attributes in the database */
	private int quizID;
	private String quizName;
	private Date quizCreation;
	private int quizCreatorUserID;
	private boolean singlePage;
	private boolean randomOrder;
	private boolean immediateCorrection;
	private boolean practiceMode;

	/** Necessary internal variables */
	private DBConnection connection;
	private ArrayList<Question> questionList;
	private boolean inPracticeMode; //Used for when the quiz is indeed in practice mode
	
	//MAY NOT NEED THIS, JUST IN CASE WE NEED QUESTION ORDER AFTER QUIZ IS OVER
	private ArrayList<Question> questionOrder;
	private int score;
	private int possibleScore;
	private long startTime; //SHOULD THIS BE A LONG/USING currentTimeMillis()
	private boolean question;
	private Question currQuestion;
	private Random rand = new Random();

	public Quiz(int quizID, DBConnection connection) throws SQLException {
		this.connection = connection;
		this.quizID = quizID;
		ResultSet quizInfo = connection.getQuizInformation(quizID);
		quizName = quizInfo.getString("quizName");
		quizCreation = quizInfo.getDate("quizCreation"); //NEED TO TEST THIS java.sql.date to java.util.date conversion
		quizCreatorUserID = quizInfo.getInt("quizCreatoruserID");
		singlePage = quizInfo.getBoolean("singlePage?");
		randomOrder = quizInfo.getBoolean("randomOrder?");
		immediateCorrection = quizInfo.getBoolean("immediateCorrection?");
	
		//CAN ALSO MAKE QUESTIONS 'ON THE FLY'
		populateQuestions();
	}

	/**
	 * This constructor takes in all of the fields necessary to create a quiz and then 
	 * updates the database with all of this information.
	 * @param quizName Name of the quiz
	 * @param quizCreatorID The userId of the user that created the quiz
	 * @param singlePage Whether the quiz is shown on a single page
	 * @param randomOrder Whether the quiz is shown in random order
	 * @param immediateCorrection Whether the quiz is corrected immediately
	 * @param practiceMode Whether the quiz can be taken in practice mode
	 * @param connection A pointer to the DBConnection object
	 * @throws SQLException
	 */
	Quiz(String quizName, int quizCreatorUserID, boolean singlePage, boolean randomOrder, boolean immediateCorrection, boolean practiceMode, DBConnection connection) throws SQLException {
		this.connection = connection;
		this.quizName = quizName;
		this.quizCreation = null;
		this.quizCreatorUserID = quizCreatorUserID;
		this.singlePage = singlePage;
		this.randomOrder = randomOrder;
		this.immediateCorrection = immediateCorrection;
		this.practiceMode = practiceMode;
		this.quizID = -1;
		questionList = new ArrayList<Question>();
		
		connection.addQuiz(this); //This should also set the quizID field
				
		//CAN ALSO MAKE QUESTIONS 'ON THE FLY'
		populateQuestions(); //This will not add anything to the list if nothing exists.
	}
	
	private void populateQuestions() throws SQLException {
		ResultSet questions = connection.getQuizQuestions(quizID);
		while (questions.next()) {
			int questionID = questions.getInt("questionID");
			String questionText = questions.getString("question");
			int questionType = questions.getInt("questionType");
			int questionNum = questions.getInt("questionNum");
			//ResultSet questionInfo = connection.getQuestionInfo(questionID); USE THIS TO TELL QUESTION WHAT TYPE IT IS
			Question currQuestion = getQuestionObject(questionID, questionText, questionType, questionNum);
			questionList.add(currQuestion);
		}
	} //populateQuestions
	
	//ali's added method
	private Question getQuestionObject(int questionID, String questionText, int questionType, int questionNum) throws SQLException {
		Question q = null;
		switch (questionType) {
			case QUESTION_RESPONSE:
				q = new QuestionResponseQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
				break;
			case FILL_BLANK:
				q = new FillBlankQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
				break;
			case MULTIPLE_CHOICE:
				q = new MultipleChoiceQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
				break;
			case PICTURE_RESPONSE:
				q = new PictureResponseQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
				break;
			default:
				q = new QuestionResponseQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
		}
		return q;
	}

	
	/* Initialize instance variables */
	public void startQuiz() {
		score = 0;
		possibleScore = 0;
		startTime = System.currentTimeMillis();
		question = true;
	}
	
	/* Updates the quiz history in the db and return an HTML
	 * 	printable string for the results.
	 */
	public String getResultsSummary(int userID) throws SQLException{
		//NEED TO FIGURE OUT HOW TO GET USERID HERE OR IN DBCONNECTION
		if (!connection.addQuizHistory(quizID, userID, score)) throw new SQLException();
		
		String result = "<p> You scored " + score + " out of a possible " + possibleScore + " points.</p>";
		long end = System.currentTimeMillis();
		long elapsed = end - startTime;
		long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
		long minutes =  TimeUnit.MILLISECONDS.toMinutes(elapsed) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed));
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		result += "\n<p> You took " + time + " to complete the quiz.</p>";
		//MOST LIKELY NEED TO TRACK INCORRECT QUESTIONS TOO TO GIVE BACK HERE
		return result;
	}

	/* Returns true if the quiz has more to display (either a Q or
	 * 	A), and false if the quiz is over.
	 */
	public boolean hasMoreHTML() {
		return !(question && questionList.size() == 0);
	}

	/* Returns a string that could be one of two things:
	 * 	1. a question to display with some type of user input
	 * 	2. the answer to the previous question (if quiz is set up 
	 * 		for immediate feedback)
	 */
	public String getNextHTML() {
		if (immediateCorrection) {
			boolean tempQuest = question;
			question = !question;
			if (tempQuest) {
				currQuestion = getNextQuestion();
				return currQuestion.showQuestion();
			} else {
				return currQuestion.showAnswerOptions();
			}
		} else {
			currQuestion = getNextQuestion();
			return currQuestion.showQuestion();
		}
	}
	
	/* Returns the next question in the quiz. This could be done in
	 * 	sequential order or random order. This is done by removing the
	 * 	current question from the questionList and adding it to the question
	 * 	order list.
	 */
	private Question getNextQuestion() {
		int index = randomOrder ? rand.nextInt(questionList.size()) : 0;
		Question result = questionList.get(index);
		questionOrder.add(result); //MAY NOT NEED THIS
		questionList.remove(index);
		return result;
	}

	/* Takes in an HTTP request that has the answers submitted in
	 * 	the form.
	 */
	public void sendAnswers(HttpServletRequest request) {
		ArrayList<String> locations = currQuestion.getAnswerLocations();
		ArrayList<String> submittedAnswers = new ArrayList<String>();
		for (int i = 0; i < locations.size(); i++) {
			submittedAnswers.add(request.getParameter(locations.get(i)));
		}
		int qScore = currQuestion.checkAnswers(submittedAnswers);
		score += qScore;
		possibleScore += currQuestion.possiblePoints();
	}

	/** Quiz history info 
	 * @throws SQLException */
	/* Returns an arrayList or the categories associated
	 * 	with this quiz.
	 */
	public ArrayList<String> getCategories() throws SQLException {	
		ArrayList<String> result = new ArrayList<String>();
		ResultSet categories = connection.getCategories(quizID);
		while (categories.next()) {
			result.add(categories.getString("categoryName"));
		}
		return result;
	}
	/* Returns an arrayList of the tags associated with
	 * 	this quiz.
	 */
	public ArrayList<String> getTags() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet tags = connection.getTags(quizID);
		while(tags.next()) {
			result.add(tags.getString("tagName"));
		}
		return result;
	} 
	/* Returns an integer corresponding to the average rating of this
	 * 	quiz.
	 */
	public double getRating() throws SQLException{	
		int numValues = 0;
		int total = 0;
		ResultSet ratings = connection.getRatings(quizID);
		while (ratings.next()) {
			total += ratings.getInt("ratingValue");
			numValues++;
		}
		return numValues == 0 ? 0 : total / (double)numValues;
	}

	/*
	 * Returns the rating the User with the specified userID provided for this
	 * Quiz. Returns null if the User has not rated
	 */
	public Integer getUserRating(int userID) throws SQLException{	
		int currUserID;
		Integer rating = null;
		
		ResultSet ratings = connection.getRatings(quizID);
		while (ratings.next()) {
			currUserID = ratings.getInt("userID");
			if ( userID == currUserID ) { 
				rating = ratings.getInt("ratingValue");
			}
		}
		return rating;
	}
	/*
	 * Returns the ratingID of the rating the User with the specified userID 
	 * provided for this Quiz. Returns null if the User has not rated
	 */
	public Integer getUserRatingID(int userID) throws SQLException{	
		int currUserID;
		Integer ratingID = null;
		
		ResultSet ratings = connection.getRatings(quizID);
		while (ratings.next()) {
			currUserID = ratings.getInt("userID");
			if ( userID == currUserID ) { 
				ratingID = ratings.getInt("ratingID");
			}
		}
		return ratingID;
	}
	
	/* Returns the number of reviews for this quiz.
	 */
	public int getNumReviews() throws SQLException {	
		int numValues = 0;
		ResultSet ratings = connection.getRatings(quizID);
		while (ratings.next()) numValues++;
		return numValues;
	}
	/* Returns the average score of the user for this quiz.
	 */
	public double getAverageScore() throws SQLException {	
		int total = 0;
		int numScores = 0;
		ResultSet histories = connection.getHistories(quizID);
		while (histories.next()) {
			total += histories.getInt("score");
			numScores++;
		}
		return numScores == 0 ? 0 : total / numScores;
	}
	/*
	 * Returns the number of questions this quiz has
	 */
	public int getNumQuestions() throws SQLException {
		int total = 0;
		ResultSet questions = connection.getQuizQuestions(quizID);
		while ( questions.next() ) {
			total++;
		}
		return total;
	}
	/* Returns a string to display a summary before the quiz 
	 * 	begins.
	 */
	public String getQuizSummary() {	
		String result = quizName + " was created on " + quizCreation + ".\n"; //COULD ADD WHO CREATED IT
		result += "There are " + questionList.size() + " questions on this quiz.\n";
		if (singlePage) {
			result += "All of the questions will be on the same page.\n";
		}
		if (randomOrder) {
			result += "The questions will come in random order.\n";
		}
		if (immediateCorrection && !singlePage) {
			result += "The answers will be displayed after every question.\n";
		} else if (!immediateCorrection){
			result += "The answers will be displayed after the quiz is over.\n";
		}
		return result;
	}

	/** Getters for the fields for a quiz in the DB */
	/* Returns the quizName */
	public String getQuizName() {
		return quizName;
	}
	/* Returns the quizCreatoruserID*/
	public int getquizCreatoruserID() {
		return quizCreatorUserID;
	}
	/* Returns the singlePage */
	public boolean getSinglePage() {
		return singlePage;
	}
	/* Returns the randomOrder */
	public boolean getRandomOrder() {
		return randomOrder;
	}
	/* Returns immediateCorrection */
	public boolean getImmediateCorrection() {
		return immediateCorrection;
	}
	
	/* Returns immediateCorrection */
	public boolean getPractiveMode() {
		return practiceMode;
	}

	/* Returns the quizCreation */
	public Date getQuizCreation() {
		return quizCreation;
	}

	/**
	 * @return the quizID
	 */
	public int getQuizID() {
		return quizID;
	}

	/**
	 * @param quizID the quizID to set
	 */
	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	/**
	 * @param quizCreation the quizCreation to set
	 */
	public void setQuizCreation(Date quizCreation) {
		this.quizCreation = quizCreation;
	}

	public int getNextQuestionNum() {
		return questionList.size() + 1;
	}

	public void addQuestion(Question question) {
		questionList.add(question);
	}

	/**
	 * @return the inPracticeMode
	 */
	public boolean isInPracticeMode() {
		return inPracticeMode;
	}

	/**
	 * @param inPracticeMode the inPracticeMode to set
	 */
	public void setInPracticeMode(boolean inPracticeMode) {
		this.inPracticeMode = inPracticeMode;
	}


}
