package il.co.togetthere;

import com.facebook.widget.ProfilePictureView;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import il.co.togetthere.db.User;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

public class UserInfoActivity extends Activity implements AsyncResponse {

	User mUser;
	TextView mNameView;
	TextView mPointsView;
	TextView mPointsTitleView;
	TextView mLevelView;
	TextView mLevelTitleView;
	TextView mPhoneTextView;
	TextView mEmailTextView;
	TextView mAddressTextView;
	EditText mEditAddressView;
	EditText mEditEmailView;
	EditText mEditPhoneView;
	TextView mEditUpdateButton;
	TextView mVolunteerQuestion;
	TextView mVolunteerToggle;
	Button mDiscard;
	ViewSwitcher mAddressSwitcher;
	ViewSwitcher mEmailSwitcher;
	ViewSwitcher mPhoneSwitcher;

	@SuppressLint("InlinedApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		/**
		 * View Initialization
		 **/

		// Hide the status bar.
		//View decorView = getWindow().getDecorView();
		//int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		//decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		//android.app.ActionBar actionBar = getActionBar();
		//actionBar.hide();

		// Profile Picture
		mUser = LoginActivity.user;
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.image_info_facebook_picture);
		if (mUser != null) {
			// Show Profile Picture
			profilePictureView.setVisibility(View.VISIBLE);
			profilePictureView.setProfileId(mUser.getFacebook_id());
		}

