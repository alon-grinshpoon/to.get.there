package il.co.togetthere.db;

public class Review {

	private String title;
	private String reviewer;
	private String text;
	private int points;

	/*
	 * Constructor
	 */
	public Review(String title, String reviewer, String text, int points) {
		this.title = title;
		this.reviewer = reviewer;
		this.text = text;
		this.points = points;
	}

	/*
	 * Setters and Getters
	 */

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
