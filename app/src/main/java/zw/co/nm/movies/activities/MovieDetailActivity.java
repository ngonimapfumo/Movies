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
    private Bundle extras;
    private String imgUrl;
    private String backgroundImgUrl;
    private String summary;
    private String year;
    private String runtime;
    private String rating;
    private String title;
    private String trailerCode;
    private String genres;

  //  private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
      //  movieDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(MovieDetailViewModel.class);

        extras = getIntent().getExtras();
        imgUrl = extras.getString("medium_cover_image");
        backgroundImgUrl = extras.getString("background_image_original");
        summary = extras.getString("summary");
        year = extras.getString("year");
        runtime = extras.getString("runtime");
        rating = extras.getString("rating");
        title = extras.getString("title");
        trailerCode = extras.getString("yt_trailer_code");
        genres = extras.getString("genres");
        Picasso.get().load(imgUrl).into(activityMovieDetailBinding.imgv);
        activityMovieDetailBinding.summaryTxt.setText(summary);
        activityMovieDetailBinding.yearTxt.setText(year);
        activityMovieDetailBinding.ratingTxt.setText(rating);
        activityMovieDetailBinding.runtimeTxt.setText(String.format("%s minutes", runtime));
        activityMovieDetailBinding.genres.setText(genres);

        activityMovieDetailBinding.backBtn.setOnClickListener(view -> {onBackPressed();});
        if (trailerCode.equals("")) {
           activityMovieDetailBinding.youtubePlayer.setVisibility(View.GONE);
           activityMovieDetailBinding.backgroundImm.setVisibility(View.VISIBLE);
           activityMovieDetailBinding.trailer404.setVisibility(View.VISIBLE);
        }

        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(trailerCode);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(MovieDetailActivity.this, "video initialization failed", Toast.LENGTH_SHORT).show();
            }
        };
        if (activityMovieDetailBinding.youtubePlayer != null) {
            activityMovieDetailBinding.youtubePlayer.initialize(BuildConfig.API_KEY, listener);
        }


    }


}