package il.co.togetthere;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;

public class SettingListener implements PopupMenu.OnMenuItemClickListener{

    private Context context;

    SettingListener(Context context){
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuitem_settings:
                Toast.makeText(this.context, "Version 2.0", Toast.LENGTH_SHORT).show();

                return true;
        }
        return false;
    }
}
