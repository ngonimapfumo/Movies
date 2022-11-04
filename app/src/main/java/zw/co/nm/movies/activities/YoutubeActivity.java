package zw.co.nm.movies.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import zw.co.nm.movies.BuildConfig;
import zw.co.nm.movies.databinding.ActivityYoutubeBinding;

public class YoutubeActivity extends YouTubeBaseActivity {

    private String ytCode;
    private ActivityYoutubeBinding activityYoutubeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityYoutubeBinding = ActivityYoutubeBinding.inflate(getLayoutInflater());
        setContentView(activityYoutubeBinding.getRoot());
        ytCode = getIntent().getStringExtra("trailerCode");
        loadVideo();
    }

    private void loadVideo() {
        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
              //  youTubePlayer.setShowFullscreenButton(true);
                youTubePlayer.cueVideo(ytCode);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YoutubeActivity.this, "video initialization failed", Toast.LENGTH_SHORT).show();
            }
        };
        activityYoutubeBinding.youtubePlayer.initialize(BuildConfig.API_KEY, listener);

    }
}
