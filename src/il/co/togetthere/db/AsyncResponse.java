package il.co.togetthere.db;

public interface AsyncResponse {
	void processFinish(DynamoDBManagerTaskResult result);
}
