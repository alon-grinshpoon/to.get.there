package il.co.togetthere;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import il.co.togetthere.db.User;

public class UserInfoActivity extends Activity {

	User mUser;
	TextView mLocationView;
	TextView mNameView;
	TextView mPointsView;
	TextView mPointsTitleView;
	TextView mLevelView;
	TextView mLevelTitleView;
	TextView mPhoneTextView;
	TextView mEmailTextView;
	EditText mEditEmailView;
	EditText mEditPhoneView;
	TextView mEditUpdateButton;
	TextView mVolunteerQuestion;
	TextView mVolunteerToggle;
	Button mDiscard;
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
					// Saving....
					((Button) v).setText(R.string.editProfile);
					String newEmail = mEditEmailView.getText().toString();
					String newPhone = mEditPhoneView.getText().toString();
					LoginActivity.user.setPhone(newPhone);
					LoginActivity.user.setEmail(newEmail);
					mEmailTextView.setText(newEmail);
					mPhoneTextView.setText(newPhone);
					LoginActivity.user.setVolunteering(mVolunteerToggle.getText().toString()
							.equals("Yes") ? true : false);
					setFixedDetailsView();

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
		
		mLocationView = (TextView) findViewById(R.id.text_user_adress);
		mNameView = (TextView) findViewById(R.id.text_user_name);
		mPointsView = (TextView) findViewById(R.id.text_points);
		mPointsTitleView = (TextView) findViewById(R.id.text_points_title);
		mLevelView = (TextView) findViewById(R.id.text_level);
		mLevelTitleView = (TextView) findViewById(R.id.text_level_title);
		mPhoneTextView = (TextView) findViewById(R.id.text_user_phone);
		mEmailTextView = (TextView) findViewById(R.id.text_user_email);
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
		mLocationView.setText(LoginActivity.user.getLocation());
		mNameView.setText(LoginActivity.user.getFullName());
		mPointsView.setText(String.valueOf(LoginActivity.user.getPoints()));
		mLevelView.setText(String.valueOf(LoginActivity.user.getLevel()));
		mDiscard.setVisibility(View.GONE);
		mEmailSwitcher = (ViewSwitcher) findViewById(R.id.email_switcher);
		mPhoneSwitcher = (ViewSwitcher) findViewById(R.id.phone_switcher);
		
		// Set fonts
		mLocationView.setTypeface(font);
		mNameView.setTypeface(fontBold);
		mPointsView.setTypeface(font);
		mPointsTitleView.setTypeface(fontBold);
		mLevelView.setTypeface(font);
		mLevelTitleView.setTypeface(fontBold);
		mPhoneTextView.setTypeface(font);
		mEmailTextView.setTypeface(font);
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
		mEditEmailView.setVisibility(View.INVISIBLE);
		mEditPhoneView.setVisibility(View.INVISIBLE);
		mEmailTextView.setVisibility(View.VISIBLE);
		mPhoneTextView.setVisibility(View.VISIBLE);
		mDiscard.setVisibility(View.GONE);
		mEmailSwitcher.showPrevious();
		mPhoneSwitcher.showPrevious();
		mVolunteerQuestion
				.setText(LoginActivity.user.isVolunteering() == true ? R.string.willingYes
						: R.string.willingNo);
	}

	private void setEditDetailsView() {
		mEditUpdateButton.setText(getString(R.string.saveChanges));
		// set email and phone
		mEditEmailView.setVisibility(View.VISIBLE);
		mEditPhoneView.setVisibility(View.VISIBLE);
		mEmailTextView.setVisibility(View.INVISIBLE);
		mPhoneTextView.setVisibility(View.INVISIBLE);
		mEditEmailView.setText(mEmailTextView.getText());
		mEditPhoneView.setText(mPhoneTextView.getText());
		mDiscard.setVisibility(View.VISIBLE);
		mEmailSwitcher.showNext();
		mPhoneSwitcher.showNext();
		// set volunteering
		mVolunteerQuestion.setText(getString(R.string.willingtoVolunteer));
		mVolunteerToggle.setVisibility(View.VISIBLE);

	}
}
