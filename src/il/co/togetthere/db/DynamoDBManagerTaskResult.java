package il.co.togetthere.db;

import il.co.togetthere.Task;

import java.util.ArrayList;


public class DynamoDBManagerTaskResult {
	private DynamoDBManagerType taskType;
	public ArrayList<ServiceProvider> mServiceProviderArray = new ArrayList<ServiceProvider>();
	public ArrayList<Task> mTaskArray = new ArrayList<Task>();

	public DynamoDBManagerType getTaskType() {
		return taskType;
	}

	public void setTaskType(DynamoDBManagerType taskType) {
		this.taskType = taskType;
	}
	
}