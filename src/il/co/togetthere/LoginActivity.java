/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package il.co.togetthere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

import il.co.togetthere.db.AmazonClientManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LoginActivity extends FragmentActivity {

    @SuppressWarnings("unused")
	private static final String PERMISSION = "publish_actions";

    private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";

    private LoginButton buttonLoginFacebook;
    private Button buttonLoginGoogle;
    private Button buttonLoginTwitter;
    private ProfilePictureView profilePictureView;
    private PendingAction pendingAction = PendingAction.NONE;
    private ViewGroup controlsContainer;
    private GraphUser facebookUser;
    private GraphPlace place;
    private List<GraphUser> tags;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    
    public static AmazonClientManager clientManager = null;
    public static User user = new User();
    
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    @SuppressLint("InlinedApi") @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions); 
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();
		
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.activity_login);

        //initialize db credentialis for registering user
        clientManager = new AmazonClientManager(this);
        
        
        // Hide Profile Picture
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setVisibility(View.GONE);
        
        // Configure the Facebook login button
        buttonLoginFacebook = (LoginButton) findViewById(R.id.button_login_facebook);
        buttonLoginFacebook.setBackgroundResource(R.drawable.button_login_facebook);
        buttonLoginFacebook.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        buttonLoginFacebook.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                LoginActivity.this.facebookUser = user;
                LoginActivity.user.init(user); //set facebook user in user
                //TelephonyManager tMgr =  (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //LoginActivity.user.setPhone(tMgr.getLine1Number());
                if (user != null) LoginActivity.user.syncDB();
                
                updateUI();
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
                handlePendingAction();
            }
        });

        // Configure the Google login button
        buttonLoginGoogle = (Button) findViewById(R.id.button_login_google);
        buttonLoginGoogle.setBackgroundResource(R.drawable.button_login_google);
        buttonLoginGoogle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Toast.makeText(getApplicationContext(), "Coming soon...",
         			   Toast.LENGTH_SHORT).show();
            }
        });

        // Configure the Twitter login button
        buttonLoginTwitter = (Button) findViewById(R.id.button_login_twitter);
        buttonLoginTwitter.setBackgroundResource(R.drawable.button_login_twitter);
        buttonLoginTwitter.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        buttonLoginTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Toast.makeText(getApplicationContext(), "Coming soon...",
            			   Toast.LENGTH_SHORT).show();
            }
        });

        controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);

        final FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            // If we're being re-created and have a fragment, we need to a) hide the main UI controls and
            // b) hook up its listeners again.
            controlsContainer.setVisibility(View.GONE);
            if (fragment instanceof FriendPickerFragment) {
                setFriendPickerListeners((FriendPickerFragment) fragment);
            } else if (fragment instanceof PlacePickerFragment) {
                setPlacePickerListeners((PlacePickerFragment) fragment);
            }
        }

        // Listen for changes in the back stack so we know if a fragment got popped off because the user
        // clicked the back button.
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fm.getBackStackEntryCount() == 0) {
                    // We need to re-show our UI.
                    controlsContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        // Can we present the share dialog for regular links?
        canPresentShareDialog = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.PHOTOS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
        updateUI();
    }

    @SuppressWarnings("unused")
	private void updateUI() {
        Session session = Session.getActiveSession();

        if (facebookUser != null) {
        	// Hide Buttons
        	buttonLoginFacebook.setEnabled(false);
        	buttonLoginFacebook.setVisibility(View.GONE);
        	buttonLoginGoogle.setEnabled(false);
        	buttonLoginGoogle.setVisibility(View.GONE);
        	buttonLoginTwitter.setEnabled(false);
        	buttonLoginTwitter.setVisibility(View.GONE);
        	
        	// Show Profile Picture
        	profilePictureView.setVisibility(View.VISIBLE);
            profilePictureView.setProfileId(facebookUser.getId());
            
            // Ask To Volunteer
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                	askToVolunteer();
                }
            }, 1500);
            
        }
    }

    private void continuteToNextScreen() {
    	new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(LoginActivity.this, TypeChooserActivity.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
            }
        }, 2000);
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
                	// Continue to next screen
                    continuteToNextScreen();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                	// Set Volunteer State
                	LoginActivity.user.setVolunteering(false);
                	// Continue to next screen
                    continuteToNextScreen();
                    break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Will you be willing to volunteer?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
    }
    
    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }

    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.successfully_posted_post, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private FacebookDialog.ShareDialogBuilder createShareDialogBuilderForLink() {
        return new FacebookDialog.ShareDialogBuilder(this)
                .setName("Hello Facebook")
                .setDescription("The 'Hello Facebook' sample application showcases simple Facebook integration")
                .setLink("http://developers.facebook.com/android");
    }

    private void postStatusUpdate() {
        if (canPresentShareDialog) {
            FacebookDialog shareDialog = createShareDialogBuilderForLink().build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else if (facebookUser != null && hasPublishPermission()) {
            final String message = getString(R.string.status_update, facebookUser.getFirstName(), (new Date().toString()));
            Request request = Request
                    .newStatusUpdateRequest(Session.getActiveSession(), message, place, tags, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            showPublishResult(message, response.getGraphObject(), response.getError());
                        }
                    });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private FacebookDialog.PhotoShareDialogBuilder createShareDialogBuilderForPhoto(Bitmap... photos) {
        return new FacebookDialog.PhotoShareDialogBuilder(this)
                .addPhotos(Arrays.asList(photos));
    }

    private void postPhoto() {
        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
        if (canPresentShareDialogWithPhotos) {
            FacebookDialog shareDialog = createShareDialogBuilderForPhoto(image).build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else if (hasPublishPermission()) {
            Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
                }
            });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_PHOTO;
        }
    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });
    }

    private void onFriendPickerDone(FriendPickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String results = "";

        List<GraphUser> selection = fragment.getSelection();
        tags = selection;
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = getString(R.string.no_friends_selected);
        }

        showAlert(getString(R.string.you_picked), results);
    }

    private void onPlacePickerDone(PlacePickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String result = "";

        GraphPlace selection = fragment.getSelection();
        if (selection != null) {
            result = selection.getName();
        } else {
            result = getString(R.string.no_place_selected);
        }

        place = selection;

        showAlert(getString(R.string.you_picked), result);
    }

    private void setPlacePickerListeners(final PlacePickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new PlacePickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onPlacePickerDone(fragment);
            }
        });
        fragment.setOnSelectionChangedListener(new PlacePickerFragment.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(PickerFragment<?> pickerFragment) {
                if (fragment.getSelection() != null) {
                    onPlacePickerDone(fragment);
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }
}
