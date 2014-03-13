package quiz;

public class Rating {

	private int quizID;
	private int userID;
	private int rating;
	private String review;
	
	public Rating(int quizID, int userID, int rating, String review) {
		this.quizID = quizID;
		this.userID = userID;
		this.rating = rating;
		this.review = review;
	}
	
	public int getQuizID() {
		return quizID;
	}
	public int getUserID() {
		return userID;
	}
	public int getRating() {
		return rating;
	}
	public String getReview() {
		return review;
	}
}
