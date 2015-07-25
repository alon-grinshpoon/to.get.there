package il.co.togetthere;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.User;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;
import il.co.togetthere.util.LikeListener;

public class ReviewsListActivity extends Activity implements AsyncResponse {

    ServiceProvider mSP;

    int colorID = 0;
    int likesDrawable = 0;
    int color = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);

        AsyncRequest asyncRequest = new AsyncRequest(ReviewsListActivity.this);
        Intent inIntent = getIntent();
        String sp_id = inIntent.getStringExtra("SP_ID");
        asyncRequest.execute(Server.SERVER_ACTION_GET_SP_BY_ID, sp_id);

    }

    @Override
    public void handleResult(AsyncResult result) {
        if (result.errored()){
            // Show message
            Toast.makeText(getApplicationContext(), "Oops! Unable to show reviews.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mSP = result.getServiceProvider();

            /* List View */
            findViewById(R.id.progress).setVisibility(View.GONE);
            ListView listView = (ListView) findViewById(R.id.list_reviews);
            ReviewsListAdapter reviewsListAdapter = new ReviewsListAdapter(ReviewsListActivity.this, mSP.getReviewsAsList());
            listView.setAdapter(reviewsListAdapter);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private class ReviewsListAdapter extends BaseAdapter {
        private List<Review> reviewsList;
        private LayoutInflater inflater;

        public ReviewsListAdapter(Context context, List<Review> reviewsList) {
            this.reviewsList = reviewsList;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return reviewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return reviewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return (long) i;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");
            Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/GOTHICB.TTF");

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.location_review_list_item, null);

                holder.userName = (TextView) convertView
                        .findViewById(R.id.text_review_list_item_title);
                holder.userImage = (ProfilePictureView) convertView
                        .findViewById(R.id.user_img_review_list_item);
                holder.content = (TextView) convertView
                        .findViewById(R.id.text_review_list_item_body);
                holder.likes = (TextView) convertView
                        .findViewById(R.id.text_review_list_item_likes);


                // Set Font
                holder.userName.setTypeface(fontBold);
                holder.content.setTypeface(font);
                holder.likes.setTypeface(font);

                //Set Colors
                colorID = getResources().getIdentifier(mSP.getCategory() + "_bg_color", "color",
                        "il.co.togetthere");
                color = getResources().getColor(colorID);
                likesDrawable = getResources().getIdentifier(
                        "ic_thumb_" + mSP.getCategory(), "drawable", getPackageName());

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Review review = reviewsList.get(position);


            holder.userName.setText(review.getUser().getFullName());
            holder.content.setText(review.getContent());
            holder.userImage.setProfileId(review.getUser().getFacebook_id());
            holder.likes.setText(review.getLikes() + "   ");
            holder.likes.setTextColor(color);
            holder.likes.setBackground(getResources().getDrawable(likesDrawable));
            holder.likes.setOnClickListener(new LikeListener(review, font));
            return convertView;
        }

        class ViewHolder {
            private TextView userName, content, likes;
            private ProfilePictureView userImage;
        }
    }

}