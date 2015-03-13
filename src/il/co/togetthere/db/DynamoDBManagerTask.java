
package il.co.togetthere.db;

import il.co.togetthere.Task;
import il.co.togetthere.User;
import il.co.togetthere.db.DynamoDBManager;
import android.os.AsyncTask;


public class DynamoDBManagerTask extends AsyncTask<String, Void, DynamoDBManagerTaskResult> {
	public AsyncResponse delegate=null;
	public ServiceProvider mSerivceProvider;
	public Task mTask;
	public User mUser;
	public DynamoDBManagerTaskResult doInBackground(
			String... types) {

		@SuppressWarnings("unused")
		String tableStatus = "serviceProvider";
		
		DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
		result.setTaskType(stringToEnum(types[0]));

	 if (types[0].equals(DynamoDBManagerType.INSERT_SERVICE_PROVIDER.toString())) {
		 	if (mSerivceProvider != null)
		 		DynamoDBManager.addObjectToDB(mSerivceProvider);
	 }
	 

	 else if (types[0].equals(DynamoDBManagerType.GET_PROVIDER.toString()) && types.length > 1) {
		 if (types[1].equals(ServiceProviderType.Medical.toString()))
			 result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.Medical);
		 else if (types[1].equals(ServiceProviderType.Restaurants.toString()))
			 result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.Restaurants);
	 	 else if (types[1].equals(ServiceProviderType.Shopping.toString())) 
	 		result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.Shopping);
		 else if (types[1].equals(ServiceProviderType.Help.toString()))
			result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.Help);
	   	 else if (types[1].equals(ServiceProviderType.PublicServices.toString()))
			result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.PublicServices);
	   	 else if (types[1].equals(ServiceProviderType.Transportation.toString()))
			result.mServiceProviderArray = DynamoDBManager.getServiceProvider(ServiceProviderType.Transportation);
	 }
	 else if (types[0].equals(DynamoDBManagerType.REGISTER_USER.toString())) {
		 if (mUser != null) DynamoDBManager.registerUser(mUser);
	 }
	 else if (types[0].equals(DynamoDBManagerType.ADD_TASK.toString())) {
		 if (mTask != null) DynamoDBManager.addObjectToDB(mTask);
	 }
	 else if (types[0].equals(DynamoDBManagerType.GET_TASKS.toString())) {
		 result.mTaskArray = DynamoDBManager.getTasks();
		 result.setTaskType(DynamoDBManagerType.GET_TASKS);
	 }
		return result;
	}

	protected void onPostExecute(DynamoDBManagerTaskResult result) {

		if (result.getTaskType() == DynamoDBManagerType.INSERT_SERVICE_PROVIDER) {
//			Toast.makeText(DisplayMessageActivity.this,
//					"Users inserted successfully!", Toast.LENGTH_SHORT).show();
		}
		
		if (result.getTaskType() == DynamoDBManagerType.GET_PROVIDER || result.getTaskType() == DynamoDBManagerType.GET_TASKS) {
			delegate.processFinish(result);
		}
				
	}
	
	public DynamoDBManagerType stringToEnum (String type) {
	
		if (type.equals("GET_PROVIDER")) return DynamoDBManagerType.GET_PROVIDER;
		else if (type.equals("INSERT_SERVICE_PROVIDER"))  return DynamoDBManagerType.INSERT_SERVICE_PROVIDER;
		
		return null;
	}
}