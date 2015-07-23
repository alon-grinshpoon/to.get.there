package il.co.togetthere.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import il.co.togetthere.db.User;

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

	public String getID() {
		return mID;
	}
	public void setID(String id){
		mID = id;
	}

	public User getTaskOpener() {
		return mTaskOpener;
	}
	public void setTaskOpener(User taskOpener) {
		mTaskOpener = taskOpener;
	}

	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}

	public String getBody() {
		return mBody;
	}
	public void setBody(String body) {
		mBody = body;
	}

	public String getDate() {
		return mDate;
	}
	public void setDate(String date) {
		mDate = date;
	}

	public Set<String> getUnParsedVolunteers() {
		return mUnParsedVolunteers;
	}
	public void setUnParsedVolunteers(Set<String> volunteers) {
		mUnParsedVolunteers = volunteers;
		parseVolunteers();
	}

	public  String getAttachedUserToTask() {
		return mAttachedUserToTaskID;
	}
	public  void setAttachedUserToTask(String attchedUserID) {
		mAttachedUserToTaskID = attchedUserID;
	}
	public void addVolunteer (User volunteer) {
		mVolunteers.add(volunteer);
		if (mUnParsedVolunteers == null) mUnParsedVolunteers = new HashSet<String>();
		//TODO mUnParsedVolunteers.add(volunteer);
		
	}
	public ArrayList<User> volunteers() {
		return mVolunteers;
	}

	// TODO
	public void addTaskToDB() {
		// DynamoDBManagerTask registerUserToDB = new DynamoDBManagerTask();
		// registerUserToDB.mTask = this;
		// registerUserToDB.execute(DynamoDBManagerType.ADD_TASK.toString());
	}
	// TODO
	public void removeTaskFromDB() {
		// DynamoDBManagerTask registerUserToDB = new DynamoDBManagerTask();
		// registerUserToDB.mTask = this;
		// registerUserToDB.execute(DynamoDBManagerType.REMOVE_TASK.toString());

	}

	// TODO
	private void parseVolunteers() {
		//Class<User> clazz = null;
		//for (String str : mUnParsedVolunteers) {
		//	User user = new User().unmarshall(clazz, str);
		//	mVolunteers.add(user);
		//}
	}
	
}
