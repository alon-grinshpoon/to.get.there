package il.co.togetthere.server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.User;

public class AsyncRequest extends AsyncTask<Object, Void, AsyncResult> {

    Context applicationContext;
    AsyncResponse sendResponseTo;

    /* Constructor */
    public AsyncRequest(Context applicationContext, AsyncResponse sendResponseTo){
        this.applicationContext = applicationContext;
        this.sendResponseTo = sendResponseTo;
    }

    @Override
    protected AsyncResult doInBackground(Object... objects) {

        // Define result
        AsyncResult result = new AsyncResult();

        // Get server action (Always the first parameter)
        int serverAction = (int) objects[0];

        // Perform server action
        switch (serverAction){
            case Server.SERVER_ACTION_GET_SPS_OF_CATEGORY:
                // Parse parameter
                ServiceProviderCategory category = (ServiceProviderCategory) objects[1];
                // Run server action
                List<ServiceProvider> serviceProviderList = null;
                try {
                    serviceProviderList = Server.getSPsOfCategory(category);
                    // Configure result
                    result.setResultCategory(category);
                    result.setServiceProviderList(serviceProviderList);
                } catch (IOException e) {
                    // Configure result as error
                    result.setError(true);
                }
                break;
            case Server.SERVER_ACTION_REGISTER_USER:
                // Parse parameter
                User user = (User) objects[1];
                // Run server action
                try {
                    Server.registerUser(user);
                } catch (IOException e) {
                    // Configure result as error
                    result.setError(true);
                }
                break;
            default:
                break;

        }

        return result;
    }

    protected void onPostExecute(AsyncResult result) {
        sendResponseTo.handleResult(result);
    }
}
