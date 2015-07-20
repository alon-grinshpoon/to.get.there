package il.co.togetthere;

import il.co.togetthere.db.AmazonClientManager;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TypeChooserActivity extends Activity {
	List<String> mServiceProviderType = new ArrayList<String>();
	List<Button> mButtonsArr = new ArrayList<Button>();

	public static AmazonClientManager clientManager = null;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_typechooser);

		// Set Profile Picture
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_chooser);
		profilePictureView.setProfileId(LoginActivity.user.getID());

		// database client manager initialize
		clientManager = new AmazonClientManager(this);

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

		/**
		 * Types Initialization
		 */
		mServiceProviderType.addAll(Arrays.asList("medical", "shopping",
				"help", "transport", "restaurants", "public_services"));

		/**
		 * Layout Initialization
		 */
		for (int i = 0; i < mServiceProviderType.size(); i++) {
			final String type = mServiceProviderType.get(i);
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
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	askToVolunteer();
            }
        }, 1500);

		// set Lower Bar
		((ProfilePictureView) findViewById(R.id.button_show_user_details_chooser  ))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent infoIntent = new Intent(
								TypeChooserActivity.this,
								UserInfoActivity.class);
						startActivity(infoIntent);

					}
				});

		// TODO set button app info

	}
	
	private void askToVolunteer(){

    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                	// Set Volunteer State
                	LoginActivity.user.setVolunteering(true);
                	// Show toast
                	Toast.makeText(getApplicationContext(), "Great! You are awesome!",
              			   Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                	// Set Volunteer State
                	LoginActivity.user.setVolunteering(false);
                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Will you be willing to volunteer?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
    }
}
