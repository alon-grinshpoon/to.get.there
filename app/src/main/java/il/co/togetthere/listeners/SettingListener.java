package il.co.togetthere.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;

import com.facebook.Session;

import il.co.togetthere.LoginActivity;
import il.co.togetthere.R;

/**
 * Listener for setting menu action.
 */
public class SettingListener implements PopupMenu.OnMenuItemClickListener{

    private Context context;

    public SettingListener(Context context){
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuitem_version:
                // Show Message
                Toast.makeText(this.context, "Version 2.0", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_logout:
                // Clear shared preferences
                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this.context.getApplicationContext());
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.clear();
                prefsEditor.commit();
                // Log out from Facebook
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().closeAndClearTokenInformation();
                }
                Session.setActiveSession(null);
                // Show Message
                Toast.makeText(this.context, "You are now logged out.", Toast.LENGTH_SHORT).show();
                // Return to login screen and close this screen
                Intent mainIntent = new Intent(this.context, LoginActivity.class);
                this.context.startActivity(mainIntent);
                ((Activity) this.context).finish();
                return true;
        }
        return false;
    }
}
