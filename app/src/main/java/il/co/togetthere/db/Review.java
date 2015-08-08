package il.co.togetthere.db;

/**
 * Class representation of a review stored in the server.
 */
public class Review {

	private String id;
	private String title;
	private User user;
	private String content;
	private int likes;

	/*
	 * Constructor
	 */
	public Review(String id, User user, String content, int likes) {
		this.id = id;
		this.title = user.getFullName();
		this.user = user;
		this.content = content;
		this.likes = likes;
	}

	/*
	 * Setters and Getters
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
}
