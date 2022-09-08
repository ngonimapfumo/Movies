package zw.co.nm.movies.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import zw.co.nm.movies.BuildConfig;
import zw.co.nm.movies.activities.viewmodels.MovieDetailViewModel;
import zw.co.nm.movies.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends YouTubeBaseActivity {

    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private String movieId;

  //  private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
      //  movieDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(MovieDetailViewModel.class);

        movieId = getIntent().getStringExtra("movieId");
        Picasso.get().load("imgUrl").into(activityMovieDetailBinding.imgv);

        activityMovieDetailBinding.backImg.setOnClickListener(view -> {onBackPressed();});
        /*if (trailerCode.equals("")) {
           activityMovieDetailBinding.youtubePlayer.setVisibility(View.GONE);
           activityMovieDetailBinding.backgroundImm.setVisibility(View.VISIBLE);
           activityMovieDetailBinding.trailer404.setVisibility(View.VISIBLE);
        }*/

        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("PmeRjrz8KVw");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(MovieDetailActivity.this, "video initialization failed", Toast.LENGTH_SHORT).show();
            }
        };
        activityMovieDetailBinding.youtubePlayer.initialize(BuildConfig.API_KEY, listener);


    }


}