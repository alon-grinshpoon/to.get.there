package il.co.togetthere.server;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import il.co.togetthere.LoginActivity;
import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.User;

public class Server {

    private static final String server = "http://django-togetthereserver.rhcloud.com/ToGetThere/android/";

    // Get all SP's of certain type
    public static final int SERVER_ACTION_GET_SPS_OF_CATEGORY = 0;
    protected static final List<ServiceProvider> getSPsOfCategory(ServiceProviderCategory category) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/category/restaurants/ */
        String json = HTTPHandler.getRequest(server + "/category/" + ServiceProviderCategory.enumToString(category) + "/");
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    // Get SP by ID (show its details, reviews list, ranks list)
    public static final int SERVER_ACTION_GET_SP_BY_ID = 1;
    protected static final ServiceProvider getSPByID(int id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/ */
        String json = HTTPHandler.getRequest(server + "/sp/" + id + "/");
        ServiceProvider sp = new Gson().fromJson(json, ServiceProvider.class);
        return sp;
    }

    // Get all reviews of certain SP
    public static final int SERVER_ACTION_GET_SP_REVIEWS_BY_ID = 2;
    protected static final List<Review> getSPReviewsByID(int id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/reviews/ */
        String json = HTTPHandler.getRequest(server + "/sp/" + id + "/reviews/");
        Review[] reviews = new Gson().fromJson(json, Review[].class);
        return Arrays.asList(reviews);
    }

    // Get details on a certain user
    public static final int SERVER_ACTION_GET_USER_BY_ID = 3;
    protected static final User getUserByID(int id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/user/1/ */
        String json = HTTPHandler.getRequest(server + "/user/" + id + "/");     // GET
        User user = new Gson().fromJson(json, User.class);                      // User GSON to convert JSON to User
        return user;                                                            // Return
    }

    // Add a new SP
    public static final int SERVER_ACTION_ADD_SP = 4;
    protected static final boolean addServiceProvider(ServiceProvider sp) throws IOException {
        /**
         POST request with a JSON body
         The body should have the following json format (order doesn't matter):
         {"sp_name": "test",
         "address": "test_address",
         "longitude": "3.2",
         "latitude": "2.2",
         "phone": "03-7511034",
         "discount": "5",
         "category": "medical",
         "website": "website",
         "toilets": "true",
         "elevator": "true",
         "entrance": "false",
         "facilities": "false",
         "parking": "true",
         "toilets_text": "toilets_text",
         "elevator_text": "elevator_text",
         "entrance_text": "entrance_text",
         "facilities_text": "facilities_text",
         "parking_text": "parking_text"}

         to

         http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/add/

         If SP with this name already exist it will not be added and you will get an empty Json response
         else you will be redirected to the response given by
         http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/
         with the newly created sp
         */
        boolean success = true;
        String json = new Gson().toJson(sp, ServiceProvider.class);
        String jsonResponse = HTTPHandler.postRequest(server + "/sp/add/", json);
        if (checkJSONError(jsonResponse)){
            success = false;
        }
        return success;
    }

    // Rank an SP with score between 1-5, with user ID, and SP id:
    public static final int SERVER_ACTION_RANK_SP = 5;
    protected static final boolean rankSP(User user, ServiceProvider sp, int rank) throws IOException {
        /**
         * example:
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/ranksp/1/score/5/user/1
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/ranksp/<sp_id>/score/5<rank>/user/<user_id- not facebook id>
         */
        boolean success = true;
        String jsonResponse = HTTPHandler.getRequest(server + "/ranksp/" + sp.getId() + "/score/" + rank + "/user/" + user.getID());
        if (checkJSONError(jsonResponse)){
            success = false;
        }
        return success;
    }

    // Add a review to a certain SP from certain user
    public static final int SERVER_ACTION_ADD_REVIEW_TO_SP = 6;
    protected static final boolean addReviewToSP(ServiceProvider sp, Review review) throws IOException {
        /**
         * example: /ToGetThere/android/addreview/
         * **POST** request with a JSON body:
         * {"sp": "1",
         * "user": "2",
         * "title": "a new test review",
         * "content": "the test review content"}
         */
        boolean success = true;
        // Create needed JSON
        String json = new Gson().toJson(review, Review.class);
        json = "{\"sp\": \"" + sp.getId() + "\", " + json.substring(1);
        // Post request
        String jsonResponse = HTTPHandler.postRequest(server + "/addreview/", json);
        if (checkJSONError(jsonResponse)){
            success = false;
        }
        return success;
    }

    // Register a new user
    public static final int SERVER_ACTION_REGISTER_USER = 7;
    protected static final void registerUser(User user) throws IOException {
        /*
         * # ex: http://django-togetthereserver.rhcloud.com/ToGetThere/android/adduser/
         * **POST** request with a JSON body:
         * {"facebook_id": "4331",
         * "first_name": "fn",
         * "last_name": "ln",
         * "email": "2323@gmail.com",
         * "birthday": "YYYY-MM-DD",
         * "phone": "fn",
         * "isVolunteering": "true/false/True/False"}
         * if user exist returns http response like in:
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/user/<newUSerId>/
         * else registers and returns response as above.
         */
        String jsonUser = new Gson().toJson(user);
        String json = HTTPHandler.postRequest(server + "/adduser/", jsonUser);
        LoginActivity.user.join(new Gson().fromJson(json, User.class));
    }

