package zw.co.nm.movies.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();

        }
    }
}
