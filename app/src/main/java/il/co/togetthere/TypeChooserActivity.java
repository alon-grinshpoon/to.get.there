package il.co.togetthere;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.widget.ProfilePictureView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import il.co.togetthere.db.ServiceProviderCategory;

public class TypeChooserActivity extends Activity {
	List<String> mServiceProviderCategory = new ArrayList<>();
	List<Button> mButtonsArr = new ArrayList<>();

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_typechooser);

		// Set Profile Picture
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_chooser);
		profilePictureView.setProfileId(LoginActivity.user.getFacebook_id());

		/**
		 * View Initialization
		 **/

		// Hide the status bar.
		//View decorView = getWindow().getDecorView();
		//int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		//decorView.setSystemUiVisibility(uiOptions);
		
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();

		/**
		 * Types Initialization
		 */
		mServiceProviderCategory.addAll(Arrays.asList(ServiceProviderCategory.enumToString(ServiceProviderCategory.medical),
				ServiceProviderCategory.enumToString(ServiceProviderCategory.shopping),
				ServiceProviderCategory.enumToString(ServiceProviderCategory.help),
				ServiceProviderCategory.enumToString(ServiceProviderCategory.transport),
				ServiceProviderCategory.enumToString(ServiceProviderCategory.restaurants),
				ServiceProviderCategory.enumToString(ServiceProviderCategory.public_services)));


		/**
		 * Search bar Initialization
		 */
		ImageView searchBtn = (ImageView) findViewById(R.id.searchButton);
		searchBtn.setOnClickListener(new MainSearchClickListener());
		EditText searchEditText = (EditText) findViewById(R.id.searchText);
		searchEditText.setHint("Search...");
		/**
		 * Layout Initialization
		 */
		for (int i = 0; i < mServiceProviderCategory.size(); i++) {
			final String type = mServiceProviderCategory.get(i);
			int btnID = getResources().getIdentifier("id_button_" + type, "id",
					getPackageName());
			Button btn = (Button) findViewById(btnID);
			btn.setVisibility(View.VISIBLE);
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent mainIntent = new Intent(TypeChooserActivity.this,
							ScreenSlideActivity.class);
					mainIntent.putExtra("TYPE_EXTRA", type);
					startActivity(mainIntent);
				}

			});

			mButtonsArr.add(i, btn);
		}
		
        // Ask To Volunteer
		if (!LoginActivity.user.wasAskedToVolunteer()) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					askToVolunteer();
				}
			}, 1500);
		}

		// set Upper Bar
		profilePictureView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(
						TypeChooserActivity.this,
						UserInfoActivity.class);
				startActivity(infoIntent);

			}
		});

		// Configure Settings Button
		findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PopupMenu popupMenu = new PopupMenu(TypeChooserActivity.this, view);
				popupMenu.setOnMenuItemClickListener(new SettingListener(TypeChooserActivity.this));
				popupMenu.inflate(R.menu.settings_menu);
				popupMenu.show();
			}
		});
	}
	
	private void askToVolunteer(){

    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                	// Set Volunteer State
                	LoginActivity.user.setVolunteering(true);
					LoginActivity.user.setWasAskedToVolunteer(true);
                	// Show toast
                	Toast.makeText(getApplicationContext(), "Great! You are awesome!",
              			   Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                	// Set Volunteer State
                	LoginActivity.user.setVolunteering(false);
					LoginActivity.user.setWasAskedToVolunteer(true);
					Toast.makeText(getApplicationContext(), "Too bad...",
							Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Will you be willing to volunteer?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
    }

	private class MainSearchClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			Intent mainIntent = new Intent(TypeChooserActivity.this,
					ScreenSlideActivity.class);
			String q = ((EditText) findViewById(R.id.searchText)).getText().toString();
			q= q.trim();
			if (q.equals("")) {
				Log.i("Type Chooser Search", "No query found, not searching");
				return;
			} else {
				Log.i("Type Chooser Search", "Initiating search query: " + q);
				mainIntent.putExtra("TYPE_EXTRA", "search");
				mainIntent.putExtra("SEARCH_QUERY", q);
				startActivity(mainIntent);
			}
		}

	}

}