    // Add like to a certain review from certain user
    public static final int SERVER_ACTION_ADD_LIKE_TO_REVIEW = 8;
    protected static final void addLikeToReview(User user, Review review) throws IOException {
        /**
         * if there is already like from that user nothing wil change
         * # ex: /ToGetThere/android/addLike/
         * POST request with a JSON body:
         * {"user": "1",
         * "review": "6"}
         * will return an http response of the sp which had the review as in
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/<sp_id>/
         */
        boolean success = true;
        // Create needed JSON
        String json = "{\"user\": \"" + user.getID() + "\", \"review\": \"" + review.getId() + "\"}";
        // Post request
        String jsonResponse = HTTPHandler.postRequest(server + "/addLike/", json);
        if (checkJSONError(jsonResponse)){
            success = false;
        }
    }

    // Remove like of a certain review from certain user
    public static final int SERVER_ACTION_REMOVE_LIKE_FROM_REVIEW = 9;
    protected static final void removeLikeToReview(User user, Review review) throws IOException {
        /**
         * example: /ToGetThere/android/removeLike/
         * POST request with a JSON body:
         * {"user": "1",
         * "review": "6"}
         * will return an http 200 response if the like was deleted successfully
         * if no like was found will return http 404
         */
        // Create needed JSON
        String json = "{\"user\": \"" + user.getID() + "\", \"review\": \"" + review.getId() + "\"}";
        // Post request
        String jsonResponse = HTTPHandler.postRequest(server + "/removeLike/", json);
    }

    // Remove review
    public static final int SERVER_ACTION_REMOVE_REVIEW = 10;
    protected static final void removeReview(Review review) throws IOException {
        /*
        * exAMPLE /ToGetThere/android/removeReview/
        * POST request with a JSON body:
        * {"review_id": "6"}
        * will return an http 200 response if the review was deleted successfully
        * if no review was found will return http 404
        */
        // Create needed JSON
        String json = "{\"review\": \"" + review.getId() + "\"}";
        // Post request
        String jsonResponse = HTTPHandler.postRequest(server + "/removeReview/", json);
    }

    // Edit the details of a certain user
    public static final int SERVER_ACTION_EDIT_USER_BY_ID = 11;
    protected static final User editUserByID(User user, int userID) throws IOException {
        /* POST request with a JSON body, the body is key-value of the fields to change:
        * {"name" : "new name"}
        * possible fields are:
        * facebook_id
        * first_name
        * last_name
        * full_name
        * email
        * birthday
        * level
        * phone
        * points
        * isVolunteering
        * wasAskedToVolunteer
        * Example: /ToGetThere/android/editprofile/1/
        * will return the new user profile in Json */
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() { // Create JSON from User but exclude id
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return (f.getDeclaringClass() == User.class && f.getName().equals("id"));
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = gson.toJson(user);
        String jsonResponse = HTTPHandler.postRequest(server + "/editprofile/" + userID + "/", json);
        User newUser = new Gson().fromJson(jsonResponse, User.class);
        return newUser;
    }

    // Edit the details of a certain SP
    public static final int SERVER_ACTION_EDIT_SP = 12;
    protected static final ServiceProvider editServiceProvider(ServiceProvider sp) throws IOException {
        /* POST request with a JSON body, the body is key-value of the fields to change:
        * {"name" : "new name"}
        * possible fields are:
        * sp_name
        * address
        * category
        * longitude
        * latitude
        * phone
        * is_verified
        * discount
        * website
        * toilets
        * elevator
        * entrance
        * facilities
        * toilets_text
        * parking_text
        * elevator_text
        * entrance_text
        * facilities_text
        * Example: /ToGetThere/android/sp/1/edit/
        * will return the new sp in Json */
        String json = new Gson().toJson(sp);
        String jsonResponse = HTTPHandler.postRequest(server + "/sp/" + sp.getId() + "/edit/", json);
        ServiceProvider newSP = new Gson().fromJson(jsonResponse, ServiceProvider.class);
        return newSP;
    }

    // Edit a certain review
    public static final int SERVER_ACTION_EDIT_REVIEW_BY_ID = 13;
    protected static final ServiceProvider ediReviewByID(Review review, int reviewID) throws IOException {
        /* POST request with a JSON body, the body is key-value of the fields to change:
        * {"title" : "new title"}
        * possible fields are:
        * title
        * content
        * created
        * user
        * Example: /ToGetThere/android/editReview/2
        * will return an http response of the sp which had the review */
        String json = new Gson().toJson(review);
        String jsonResponse = HTTPHandler.postRequest(server + "/editReview/" + reviewID + "/", json);
        ServiceProvider sp = new Gson().fromJson(jsonResponse, ServiceProvider.class);
        return sp;
    }

    // Search by string
    public static final int SERVER_ACTION_SEARCH_BY_STRING = 14;
    protected static final List<ServiceProvider> searchByString(String text) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/search?q=serach-text */
        String json = HTTPHandler.getRequest(server + "search?q=" + text);
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    // Search by category and vicinity of 5 KM n/s e/w
    public static final int SERVER_ACTION_SEARCH_BY_CATEGORY_AND_VICINITY = 15;
    protected static final List<ServiceProvider> searchByCategoryAndVicinity(ServiceProviderCategory category, double lat, double lng) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/lng/34.3/lat/23.3/category/medical/ */
        String json = HTTPHandler.getRequest(server + "/lng/" + lng + "/lat/" + lat + "/category/" + ServiceProviderCategory.enumToString(category) + "/");
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    // Check JSON validity
    private static final boolean checkJSONError(String json){
        return (json == null || json.equalsIgnoreCase("{}"));
    }
}