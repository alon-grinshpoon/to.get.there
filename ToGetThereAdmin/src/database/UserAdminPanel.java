package database;
import java.util.Calendar;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;



@DynamoDBTable(tableName = "registeredUsers")
public class UserAdminPanel implements DynamoDBMarshaller<UserAdminPanel>{
	
	private String mId;
	private String mFirstName;
	private String mLastName;
	private String mBirthday;	

	private String mLocation = "Tel Aviv-Yafo, Israel";
	private int mPoints = 0;
	private int mLevel = 1;
	
	private boolean isVolunteering = true;
	private String mEmail = "alongrinshpon@gmail.com";
	private String mPhone = "0546606668";
	
	private final int ID = 0;
	private final int FIRSTNAME = 1;
	private final int LASTNAME = 2;
	private final int BIRTHDAY = 3;
	private final int EMAIL = 4;
	//private final int PHONE_PREFIX = 5;
	private final int PHONE = 5;
	private final int LOCATION = 6;
//	public void setUser(GraphUser userFacebook) {
//		mUser = userFacebook;
//		mFirstName = mUser.getFirstName();
//		mLastName = mUser.getLastName();
//		mBirthday = mUser.getBirthday();
//		mId = mUser.getId();
//		
//	}
	public UserAdminPanel() {
		
	}
	public UserAdminPanel(String firstname, String lastname, String birthday, String id, String email, String phone, String location) {
		
		mFirstName = firstname;
		mLastName = lastname;
		mBirthday = birthday;
		mId = id;
		mEmail = email;
		mPhone = phone;
		mLocation = location;
	}

	@DynamoDBHashKey(attributeName = "userId")
	public  String getID() {
		return mId;
	}
	public  void setID(String id){
		mId = id;
	}
	
	@DynamoDBAttribute(attributeName = "firstname")
	public  String getFirstName() {
		return mFirstName;
	}
	public  void setFirstName(String firstName) {
		mFirstName = firstName;
	}
	
	@DynamoDBAttribute(attributeName = "lastname")
	public  String getLastName() {
		return mLastName;
	}
	public  void setLastName(String lastName) {
		mLastName = lastName;
	}
	
	@DynamoDBAttribute(attributeName = "birthday")
	public  String getBirthday() {
		return mBirthday;
	}
	public void setBirthday(String birthday) {
		mBirthday = birthday;
	}
	
	@DynamoDBAttribute(attributeName = "email")
	public  String getEmail() {
		return mEmail;
	}
	public  void setEmail(String email) {
		mEmail = email;
	}
	@DynamoDBAttribute(attributeName = "phone")
	public  String getPhone() {
		return mPhone;
	}
	public  void setPhone(String phone) {
		mPhone = phone;
	}
	@DynamoDBAttribute(attributeName = "isVolunteer")
	public  boolean isVolunteering() {
		return isVolunteering;
	}
	public  void setVolunteering(boolean bool) {
		isVolunteering = bool;
	}

	@DynamoDBAttribute(attributeName = "points")
	public  int getPoints() {
		return mPoints;
	}
	public  void setPoints(int points) {
		mPoints = points;
	}

	@DynamoDBAttribute(attributeName = "level")
	public  int getLevel() {
		return mLevel;
	}
	public  void setLevel(int level) {
		mLevel = level;
	}
	
	@DynamoDBAttribute(attributeName = "location")
	public  String getLocation(){
		if (mLocation!= null) {
			return mLocation;
		} else {
			return "";
		}
	}
	public void setLocation (String location) {
		mLocation = location;
	}
	

	public String userAge() {
		String mAge = "0";
		if (!mBirthday.equals("")) {
			String[] str = mBirthday.split(".");
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			Integer age  =  currentYear - Integer.parseInt(str[2]);
			mAge = age.toString();
		}
		return mAge;
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof UserAdminPanel))return false;
		return mId.equals(((UserAdminPanel)other).getID());
	}
	@Override
	public String marshall(UserAdminPanel user) {
		
		return user.getID() + "$" + user.getFirstName() + "$" + user.getLastName() + "$" +
					user.getBirthday() + "$" + user.getEmail() + "$" + user.getPhone() + "$" +user.getLocation();
	}
	
	@Override
	public UserAdminPanel unmarshall(Class<UserAdminPanel> arg0, String s) {
		String[] str = s.split("\\$");
		UserAdminPanel user = new UserAdminPanel(str[FIRSTNAME], str[LASTNAME], str[BIRTHDAY], str[ID], str[EMAIL], str[PHONE], str[LOCATION]);
		return user;
	}

}
