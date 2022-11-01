package zw.co.nm.movies.ui.host;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import zw.co.nm.movies.databinding.ActivityHostBinding;
import zw.co.nm.movies.utils.NetworkChangeListener;

public class HostActivity extends AppCompatActivity {

    private ActivityHostBinding activityHostBinding;
    private NetworkChangeListener networkChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHostBinding = ActivityHostBinding.inflate(getLayoutInflater());
        setContentView(activityHostBinding.getRoot());
        networkChangeListener = new NetworkChangeListener();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}