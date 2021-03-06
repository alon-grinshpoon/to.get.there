package il.co.togetthere.server;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import il.co.togetthere.LoginActivity;
import il.co.togetthere.db.Images;
import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.User;

/**
 * Class implementation of all valid server calls.
 */
public class Server {

    /**
     * Server URL
     */
    private static final String server = "http://django-togetthereserver.rhcloud.com/ToGetThere/android/";

    public static final int SERVER_ACTION_GET_SPS_OF_CATEGORY = 0;
    /**
     * Get all service providers of certain type and sort by rank (my use vicinity).
     * @param category A service providers category
     * @return A list of matching service providers
     * @throws IOException
     */
    protected static final List<ServiceProvider> getSPsOfCategory(ServiceProviderCategory category) throws IOException {
        if (LoginActivity.user.hasLatLng()){
            return searchByCategoryAndVicinity(category, LoginActivity.user.getLatitude(), LoginActivity.user.getLongitude());
        } else {
            /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/category/restaurants/ */
            String json = HTTPHandler.getRequest(server + "/category/" + ServiceProviderCategory.enumToString(category) + "/");
            ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
            return Arrays.asList(sps);
        }
    }

    public static final int SERVER_ACTION_GET_SP_BY_ID = 1;
    /**
     * Get a service provider by ID (show its details, reviews list, ranks list).
     * @param id An ID in string format
     * @return A matching service provider
     * @throws IOException
     */
    protected static final ServiceProvider getSPByID(String id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/ */
        String json = HTTPHandler.getRequest(server + "/sp/" + id + "/");
        ServiceProvider sp = new Gson().fromJson(json, ServiceProvider.class);
        return sp;
    }

    public static final int SERVER_ACTION_GET_SP_REVIEWS_BY_ID = 2;
    /**
     * Get all reviews of certain service provider.
     * @param id An ID in string format
     * @return A list of reviews to the matching service provider
     * @throws IOException
     */
    protected static final List<Review> getSPReviewsByID(int id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/reviews/ */
        String json = HTTPHandler.getRequest(server + "/sp/" + id + "/reviews/");
        Review[] reviews = new Gson().fromJson(json, Review[].class);
        return Arrays.asList(reviews);
    }

