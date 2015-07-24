package il.co.togetthere.server;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.User;

public class AsyncRequest extends AsyncTask<Object, Void, AsyncResult> {

    AsyncResponse sendResponseTo;

    /* Constructor */
    public AsyncRequest(AsyncResponse sendResponseTo){
        this.sendResponseTo = sendResponseTo;
    }

    @Override
    protected AsyncResult doInBackground(Object... objects) {

        // Define result
        AsyncResult result = new AsyncResult();

        // Get server action (Always the first parameter)
        int serverAction = (int) objects[0];

        // Define parameters
        User user;
        int userID;
        Review review;
        ServiceProvider serviceProvider;
        String query;
        ServiceProviderCategory category;

        // Perform server action
        switch (serverAction){

            case Server.SERVER_ACTION_GET_SPS_OF_CATEGORY:
                // Parse parameter
                category = (ServiceProviderCategory) objects[1];
                // Run server action
                List<ServiceProvider> serviceProviderList = null;
                try {
                    serviceProviderList = Server.getSPsOfCategory(category);
                    // Configure result
                    result.setResultCategory(category);
                    result.setServiceProviderList(serviceProviderList);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_REGISTER_USER:
                // Parse parameter
                user = (User) objects[1];
                // Run server action
                try {
                    Server.registerUser(user);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_EDIT_USER_BY_ID:
                // Parse parameter
                user = (User) objects[1];
                userID = (int) objects[2];
                // Run server action
                try {
                    Server.editUserByID(user, userID);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_ADD_LIKE_TO_REVIEW:
                // Parse parameter
                user = (User) objects[1];
                review = (Review) objects[2];
                // Run server action
                try {
                    Server.addLikeToReview(user, review);
                } catch (IOException e) {
                        // Configure result as error
                        result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_RANK_SP:
                // Parse parameter
                user = (User) objects[1];
                serviceProvider = (ServiceProvider) objects[2];
                int rank = (int) objects[3];
                // Run server action
                try {
                    Server.rankSP(user, serviceProvider, rank);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_ADD_SP:
                // Parse parameter
                serviceProvider = (ServiceProvider) objects[1];
                // Run server action
                try {
                    Server.addServiceProvider(serviceProvider);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_EDIT_SP:
                // Parse parameter
                serviceProvider = (ServiceProvider) objects[1];
                // Run server action
                try {
                    Server.editServiceProvider(serviceProvider);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_SEARCH_BY_STRING:
                // Parse parameter
                query = (String) objects[1];
                // Run server action
                try {
                    serviceProviderList = Server.searchByString(query);
                    // Configure result
                    result.setServiceProviderList(serviceProviderList);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
                }
                break;
            case Server.SERVER_ACTION_SEARCH_CATEGORY_BY_STRING:
                // Parse parameter
                category = (ServiceProviderCategory) objects[1];
                query = (String) objects[2];

                // Run server action
                try {
                    serviceProviderList = Server.searchCategoryByString(category, query);
                    // Configure result
                    result.setServiceProviderList(serviceProviderList);
                } catch (IOException e) {
                    // Configure result as error
                    result.catchException(e);
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
