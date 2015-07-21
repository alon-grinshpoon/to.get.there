package il.co.togetthere;

import il.co.togetthere.db.DynamoDBManagerTask;
import il.co.togetthere.db.DynamoDBManagerType;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@DynamoDBTable(tableName = "registeredUsers")
public class User implements DynamoDBMarshaller<User> {

	private GraphUser mUser;

	private String mId;
	private String mFirstName;
	private String mLastName;
	private String mBirthday; // YYYY-MM-DD
	private String mLocation = "Tel Aviv-Yafo, Israel"; //TODO get from FB
	private int mPoints = 0;
	private int mLevel = 1;
	private String mEmail = "alongrinshpon@gmail.com";  //TODO get from FB
	private String mPhone = "054-6606668";  //TODO get from FB
	private boolean isVolunteering = true;
	private boolean mWasAskedToVolunteer;
	private double mLatitude;
	private double mLongitude;

	private final int ID = 0;
	private final int FIRSTNAME = 1;
	private final int LASTNAME = 2;
	private final int BIRTHDAY = 3;
	private final int EMAIL = 4;
	private final int PHONE = 5;
	private final int LOCATION = 6;
	
	public User(){
		super();
	}
	
	public User(String firstname, String lastname, String birthday, String id,
			String email, String phone, String location) {

		mFirstName = firstname;
		mLastName = lastname;
		mBirthday = birthday;
		mId = id;
		mEmail = email;
		mPhone = phone;
		mLocation = location;
	}

	public void init(GraphUser user) {
		this.mUser = user;
		if (user != null) {
			mFirstName = user.getFirstName();
			mLastName = user.getLastName();
			mBirthday = user.getBirthday();
			mId = user.getId();
			//gets users current location from facebook
			GraphPlace location = user.getLocation();
			//if user doesn't allow location services, location is null
			if (location != null ) {
				mLatitude = location.getLocation().getLatitude();
				mLongitude = location.getLocation().getLongitude();
			} else {
			mLatitude = 0;
			mLongitude = 0;
			}
		//	syncDB();
		}

	}
	
	public GraphUser returnFacebookUser() {
		return mUser;
	}

	@DynamoDBHashKey(attributeName = "userId")
	public String getID() {
		return mId;
	}

	public void setID(String id) {
		mId = id;
	}

	@DynamoDBAttribute(attributeName = "firstname")
	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	@DynamoDBAttribute(attributeName = "lastname")
	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	@DynamoDBAttribute(attributeName = "birthday")
	public String getBirthday() {
		return mBirthday;
	}

	public void setBirthday(String birthday) {
		mBirthday = birthday;
	}

	@DynamoDBAttribute(attributeName = "email")
	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	@DynamoDBAttribute(attributeName = "phone")
	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	@DynamoDBAttribute(attributeName = "isVolunteer")
	public boolean isVolunteering() {
		return isVolunteering;
	}

	public void setVolunteering(boolean bool) {
		isVolunteering = bool;
	}

	@DynamoDBAttribute(attributeName = "points")
	public int getPoints() {
		return mPoints;
	}

	public void setPoints(int points) {
		mPoints = points;
	}

	@DynamoDBAttribute(attributeName = "level")
	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int level) {
		mLevel = level;
	}
	@DynamoDBAttribute(attributeName = "latitude")
	public double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}
	
	@DynamoDBAttribute(attributeName = "longitude")
	public double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}
	
	@DynamoDBAttribute(attributeName = "location")
	public String getLocation() {
		if (mLocation != null) {
			return mLocation;
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
		mLocation = location;
	}

	public boolean isLoggedIn() {
		return (mUser != null);
	}

	public boolean wasAskedToVolunteer() { return mWasAskedToVolunteer; }

	public void setWasAskedToVolunteer(boolean wasAskedToVolunteer) {  mWasAskedToVolunteer = wasAskedToVolunteer; }

	public void syncDB() {
		DynamoDBManagerTask registerUserToDB = new DynamoDBManagerTask();
		registerUserToDB.mUser = this;
		registerUserToDB.execute(DynamoDBManagerType.REGISTER_USER.toString());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof User))
			return false;
		return mId.equals(((User) other).getID());
	}

	@Override
	public String marshall(User user) {

		return user.getID() + "$" + user.getFirstName() + "$"
				+ user.getLastName() + "$" + user.getBirthday() + "$"
				+ user.getEmail() + "$" + user.getPhone() + "$"
				+ user.getLocation();
	}

	@Override
	public User unmarshall(Class<User> arg0, String s) {
		String[] str = s.split("\\$");
		User user = new User(str[FIRSTNAME], str[LASTNAME], str[BIRTHDAY],
				str[ID], str[EMAIL], str[PHONE], str[LOCATION]);

		return user;
	}

}