		// Configure Settings Button
		findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PopupMenu popupMenu = new PopupMenu(UserInfoActivity.this, view);
				popupMenu.setOnMenuItemClickListener(new SettingListener(UserInfoActivity.this));
				popupMenu.inflate(R.menu.settings_menu);
				popupMenu.show();
			}
		});

		// Initialize View
		InitializeViewMembers();

		mEditUpdateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mEditUpdateButton.getText().equals(
						getString(R.string.editProfile))) {
					setEditDetailsView();
				} else {
					// Save on screen
					((Button) v).setText(R.string.editProfile);
					String newAddress = mEditAddressView.getText().toString();
					String newEmail = mEditEmailView.getText().toString();
					String newPhone = mEditPhoneView.getText().toString();
					// Update user
					LoginActivity.user.setPhone(newPhone);
					LoginActivity.user.setEmail(newEmail);
					LoginActivity.user.setLocation(newAddress);
					LoginActivity.user.setVolunteering(mVolunteerToggle.getText().toString()
							.equals("Yes") ? true : false);
					// Update screen
					mAddressTextView.setText(newAddress);
					mEmailTextView.setText(newEmail);
					mPhoneTextView.setText(newPhone);
					setFixedDetailsView();
					// Save on server
					AsyncRequest asyncRequest = new AsyncRequest(UserInfoActivity.this);
					asyncRequest.execute(Server.SERVER_ACTION_EDIT_USER_BY_ID, LoginActivity.user, Integer.parseInt(LoginActivity.user.getID()));
				}

			}

		});

		// Wiling to volunteer
		mVolunteerToggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView volunteer = (TextView) v;
				if (volunteer.getText().toString().equals("Yes")) {
					volunteer.setText("No");
					volunteer.setBackgroundColor(0x66FF4444);
				} else {
					volunteer.setText("Yes");
					volunteer.setBackgroundColor(0x6070AD47);
				}
			}
		});

		mDiscard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setFixedDetailsView();
			}
		});
	}

	private void InitializeViewMembers() {
		// Define Font
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");
		Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/GOTHICB.TTF");

		mNameView = (TextView) findViewById(R.id.text_user_name);
		mPointsView = (TextView) findViewById(R.id.text_points);
		mPointsTitleView = (TextView) findViewById(R.id.text_points_title);
		mLevelView = (TextView) findViewById(R.id.text_level);
		mLevelTitleView = (TextView) findViewById(R.id.text_level_title);
		mPhoneTextView = (TextView) findViewById(R.id.text_user_phone);
		mEmailTextView = (TextView) findViewById(R.id.text_user_email);
		mAddressTextView = (TextView) findViewById(R.id.text_user_address);
		mEditAddressView = (EditText) findViewById(R.id.edit_text_user_address);
		mEditEmailView = (EditText) findViewById(R.id.edit_text_user_email);
		mEditPhoneView = (EditText) findViewById(R.id.edit_text_user_phone);
		mEditUpdateButton = (Button) findViewById(R.id.button_edit_update_profile);
		mVolunteerQuestion = (TextView) findViewById(R.id.text_volunteer);
		mVolunteerToggle = (TextView) findViewById(R.id.button_willing_volunteer);
		mDiscard = (Button) findViewById(R.id.button_discard);
		mVolunteerToggle.setVisibility(View.GONE);
		mVolunteerQuestion
				.setText(LoginActivity.user.isVolunteering() == true ? R.string.willingYes
						: R.string.willingNo);
		mEmailTextView.setText(LoginActivity.user.getEmail());
		mPhoneTextView.setText(LoginActivity.user.getPhone());
		mAddressTextView.setText(LoginActivity.user.getLocation());
		mNameView.setText(LoginActivity.user.getFullName());
		mPointsView.setText(String.valueOf(LoginActivity.user.getPoints()));
		mLevelView.setText(String.valueOf(LoginActivity.user.getLevel()));
		mDiscard.setVisibility(View.GONE);
		mAddressSwitcher = (ViewSwitcher) findViewById(R.id.address_switcher);
		mEmailSwitcher = (ViewSwitcher) findViewById(R.id.email_switcher);
		mPhoneSwitcher = (ViewSwitcher) findViewById(R.id.phone_switcher);
		
		// Set fonts
		mNameView.setTypeface(fontBold);
		mPointsView.setTypeface(font);
		mPointsTitleView.setTypeface(fontBold);
		mLevelView.setTypeface(font);
		mLevelTitleView.setTypeface(fontBold);
		mPhoneTextView.setTypeface(font);
		mEmailTextView.setTypeface(font);
		mAddressTextView.setTypeface(font);
		mEditAddressView.setTypeface(font);
		mEditEmailView.setTypeface(font);
		mEditPhoneView.setTypeface(font);
		mEditUpdateButton.setTypeface(fontBold);
		mVolunteerQuestion.setTypeface(font);
		mVolunteerToggle.setTypeface(font);
		mDiscard.setTypeface(font);
		
	}

	private void setFixedDetailsView() {
		mVolunteerToggle.setVisibility(View.INVISIBLE);
		mEditUpdateButton.setText(getString(R.string.editProfile));
		mEditAddressView.setVisibility(View.INVISIBLE);
		mEditEmailView.setVisibility(View.INVISIBLE);
		mEditPhoneView.setVisibility(View.INVISIBLE);
		mAddressTextView.setVisibility(View.VISIBLE);
		mEmailTextView.setVisibility(View.VISIBLE);
		mPhoneTextView.setVisibility(View.VISIBLE);
		mDiscard.setVisibility(View.GONE);
		mAddressSwitcher.showPrevious();
		mEmailSwitcher.showPrevious();
		mPhoneSwitcher.showPrevious();
		mVolunteerQuestion
				.setText(LoginActivity.user.isVolunteering() == true ? R.string.willingYes
						: R.string.willingNo);
	}

	private void setEditDetailsView() {
		mEditUpdateButton.setText(getString(R.string.saveChanges));
		// set email and phone
		mEditAddressView.setVisibility(View.VISIBLE);
		mEditEmailView.setVisibility(View.VISIBLE);
		mEditPhoneView.setVisibility(View.VISIBLE);
		mEmailTextView.setVisibility(View.INVISIBLE);
		mPhoneTextView.setVisibility(View.INVISIBLE);
		mEditAddressView.setText(mAddressTextView.getText());
		mEditEmailView.setText(mEmailTextView.getText());
		mEditPhoneView.setText(mPhoneTextView.getText());
		mDiscard.setVisibility(View.VISIBLE);
		mAddressSwitcher.showNext();
		mEmailSwitcher.showNext();
		mPhoneSwitcher.showNext();
		// set volunteering
		mVolunteerQuestion.setText(getString(R.string.willingtoVolunteer));
		mVolunteerToggle.setVisibility(View.VISIBLE);

	}

	@Override
	public void handleResult(AsyncResult result) {
		if (result.errored()){
			// Show message
			Toast.makeText(getApplicationContext(), "Oops! Unable to update your user.",
					Toast.LENGTH_SHORT).show();
		} else {
			// Save user as a shared preference
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(LoginActivity.user);
			prefsEditor.putString("User", json);
			prefsEditor.commit();
			// Show message
			Toast.makeText(getApplicationContext(), "Awesome! your user was update.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
