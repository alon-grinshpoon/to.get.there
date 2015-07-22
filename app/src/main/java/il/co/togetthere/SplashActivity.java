package il.co.togetthere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.gson.Gson;

import java.io.IOException;

import il.co.togetthere.db.User;
import il.co.togetthere.server.Server;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @SuppressLint({ "InlinedApi", "NewApi" }) @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
		// Hide the status bar.
        View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		// Set Layout 
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                /* Disable strict thread policy to enable communication to our server. */
                /* This can also be removed if we don't write network call in Main UI Thread, and use Async Task for that. */
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                /* Retrieve a shared preference */
                SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = mPrefs.getString("User", "");
                User user = gson.fromJson(json, User.class);
                if (user != null){
                    LoginActivity.user = user;
                }

                /* Create an Intent that will start the next Activity. */
            	/* Prompt login if the user isn't logged in or the type choosing screen otherwise. */
            	if (LoginActivity.user == null || !LoginActivity.user.isLoggedIn()){
            		Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
            	} else {
            		Intent mainIntent = new Intent(SplashActivity.this, TypeChooserActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
            	}
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
