package database;


public class ReviewObj {
	private String _title;
	private String _reviewer;
	private String _review;
	private int _points;
	
	public ReviewObj () {
		new ReviewObj("","","",0);
	}
	public ReviewObj (String reviewer, String title,String review, int points) {
		_review = review;
		_points = points;
		_reviewer = reviewer;
		_title = title;
	}

	public String getReviewer() {
		return _reviewer;
	}
	public String getTitle() {
		return _title;
	}
	public String getReview() {
		return _review;
	}
	public int getPoints() {
		return _points;
	}
	public void setReviewer(String reviewer) {
		this._reviewer = reviewer;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	public void setReview(String review) {
		this._review = review;
	}
	public void setPoints(int points) {
		this._points = points;
	}
	public void addPoints(int points) {
		_points += points;
	}
}

