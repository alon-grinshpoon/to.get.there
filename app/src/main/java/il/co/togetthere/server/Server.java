package il.co.togetthere.server;

import com.google.gson.Gson;

import java.io.IOException;

import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderType;
import il.co.togetthere.db.User;

public class Server {

    private static final String server = "http://django-togetthereserver.rhcloud.com/ToGetThere/android/";

    // Get all SP's of certain type
    public static final void getSPsOfType(ServiceProviderType type) throws IOException {
        HTTPHandler.getRequest(server + "/category/" + ServiceProviderType.enumToString(type) + "/");
    }

    // Get SP by ID (show its details, reviews list, ranks list)
    public static final void getSPByID(int id) throws IOException {
        HTTPHandler.getRequest(server + "/sp/" + id + "/");
    }

    // Get all reviews of certain SP
    public static final void getSPReviewsByID(int id) throws IOException {
        HTTPHandler.getRequest(server + "/sp/" + id + "/reviews/");
    }

    // Get details on a certain user
    public static final User getUserByID(int id) throws IOException {
        // GET
        String json = HTTPHandler.getRequest(server + "/user/" + id + "/");
        // User GSON to convert JSON to User
        User user = new Gson().fromJson(json, User.class);
        // Return
        return user;
    }

    // Add a new SP
    public static final void addSP(ServiceProvider sp) throws IOException {
        /**
         * to add a new SP
         **POST** request with a JSON body
         the body should have the following json format (order doesn't matter):
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

         *** If SP with this name already exist it will not be added and you will get an empty Json response
         else you will be redirected to the response given by
         http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/1/
         with the newly created sp
         */
        HTTPHandler.postRequest(server + "/sp/add/", sp.toString());
    }

    // Rank an SP with score between 1-5, with user ID, and SP id:
    public static final void rankSP(int userID, int spID, int rank) throws IOException {
        /**
         * example:
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/ranksp/1/score/5/user/1
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/ranksp/<sp_id>/score/5<rank>/user/<user_id- not facebook id>
         */
        HTTPHandler.getRequest(server + "/ranksp/" + spID + "/score/" + rank + "/user/" + userID);
    }

    // Add a review to a certain SP from certain user
    public static final void addReviewToSP(int userID, int spID, String title, String content) throws IOException {
        /**
         * # ex: /ToGetThere/android/addreview/
         * **POST** request with a JSON body:
         * {"sp": "1",
         * "user": "2",
         * "title": "a new test review",
         * "content": "the test review content"}
         */
        String json = "";
        HTTPHandler.postRequest(server + "/addreview/", json);
    }

    // Add like to a certain review from certain user
    public static final void addLikeToReview(int userID, int spID, int reviewID) throws IOException {
        /**
         * if there is already like from that user nothing wil change
         * # ex: /ToGetThere/android/addLike/
         * **POST** request with a JSON body:
         * {"user": "1",
         * "review": "6"}
         * will return an http response of the sp which had the review as in
         * http://django-togetthereserver.rhcloud.com/ToGetThere/android/sp/<sp_id>/
         */
        String json = "";
        HTTPHandler.postRequest(server + "/addLike/", json);
    }

    public static final void registerUser(User user) throws IOException {
        /*
         * # register a new user
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
         * else returns http 400 response - User already exist
         */
        String json = "";
        HTTPHandler.postRequest(server + "/adduser/", json);
    }
}
