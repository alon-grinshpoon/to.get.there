package il.co.togetthere;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;

import java.util.List;

import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

public class RankingListener implements PopupMenu.OnMenuItemClickListener, AsyncResponse {

    private Context context;
    private List<ServiceProvider> serviceProvidersList;
    int rank = 0;

    RankingListener(Context context, List<ServiceProvider> serviceProvidersList){
        this.context = context;
        this.serviceProvidersList = serviceProvidersList;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Define rank value
        switch (menuItem.getItemId()) {
            case R.id.option_rank5:
                rank = 5;
                break;
            case R.id.option_rank4:
                rank = 4;
                break;
            case R.id.option_rank3:
                rank = 3;
                break;
            case R.id.option_rank2:
                rank = 2;
                break;
            case R.id.option_rank1:
                rank = 1;
                break;
        }
        // Rank Service Provider
        ServiceProvider serviceProvider = serviceProvidersList.get(ScreenSlideActivity.currentIndex);
        AsyncRequest asyncRequest = new AsyncRequest(RankingListener.this);
        asyncRequest.execute(Server.SERVER_ACTION_RANK_SP, LoginActivity.user, serviceProvider, rank);
        return true;
    }

    @Override
    public void handleResult(AsyncResult result) {
        if (result.errored()){
            Toast.makeText(this.context, "Oops! Your rank failed to process.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.context, "Great! You gave a " + rank + "-star ranking to " + serviceProvidersList.get(ScreenSlideActivity.currentIndex).getSp_name(), Toast.LENGTH_LONG).show();
        }

    }
}