    public static final int SERVER_ACTION_GET_USER_BY_ID = 3;
    /**
     * Get a certain user by it's ID.
     * @param id An ID in string format
     * @return A matching User object
     * @throws IOException
     */
    protected static final User getUserByID(int id) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/user/1/ */
        String json = HTTPHandler.getRequest(server + "/user/" + id + "/");     // GET
        User user = new Gson().fromJson(json, User.class);                      // User GSON to convert JSON to User
        return user;                                                            // Return
    }

    public static final int SERVER_ACTION_ADD_SP = 4;
    /**
     * Add a new service provider.
     * @param sp A service provider object to add to the system
     * @return True if service provider was added successfully
     * @throws IOException
     */
    protected static final boolean addServiceProvider(ServiceProvider sp) throws IOException {
        /*
        * POST request with a JSON body
        * The body should have the following json format (order doesn't matter):
        * {"sp_name": "test",
        * "address": "test_address",
        * "longitude": "3.2",
        * "latitude": "2.2",
        * "phone": "03-7511034",
        * "discount": "5",
        * "category": "medical",
        * "website": "website",
        * "toilets": "true",
        * "elevator": "true",
        * "entrance": "false",
        * "facilities": "false",
        * "parking": "true",
        * "toilets_text": "toilets_text",
        * "elevator_text": "elevator_text",
        * "entrance_text": "entrance_text",
        * "facilities_text": "facilities_text",
        * "parking_text": "parking_text"}
        *
        * to
        *
        * http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/add/
        *
        * If SP with this name already exist it will not be added and you will get an empty Json response
        * else you will be redirected to the response given by
        * http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/
        * with the newly created sp
        */
        boolean success = true;
        String json = new Gson().toJson(sp, ServiceProvider.class);
        String jsonResponse = HTTPHandler.postRequest(server + "/sp/add/", json);
        if (checkJSONError(jsonResponse)){
            success = false;
        }
        return success;
    }

    public static final int SERVER_ACTION_RANK_SP = 5;
    /**
     * Rank an service provider with a score based on a user ID service provider ID.
     * @param user A ranking user
     * @param sp A service provider object to be ranked
     * @param rank A score between 1-5
     * @return True if the user ranked the service provider successfully
     * @throws IOException
     */
    protected static final boolean rankSP(User user, ServiceProvider sp, int rank) throws IOException {
        /*
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

    public static final int SERVER_ACTION_ADD_REVIEW_TO_SP = 6;
    /**
     * Add a review to a certain service provider.
     * @param sp A service provider object to be reviewed
     * @param review A review object
     * @return True if the review was added to the service provider successfully
     * @throws IOException
     */
    protected static final ServiceProvider addReviewToSP(ServiceProvider sp, Review review) throws IOException {
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
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() { // Create JSON from User but exclude user object and likes
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return (f.getDeclaringClass() == Review.class) &&
                        (f.getName().equals("user") || f.getName().equals("likes"));
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = gson.toJson(review, Review.class);
        // Add sp and user to the json
        json = "{\"sp\": \"" + sp.getId() + "\", \"user\": " + LoginActivity.user.getID() + ", " + json.substring(1);
        // Post request
        String jsonResponse = HTTPHandler.postRequest(server + "/addreview/", json);
        ServiceProvider updateServiceProvider = new Gson().fromJson(jsonResponse, ServiceProvider.class);
        if (checkJSONError(jsonResponse)){
            success = false;
        }
        return updateServiceProvider;
    }

    public static final int SERVER_ACTION_REGISTER_USER = 7;
    /**
     * Register a new user.
     * @param user A user object
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_ADD_LIKE_TO_REVIEW = 8;
    /**
     * Add like to a certain review from certain user.
     * @param user The user preforming the like
     * @param review The review to like
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_REMOVE_LIKE_FROM_REVIEW = 9;
    /**
     * Remove like of a certain review from certain user.
     * @param user The user preforming the unlike
     * @param review The review to unlike
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_REMOVE_REVIEW = 10;
    /**
     * Remove a review.
     * @param review The review to remove
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_EDIT_USER_BY_ID = 11;
    /**
     * Edit the details of a certain user.
     * @param user The user with the edited details
     * @param userID The user ID of the user to be edited
     * @return True if the user was successfully edited
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_EDIT_SP = 12;
    /**
     * Edit the details of a certain service provider.
     * @param sp The service provider with the edited details
     * @return True if the service provider was successfully edited
     * @throws IOException
     */
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
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return (f.getDeclaringClass() == ServiceProvider.class) &&
                        (f.getName().equals("avg_rank")
                                || f.getName().equals("reviews")
                                || f.getName().equals("id"));
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = gson.toJson(sp);
        String jsonResponse = HTTPHandler.postRequest(server + "/sp/" + sp.getId() + "/edit/", json);
        ServiceProvider newSP = new Gson().fromJson(jsonResponse, ServiceProvider.class);
        return newSP;
    }

    public static final int SERVER_ACTION_EDIT_REVIEW_BY_ID = 13;
    /**
     * Edit a certain review.
     * @param review A review with the edited details
     * @param reviewID The ID of the review to edit
     * @return True if the review was edited successfully
     * @throws IOException
     */
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

    public static final int SERVER_ACTION_SEARCH_BY_STRING = 14;
    /**
     * Search by string and sort by rank (may use vicinity).
     * @param text A string to search for
     * @return A list of matching service providers
     * @throws IOException
     */
    protected static final List<ServiceProvider> searchByString(String text) throws IOException {
        if (LoginActivity.user.hasLatLng()){
            return searchByStringAndVicinity(text, LoginActivity.user.getLatitude(), LoginActivity.user.getLongitude());
        } else {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/search?q=serach-text */
            String json = HTTPHandler.getRequest(server + "search?q=" + text);
            ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
            return Arrays.asList(sps);
        }
    }

    public static final int SERVER_ACTION_SEARCH_BY_CATEGORY_AND_VICINITY = 15;
    /**
     * Search by category and vicinity of 5 KM n/s e/w and sort by vicinity and rank
     * @param category A service provider category
     * @param lat A latitude coordination
     * @param lng A longitude coordination
     * @return A list of matching service providers
     * @throws IOException
     */
    protected static final List<ServiceProvider> searchByCategoryAndVicinity(ServiceProviderCategory category, double lat, double lng) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/lng/34.3/lat/23.3/category/medical/ */
        String json = HTTPHandler.getRequest(server + "/lng/" + lng + "/lat/" + lat + "/category/" + ServiceProviderCategory.enumToString(category) + "/");
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    public static final int SERVER_ACTION_SEARCH_CATEGORY_BY_STRING = 16;
    /**
     * Search by string within category (may use vicinity).
     * @param category A service provider category
     * @param text A string to search for
     * @return A list of matching service providers
     * @throws IOException
     */
    protected static final List<ServiceProvider> searchCategoryByString(ServiceProviderCategory category, String text) throws IOException {
        if (LoginActivity.user.hasLatLng()){
            return searchCategoryByStringAndVicinity(category, text, LoginActivity.user.getLatitude(), LoginActivity.user.getLongitude());
        } else {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/[medical|restaurants|shopping|public_services|transportat|help]/search?q=serach-text */
            String json = HTTPHandler.getRequest(server + ServiceProviderCategory.enumToString(category) + "/search?q=" + text);
            ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
            return Arrays.asList(sps);
        }
    }

    public static final int SERVER_ACTION_SEARCH_BY_STRING_AND_VICINITY = 17;
    /**
     * Search by string and vicinity of 5 KM n/s e/w and sort by vicinity and rank.
     * @param text A string to search for
     * @param lat A latitude coordination
     * @param lng A longitude coordination
     * @return
     * @throws IOException
     */
    protected static final List<ServiceProvider> searchByStringAndVicinity(String text, double lat, double lng) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/lng/34.3/lat/23.3/search?q=search-text */
        String json = HTTPHandler.getRequest(server + "/lng/" + lng + "/lat/" + lat + "/search?q=" + text);
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    public static final int SERVER_ACTION_SEARCH_CATEGORY_BY_STRING_AND_VICINITY = 18;
    /**
     * Search by string, category and vicinity of 5 KM n/s e/w and sort by vicinity and rank.
     * @param category A service provider category
     * @param text A string to search for
     * @param lat A latitude coordination
     * @param lng A longitude coordination
     * @return A list of matching service providers
     * @throws IOException
     */
    protected static final List<ServiceProvider> searchCategoryByStringAndVicinity(ServiceProviderCategory category, String text, double lat, double lng) throws IOException {
        /* example: http://django-togetthereserver.rhcloud.com/ToGetThere/android/lng/34.3/lat/23.3/category/medical/ */
        String json = HTTPHandler.getRequest(server + "/lng/" + lng + "/lat/" + lat + "/category/" + ServiceProviderCategory.enumToString(category) + "/search?q=" + text);
        ServiceProvider[] sps = new Gson().fromJson(json, ServiceProvider[].class);
        return Arrays.asList(sps);
    }

    public static final int SERVER_ACTION_SEARCH_IMAGES_BY_STRING = 19;
    /**
     * Search for images using AJAX Google Images API
     * @param search A string to search for
     * @return A list of string URLs of web-stored images
     * @throws IOException
     */
    protected static final List<String> searchImagesByString(String search) throws IOException {
        String google = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
        String json = HTTPHandler.getRequest(google + search);
        Images images = new Gson().fromJson(json, Images.class);
        return images.getURLs();
    }

    /**
     * Check JSON validity
     * @param json A JSON object as a string
     * @return The if the JSON is valid
     */
    private static final boolean checkJSONError(String json){
        return (json == null || json.equalsIgnoreCase("{}"));
    }

}