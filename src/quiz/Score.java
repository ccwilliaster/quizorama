package quiz;

public class Score {
	
	private int quizID;
	private int userID;
	private int score;
	private java.util.Date date;
	
	public Score(int quizID, int userID, int score, java.util.Date date) {
		this.quizID = quizID;
		this.userID = userID;
		this.score = score;
		this.date = date;
	}
	
	public int getQuizID() {
		return quizID;
	}
	public int getUserID() {
		return userID;
	}
	public int getScore() {
		return score;
	}
	public java.util.Date getDate() {
		return date;
	}
}
