package il.co.togetthere;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;

import il.co.togetthere.db.ServiceProvider;

public class RankingListener implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private ServiceProvider sp;

    RankingListener(Context context, ServiceProvider sp){
        this.context = context;
        this.sp = sp;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.option_rank5:
                Toast.makeText(this.context, "5" + sp.getId(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_rank4:
                Toast.makeText(this.context, "4" + sp.getId(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_rank3:
                Toast.makeText(this.context, "3" + sp.getId(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_rank2:
                Toast.makeText(this.context, "2" + sp.getId(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_rank1:
                Toast.makeText(this.context, "1" + sp.getId(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}