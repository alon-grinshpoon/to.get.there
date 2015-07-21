package il.co.togetthere;



import il.co.togetthere.db.DynamoDBManagerTask;
import il.co.togetthere.db.DynamoDBManagerType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "tasks")
public class Task {

	private String mID;
	private String mTitle;
	private String mBody;
	private String mDate;
	private User mTaskOpener;
	private ArrayList<User> mVolunteers;
	private Set<String> mUnParsedVolunteers;
	private String mAttachedUserToTaskID;
	
	public Task() {
		mVolunteers = new ArrayList<User>();
	}
	
	public Task (String title, String body, User taskOpener) {
		setTitle(title);
		mBody = body;
		mTaskOpener = taskOpener;
		mVolunteers = new ArrayList<User>();
		//mUnParsedVolunteers = new HashSet<String>();
	}
	
	@DynamoDBHashKey(attributeName = "Id")
	@DynamoDBAutoGeneratedKey
	public String getID() {
		return mID;
	}
	public void setID(String id){
		mID = id;
	}
	
	@DynamoDBMarshalling (marshallerClass = User.class)
	public User getTaskOpener() {
		return mTaskOpener;
	}
	public void setTaskOpener(User taskOpener) {
		mTaskOpener = taskOpener;
	}
	
	@DynamoDBAttribute(attributeName = "title")
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	
	@DynamoDBAttribute(attributeName = "body")
	public String getBody() {
		return mBody;
	}
	public void setBody(String body) {
		mBody = body;
	}
	
	@DynamoDBAttribute(attributeName = "date")
	public String getDate() {
		return mDate;
	}
	public void setDate(String date) {
		mDate = date;
	}
	
	@DynamoDBAttribute(attributeName = "volunteers")
	public Set<String> getUnParsedVolunteers() {
		return mUnParsedVolunteers;
	}
	public void setUnParsedVolunteers(Set<String> volunteers) {
		mUnParsedVolunteers = volunteers;
		parseVolunteers();
	}
	
	@DynamoDBAttribute(attributeName = "attachedUserToTask")
	public  String getAttachedUserToTask() {
		return mAttachedUserToTaskID;
	}
	public  void setAttachedUserToTask(String attchedUserID) {
		mAttachedUserToTaskID = attchedUserID;
	}
	public void addVolunteer (User volunteer) {
		mVolunteers.add(volunteer);
		if (mUnParsedVolunteers == null) mUnParsedVolunteers = new HashSet<String>();
		mUnParsedVolunteers.add(volunteer.marshall(volunteer));
		
	}
	public ArrayList<User> volunteers() {
		return mVolunteers;
	}
	
	public void addTaskToDB() {
		DynamoDBManagerTask registerUserToDB = new DynamoDBManagerTask();
		registerUserToDB.mTask = this;
		registerUserToDB.execute(DynamoDBManagerType.ADD_TASK.toString());
	}
	public void removeTaskFromDB() {
		DynamoDBManagerTask registerUserToDB = new DynamoDBManagerTask();
		registerUserToDB.mTask = this;
		registerUserToDB.execute(DynamoDBManagerType.REMOVE_TASK.toString());
		
	}
	
	private void parseVolunteers() {
		Class<User> clazz = null;
		for (String str : mUnParsedVolunteers) {
			User user = new User().unmarshall(clazz, str);
			mVolunteers.add(user);
		}		
	}
	
}