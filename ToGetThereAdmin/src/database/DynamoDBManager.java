package database;
import java.util.ArrayList;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;



public class DynamoDBManager {
	 /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (~/.aws/credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WANRNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonDynamoDBClient dynamoDBClient;

    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    public static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        dynamoDBClient = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDBClient.setRegion(usWest2);
    }

    private static void addToDB (Object obj) {
    	  DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
          try { 
                  mapper.save(obj);
        	  		
          } catch (AmazonServiceException ex) {
        	  System.out.println ("Error message: "+ ex.getMessage());
          }
    }
    
    public static void addTaskToDB(TaskAdminPanel t) {
    	addToDB(t);
    }
    
    public static ArrayList<TaskAdminPanel> getTasks() {
    	ArrayList<TaskAdminPanel> tasks = new ArrayList<TaskAdminPanel>();
    	DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<TaskAdminPanel> result = mapper.scan(TaskAdminPanel.class, scanExpression);
            for (TaskAdminPanel up : result) tasks.add(up);
        } catch (AmazonServiceException ex) {
        	 System.out.println ("Error message: "+ ex.getMessage());
        }
        return tasks;
    }

	public static void addServiceProvider(ServiceProvider sp) {
		addToDB(sp);
		System.out.println("SP name inserted: "+sp.getName());
		
	}
}
