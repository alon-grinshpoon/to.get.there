package il.co.togetthere;

import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.Task;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A fragment representing a single page.
 */
public class ScreenSlidePageFragment extends Fragment implements
		OnMarkerClickListener {
	/* The argument key for the page number this fragment represents. */
	public static final String ARG_PAGE = "page";
	public static final String ARG_TYPE = "Type";

	/*
	 * The fragment's page number, which is set to the argument value for {@link
	 * #ARG_PAGE}.
	 */
	public ViewGroup rootView;
	public String mServiceProviderCategory = null;
	private int mPageNumber;
	private ServiceProvider mSP;
	private Task mTask;
	private List<Review> mReviewsList;

	MapView mMapView;
	private GoogleMap googleMap;

	/*
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given SP.
	 */
	public static ScreenSlidePageFragment create(int pageNumber, String Type) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		args.putString(ARG_TYPE, Type);
		fragment.setArguments(args);
		return fragment;
	}

	public ScreenSlidePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		mServiceProviderCategory = getArguments().getString(ARG_TYPE);

		/**
		 * Get information from DB
		 **/
		if (mServiceProviderCategory.equals("help")) {
			mTask = getCurrTask(mPageNumber);
		} else {
			mSP = getCurrSP(mPageNumber);
		}
	}

	private ServiceProvider getCurrSP(int PageNumber) {
		return ScreenSlideActivity.getServiceProvider(PageNumber);
	}

	private Task getCurrTask(int PageNumber) {
		return ScreenSlideActivity.getTask(PageNumber);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout containing a title and body text.
		if (mServiceProviderCategory.equals("help")) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_help, container, false);
			setHelpView(rootView, mServiceProviderCategory);

		} else {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_sp, container, false);
			setSPView(rootView, mSP.getCategory().toString());
		}

		// Map
		if (!mServiceProviderCategory.equals("help")) {
			mMapView = (MapView) rootView.findViewById(R.id.mapView);
			mMapView.onCreate(savedInstanceState);
			mMapView.onResume();// needed to get the map to display immediately

			try {
				MapsInitializer.initialize(getActivity()
						.getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}

			googleMap = mMapView.getMap();

			LatLng positionSP = new LatLng(mSP.getLatitude(),
					mSP.getLongitude());
			googleMap.setOnMarkerClickListener(this);
			googleMap.setMyLocationEnabled(true);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionSP,
					12));
			googleMap.addMarker(new MarkerOptions().position(positionSP).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.ic_g_marker)));

			LatLng currentUserPosition = new LatLng(
					LoginActivity.user.getLatitude(),
					LoginActivity.user.getLongitude());
			googleMap.addMarker(new MarkerOptions()
					.position(currentUserPosition));

		} else {
			// TODO help map
		}
		
		// Titles
		TextView titlePhotos = ((TextView) rootView.findViewById(R.id.title_photos));
		TextView titleReview = ((TextView) rootView.findViewById(R.id.title_reviews));
		TextView titleBusinessButton1 = ((TextView) rootView.findViewById(R.id.title_business_button_1));
		TextView titleBusinessButton2 = ((TextView) rootView.findViewById(R.id.title_business_button_2));
				
		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHICB.TTF");
		titlePhotos.setTypeface(fontBold);
		titleReview.setTypeface(fontBold);
		titleBusinessButton1.setTypeface(fontBold);
		titleBusinessButton2.setTypeface(font);

		// Define Verified Button
		((LinearLayout) rootView.findViewById(R.id.button_get_verified)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String uriText =
						"mailto:" + getString(R.string.app_email) +
								"?subject=" + Uri.encode("to.get.there - I want to get verified!") +
								"&body=" + Uri.encode("Please supply details here.");
				Uri uri = Uri.parse(uriText);
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(uri);
				startActivity(emailIntent);
			}
		});

		return rootView;

	}

	private void setHelpView(ViewGroup v, String Type) {

		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
				
		// Set Task Title
		TextView taskName = ((TextView) v.findViewById(R.id.text_task_name));
		taskName.setText(mTask.getTitle());
		taskName.setTypeface(font);

		// Set Task Description
		TextView taskDescription = ((TextView) v.findViewById(R.id.text_task_description));
		taskDescription.setText(mTask.getBody());
		taskDescription.setTypeface(font);
		
		

		Button volunteerButton = (Button) v.findViewById(R.id.button_volunteer);
		volunteerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTask.addVolunteer(LoginActivity.user);
				mTask.addTaskToDB();
				Toast.makeText(getActivity().getApplicationContext(),
						"Great! You're Amazing!", Toast.LENGTH_SHORT).show();
			}
		});

		// TODO Set Verified

		// if (!mTask.isIs_verified()) {
		// ((ImageView) v.findViewById(R.id.img_is_verified))
		// .setVisibility(View.INVISIBLE);

		// TODO set mLocationView

		// TODO set time and date

	}

	private void setSPView(View v, String Type) {

		int colorID = getResources().getIdentifier(Type + "_bg_color", "color",
				"il.co.togetthere");
		int color = getResources().getColor(colorID);

		SetViewColors(v, color);
		
		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		
		// Set Title
		TextView locationName = ((TextView) v.findViewById(R.id.text_location_name));
		locationName.setText(mSP.getSp_name());
		locationName.setTypeface(font);

		// Set Description
		TextView locationDescription = ((TextView) v.findViewById(R.id.text_location_description));
		locationDescription.setVisibility(View.GONE);
		locationDescription.setTypeface(font);

		// Set Address
		TextView locationAddress = ((TextView) v.findViewById(R.id.text_location_address));
		locationAddress.setText(mSP.getAddress());
		locationAddress.setTypeface(font);

		// Set Verified
		if (!mSP.is_verified()) {
			((ImageView) v.findViewById(R.id.img_is_verified))
					.setVisibility(View.GONE);
		}
		
		// Set Phone
		TextView locationPhone = ((TextView) v.findViewById(R.id.text_location_phone));
		locationPhone.setText(mSP.getPhone());
		locationPhone.setTypeface(font);

		LinearLayout locationPhoneButton = ((LinearLayout) v.findViewById(R.id.button_phone));
		locationPhoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!mSP.getPhone().equals("")) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + mSP.getPhone()));
					startActivity(callIntent);
				}
			}
		});

		// Set Discount
		TextView locationDiscount = ((TextView) v.findViewById(R.id.text_location_discount));
		locationDiscount.setText("%" + mSP.getDiscount());
		locationDiscount.setTypeface(font);

		// Set Website
		TextView locationWebsite = ((TextView) v.findViewById(R.id.text_location_website));
		locationWebsite.setText(mSP.getWebsite() != null ? mSP.getWebsite() : "N/A");
		locationWebsite.setTypeface(font);

		// set accessibility percentage
		setAccecibility(v);

		// set Stars rank
		setStarsView(v, Math.round(mSP.getAverageRank()));

		// Set Waze button
		LinearLayout wazeButton = (LinearLayout) v
				.findViewById(R.id.button_waze);
		wazeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onWazeClick();
			}
		});

		// TODO set List of reviews
		// ListView listView = (ListView)
		// rootView.findViewById(R.id.list_reviews);
		// listView.setAdapter(new mListAdapter(getActivity(), getReviews()));
		// listView.setOnTouchListener(new View.OnTouchListener() {

		// Reviews		
		getReviews();
		setReviewsView(v, color);
		TextView readMore = (TextView) v
				.findViewById(R.id.button_read_more_reviews);
		readMore.setTypeface(font);
		if (mReviewsList.size() > 3) {
			readMore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Open an activity with list view of reviews;
				}
			});
		} else {
			readMore.setVisibility(View.GONE);
		}
	}

	private void setAccecibility(View v) {
        ImageView accessibleView;
        ImageView accessibilityImage;
        TextView tv = (TextView) rootView.findViewById(R.id.text_location_accecability);
        tv.setVisibility(View.GONE);

        if (mSP.isElevator()) {
            accessibleView = (ImageView) v.findViewById(R.id.image_check_elevator);
            accessibleView.setVisibility(View.VISIBLE);}
		if (mSP.isEntrance()) {
            accessibleView = (ImageView) v.findViewById(R.id.image_check_entrance);
            accessibleView.setVisibility(View.VISIBLE);
		}
		if (mSP.isFacilities()) {
            accessibleView = (ImageView) v.findViewById(R.id.image_check_furniture);
            accessibleView.setVisibility(View.VISIBLE);
		}
		if (mSP.isParking()) {
            accessibleView = (ImageView) v.findViewById(R.id.image_check_parking);
            accessibleView.setVisibility(View.VISIBLE);
		}
		if (mSP.isToilets()) {
            accessibleView = (ImageView) v.findViewById(R.id.image_check_toilet);
            accessibleView.setVisibility(View.VISIBLE);
		}
        // Set onClickListeneres for text
        accessibilityImage = (ImageView) v.findViewById(R.id.image_elevator);
        accessibilityImage.setOnClickListener(new AccessibilityListener(mSP.getElevator_text()));
        accessibilityImage = (ImageView) v.findViewById(R.id.image_toilets);
        accessibilityImage.setOnClickListener(new AccessibilityListener(mSP.getToilets_text()));
        accessibilityImage = (ImageView) v.findViewById(R.id.image_parking);
        accessibilityImage.setOnClickListener(new AccessibilityListener(mSP.getParking_text()));
        accessibilityImage = (ImageView) v.findViewById(R.id.image_facilities);
        accessibilityImage.setOnClickListener(new AccessibilityListener(mSP.getFacilities_text()));
        accessibilityImage = (ImageView) v.findViewById(R.id.image_entrance);
        accessibilityImage.setOnClickListener(new AccessibilityListener(mSP.getEntrance_text()));
	}

    class AccessibilityListener implements OnClickListener{
        String accecibilityText;

        public AccessibilityListener(String accecibilityText) {
            Log.i("accecibility", "Adding listener to accessibility test: " + this.accecibilityText);
            this.accecibilityText = accecibilityText;
        }

        @Override
        public void onClick(View view) {
            //Get Accessibility text view
            TextView tv = (TextView) rootView.findViewById(R.id.text_location_accecability);;

            // If no txt, hide box
            if (accecibilityText== null || accecibilityText.equals("")) {
                tv.setVisibility(View.GONE);
            } else {
                Log.i("accessability", "Showing: " + accecibilityText);
                tv.setText(accecibilityText);
                tv.setVisibility(View.VISIBLE);
            }
        }
    }


    private void setStarsView(View v, int starsNum) {
		switch (starsNum) {
		case 5:
			((ImageView) v.findViewById(R.id.image_rank_5))
					.setVisibility(View.VISIBLE);
		case 4:
			((ImageView) v.findViewById(R.id.image_rank_4))
					.setVisibility(View.VISIBLE);
		case 3:
			((ImageView) v.findViewById(R.id.image_rank_3))
					.setVisibility(View.VISIBLE);
		case 2:
			((ImageView) v.findViewById(R.id.image_rank_2))
					.setVisibility(View.VISIBLE);
		case 1:
			((ImageView) v.findViewById(R.id.image_rank_1))
					.setVisibility(View.VISIBLE);
		}

	}
	
	/**
	 * Sets the appearance if the reviews sections according to number of reviews that exist
	 * */
	private void setReviewsView(View v, int color) {
		
		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		
		TextView noReviews = (TextView) v
				.findViewById(R.id.text_be_first_reviewer);
		noReviews.setTypeface(font);

		// Set reviews
		RelativeLayout review1 = (RelativeLayout) v.findViewById(R.id.review0);
		RelativeLayout review2 = (RelativeLayout) v.findViewById(R.id.review1);
		RelativeLayout review3 = (RelativeLayout) v.findViewById(R.id.review2);

		switch (mReviewsList.size()) {
		case 0: // no reviews
			noReviews.setTextColor(color);
			review1.setVisibility(View.GONE);
			review2.setVisibility(View.GONE);
			review3.setVisibility(View.GONE);
			break;
		case 1: // one review
			noReviews.setVisibility(View.GONE);
			setReview(v, color, 0);
			review2.setVisibility(View.GONE);
			review3.setVisibility(View.GONE);
			break;
		case 2: // two reviews
			noReviews.setVisibility(View.GONE);
			setReview(v, color, 0);
			setReview(v, color, 1);
			review3.setVisibility(View.GONE);
			break;
		default: // 3 or more
			noReviews.setVisibility(View.GONE);
			setReview(v, color, 0);
			setReview(v, color, 1);
			setReview(v, color, 2);
			break;
		}

		// Set the user review
		setUserReview(v, color);
	}

	private void setReview(View v, int color, int num) {
		int reviewerTextID = getResources().getIdentifier(
				"review_item_title" + num, "id",
				v.getContext().getPackageName());
		int reviewTextID = getResources()
				.getIdentifier("review_item_body" + num, "id",
						v.getContext().getPackageName());
		int likesID = getResources().getIdentifier("text_review_likes_" + num,
				"id", v.getContext().getPackageName());
		int userImageID = getResources().getIdentifier("img_review_user" + num,
				"id", v.getContext().getPackageName());

		Log.i("review", "ic_thumb_" + ServiceProviderCategory.enumToString(mSP.getCategory()));
		int likesDrawable = getResources().getIdentifier(
				"ic_thumb_" + ServiceProviderCategory.enumToString(mSP.getCategory()), "drawable",
				v.getContext().getPackageName());

		// get the reviews view's
		TextView reviewer = (TextView) v.findViewById(reviewerTextID);
		final TextView review = (TextView) v.findViewById(reviewTextID);
		TextView likes = (TextView) v.findViewById(likesID);
		ProfilePictureView profilePictureView = (ProfilePictureView) v
				.findViewById(userImageID);
		
		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		// set font
		reviewer.setTypeface(font);
		review.setTypeface(font);
		likes.setTypeface(font);
		
		// get the review object
		Review reviewObj = mReviewsList.get(num);

		// set review details
		reviewer.setText(reviewObj.getUser().getFullName());
		review.setText(reviewObj.getContent());
        profilePictureView.setProfileId(reviewObj.getUser().getFacebook_id());
		likes.setText(reviewObj.getLikes() + "   ");
		likes.setTextColor(color);
		likes.setBackground(getResources().getDrawable(likesDrawable));
		likes.setOnClickListener(new LikeListener(num));
		
		// TODO id of user that wrote review
		// profilePictureView.setProfileId(mReviewsList.get(position).g);
	}

	private void setUserReview(View v, int color) {
		int reviewerTextID = getResources().getIdentifier(
				"review_item_title_yours", "id",
				v.getContext().getPackageName());
		int reviewTextID = getResources()
				.getIdentifier("review_item_body_yours", "id",
						v.getContext().getPackageName());
		int userImageID = getResources().getIdentifier("img_review_yours",
				"id", v.getContext().getPackageName());
		int buttonID = getResources().getIdentifier("post_your_review_button",
				"id", v.getContext().getPackageName());

		// get the reviews view's
		TextView reviewer = (TextView) v.findViewById(reviewerTextID);
		final EditText review = (EditText) v.findViewById(reviewTextID);
		ProfilePictureView profilePictureView = (ProfilePictureView) v
				.findViewById(userImageID);
		Button submitButton = (Button) v.findViewById(buttonID);

		// Define Font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		// set font
		reviewer.setTypeface(font);
		review.setTypeface(font);
		submitButton.setTypeface(font);

		// set review details
		reviewer.setText(LoginActivity.user.getFullName());
		profilePictureView.setProfileId(LoginActivity.user.getFacebook_id());
		submitButton.setTextColor(color);
		submitButton.setOnClickListener(new SumbitReviewListener(mSP, review));
	}

	class SumbitReviewListener implements OnClickListener, AsyncResponse{

		Button v;
		ServiceProvider sp;
		EditText review;
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");

		public SumbitReviewListener(ServiceProvider sp, EditText review) {
			this.sp = sp;
			this.review = review;
		}
		@Override
		public void onClick(View v) {
			String reviewBody = review.getText().toString();
			if (!reviewBody.trim().equals("")) {
				this.v = (Button) v;
				// Create Review
				Review review = new Review("0", LoginActivity.user, reviewBody, 0);
				// Add Review
				AsyncRequest asyncRequest = new AsyncRequest(SumbitReviewListener.this);
				asyncRequest.execute(Server.SERVER_ACTION_ADD_REVIEW_TO_SP, this.sp, review);
			} else {
				Toast.makeText(v.getContext().getApplicationContext(), "Oops! Review text cannot be empty.",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void handleResult(AsyncResult result) {
			if (result.errored()) {
				Toast.makeText(v.getContext().getApplicationContext(), "Oops! Unable to add your review.",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(v.getContext().getApplicationContext(), "Excellent! Your review was added.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

    class LikeListener implements OnClickListener, AsyncResponse{
        int mReviewPos;
        TextView v;
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");

        public LikeListener(int reviewPos) {
            this.mReviewPos = reviewPos;
        }
        @Override
        public void onClick(View v) {
            this.v =(TextView) v;
            // Register Like
            AsyncRequest asyncRequest = new AsyncRequest(LikeListener.this);
            asyncRequest.execute(Server.SERVER_ACTION_ADD_LIKE_TO_REVIEW, LoginActivity.user, mReviewsList.get(mReviewPos));
        }

        @Override
        public void handleResult(AsyncResult result) {
            if (result.errored()) {
				String message;
				if (result.getStatusCode() == 400){
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

	public void onWazeClick() {
		onMarkerClick(null);
	}

	public class mListAdapter extends BaseAdapter {
		private List<Review> reviewsList;
		private LayoutInflater mInflater;

		public mListAdapter(Context screenSlidePageFragment,
				List<Review> reviewsList) {
			this.reviewsList = reviewsList;
			this.mInflater = LayoutInflater.from(screenSlidePageFragment);
		}

		@Override
		public int getCount() {
			return reviewsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return reviewsList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.location_review_list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.review_item_title);
				holder.body = (TextView) convertView
						.findViewById(R.id.review_item_body);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(reviewsList.get(position).getUser().getFullName());
			holder.body.setText(reviewsList.get(position).getContent());

			// Define Font
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
			
			holder.title.setTypeface(font);
			holder.body.setTypeface(font);
			
			return convertView;
		}

		class ViewHolder {
			TextView title, body;
		}
	}

	public List<Review> getReviews() {

		mReviewsList = mSP.getReviewsAsList();
/* 		These are Mock reviews for testing
 *
		ReviewObj review1 = new ReviewObj();
		review1.setTitle("TitleA1");
		review1.setReview("Great Place, Very Accecible");
		review1.setReviewer("Rony Jacobson");
		review1.setPoints(1);
		mReviewsList.add(review1);

		ReviewObj review2 = new ReviewObj();
		review2.setTitle("TitleA2");
		review2.setReview("I couldent find a close parking spot");
		review2.setReviewer("John Doh");
		review2.setPoints(10);
		mReviewsList.add(review2);

		ReviewObj review3 = new ReviewObj();
		review3.setTitle("TitleA3");
		review3.setReview("Good Atmospheir and great Service");
		review3.setReviewer("Jane Doh");
		review3.setPoints(0);
		mReviewsList.add(review3);
		*/
		return mReviewsList;
	}

	private void SetViewColors(View v, int color) {

		// Title bg
		((LinearLayout) v.findViewById(R.id.layout_location_name_bar))
				.setBackgroundColor(color);

		// Review text color
		((Button) v.findViewById(R.id.button_read_more_reviews))
				.setTextColor(color);

		// Verified
		((LinearLayout) v.findViewById(R.id.button_get_verified))
				.setBackgroundColor(color);
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		try {

			String lat = String.valueOf(mSP.getLatitude());
			String lon = String.valueOf(mSP.getLongitude());
			String url = "waze://?ll=" + lat + "," + lon + "&navigate=yes";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=com.waze"));
			startActivity(intent);
		}
		return false;
	}

}
