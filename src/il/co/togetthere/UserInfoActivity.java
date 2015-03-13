package il.co.togetthere;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class UserInfoActivity extends Activity {

	GraphUser mUser;
	TextView mLocationView;
	TextView mNameView;
	TextView mPointsView;
	TextView mLevelView;
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

		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();

		// Profile Picture
		mUser = LoginActivity.user.returnFacebookUser();
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.image_info_facebook_picture);
		if (mUser != null) {
			// Show Profile Picture
			profilePictureView.setVisibility(View.VISIBLE);
			profilePictureView.setProfileId(mUser.getId());
		}

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
		mLocationView = (TextView) findViewById(R.id.text_user_adress);
		mNameView = (TextView) findViewById(R.id.text_user_name);
		mPointsView = (TextView) findViewById(R.id.text_points);
		mLevelView = (TextView) findViewById(R.id.text_level);
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
		// TODO user location - mLocationView.setText(User.getLocation());
		// TODO user name- mNameView.setText(User.getName());
		// TODO user points- mPointsView.setText(User.getPoints());
		// TODO user level -mLevelView.setText(User.getLevel());
		mDiscard.setVisibility(View.GONE);
		mEmailSwitcher = (ViewSwitcher) findViewById(R.id.email_switcher);
		mPhoneSwitcher = (ViewSwitcher) findViewById(R.id.phone_switcher);
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
