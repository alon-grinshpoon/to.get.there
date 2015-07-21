package il.co.togetthere.db;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class User {

	private transient GraphUser facebookUser;
	private String facebook_id;

	private String id;
	private String first_name;
	private String last_name;
	private String birthday;	// Format: YYYY-MM-DD
	private String location;
	private int points = 0;
	private int level = 1;
	private String email;
	private String phone;
	private boolean isVolunteering = true;
	private boolean wasAskedToVolunteer;
	private transient double latitude;
	private transient double longitude;

	/*
	 * Empty Constructor
	 */
	public User(){

	}

	/*
	 * Constructor
	 */
	public User(String first_name, String last_name, String birthday,
			String email, String phone, String location) {

		this.first_name = first_name;
		this.last_name = last_name;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.location = location;
	}

	// Initialize user from Facebook
	public void init(GraphUser facebookUser) {
		// Set facebook user
		this.facebookUser = facebookUser;

		if (facebookUser != null) {
			// Set Facebook ID
			this.facebook_id = facebookUser.getId();
			// Set name
			this.first_name = facebookUser.getFirstName();
			this.last_name = facebookUser.getLastName();
			// Set Birthday
			this.birthday = (facebookUser.getBirthday() != null) ? facebookUser.getBirthday() : "1900-01-01";
			// Set Email
			this.email = "";
			// Set Phone
			this.phone = "";
			// Gets users current location from Facebook
			// If user doesn't allow location services, location is null
			GraphPlace location = facebookUser.getLocation();
			if (location != null ) {
				this.location = location.toString();
				this.latitude = location.getLocation().getLatitude();
				this.longitude = location.getLocation().getLongitude();
			} else {
				this.location = "Unknown";
				this.latitude = 0;
				this.longitude = 0;
			}
		}

	}
	
	public GraphUser returnFacebookUser() {
		return facebookUser;
	}

	public String getFacebook_id() {
		return facebook_id;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		id = id;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return this.last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isVolunteering() {
		return isVolunteering;
	}

	public void setVolunteering(boolean bool) {
		isVolunteering = bool;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) { this.longitude = longitude; }

	public String getLocation() {
		if (location != null) {
			return location;
		} else {
			return "";
		}
	}

	public String getFullName(){
		return getFirstName() + " " + getLastName();
	}

	public int getAge(){
		int year = Integer.parseInt(getBirthday().substring(0, 4));
		int month = Integer.parseInt(getBirthday().substring(5, 7));
		int day = Integer.parseInt(getBirthday().substring(8, 10));
		Calendar dob = new GregorianCalendar();
		dob.set(year + 1900, month, day);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR))
			age--;
		return age;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isLoggedIn() { return (facebookUser != null); }

	public boolean wasAskedToVolunteer() { return wasAskedToVolunteer; }
	public void setWasAskedToVolunteer(boolean wasAskedToVolunteer) {  this.wasAskedToVolunteer = wasAskedToVolunteer; }

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof User))
			return false;
		return id.equals(((User) other).getID());
	}

	@Override
	public String toString() {
		return "User " + getID() + ": " + getFullName();
	}
}
