package il.co.togetthere.util;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import il.co.togetthere.LoginActivity;
import il.co.togetthere.UserInfoActivity;
import il.co.togetthere.db.Review;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

/**
 * Created by Rony on 25/07/2015.
 */
public class LikeListener implements View.OnClickListener, AsyncResponse {
    Review review;
    TextView v;
    Typeface font;

    public LikeListener(Review review, Typeface font) {
        this.review = review;
        this.font = font;
    }

    @Override
    public void onClick(View v) {
        this.v = (TextView) v;
        // Register Like
        AsyncRequest asyncRequest = new AsyncRequest(LikeListener.this);
        asyncRequest.execute(Server.SERVER_ACTION_ADD_LIKE_TO_REVIEW, LoginActivity.user, review);
    }

    @Override
    public void handleResult(AsyncResult result) {
        if (result.errored()) {
            String message;
            if (result.getStatusCode() == 400) {
                message = "Uh-oh! You already liked this review.";
            } else {
                message = "Oops! Unable to add your like.";
            }
            Toast.makeText(v.getContext().getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Inc Likes
            int numLikes = 1 + Integer.parseInt(((String) v.getText()).trim());
            v.setText(numLikes + "   ");
            v.setTypeface(font);
        }
    }
}


