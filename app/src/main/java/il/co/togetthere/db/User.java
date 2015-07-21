package il.co.togetthere.db;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class User {

	private GraphUser fbUser;

	private String id;
	private String firstName;
	private String lastName;
	private String birthday;	// Format: YYYY-MM-DD
	private String location;
	private int points = 0;
	private int level = 1;
	private String email;
	private String phone;
	private boolean isVolunteering = true;
	private boolean wasAskedToVolunteer;
	private double latitude;
	private double longitude;

	/*
	 * Empty Constructor
	 */
	public User(){

	}

	/*
	 * Constructor
	 */
	public User(String firstName, String lastName, String birthday,
			String email, String phone, String location) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.location = location;
	}

	// Initialize user from Facebook
	public void init(GraphUser user) {
		this.fbUser = user;
		if (user != null) {
			this.firstName = user.getFirstName();
			this.lastName = user.getLastName();
			birthday = (user.getBirthday() != null) ? user.getBirthday() : "0000-00-00";
			id = user.getId();
			// Gets users current location from Facebook
			GraphPlace location = user.getLocation();
			// If user doesn't allow location services, location is null
			if (location != null ) {
				latitude = location.getLocation().getLatitude();
				longitude = location.getLocation().getLongitude();
			} else {
				latitude = 0;
				longitude = 0;
			}
		}

	}
	
	public GraphUser returnFacebookUser() {
		return fbUser;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public boolean isLoggedIn() { return (fbUser != null); }

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
}
