package zw.co.nm.movies.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zw.co.nm.movies.BuildConfig;
import zw.co.nm.movies.R;
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMovieDetailResponse;
import zw.co.nm.movies.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends YouTubeBaseActivity {

    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private String ytTrailer;
    private String movieTitle;
    private String imgUrl;
    private String movieYear;
    private String movieSummary;
    private String movieMPARating;
    private String movieRating;
    private int movieDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
        String movieId = getIntent().getStringExtra("movieId");
        getMovieDetail(movieId);
        activityMovieDetailBinding.backImg.setOnClickListener(view -> {
            onBackPressed();
        });
        activityMovieDetailBinding.mainLayout.setVisibility(View.GONE);
    }

    private void getMovieDetail(String id) {
        Call<GetMovieDetailResponse> call = Retrofit.getService().getMovieDetail(id, true);
        call.enqueue(new Callback<GetMovieDetailResponse>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<GetMovieDetailResponse> call, @NonNull Response<GetMovieDetailResponse> response) {
                if (response.isSuccessful()) {
                    activityMovieDetailBinding.mainLayout.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        imgUrl = response.body().getData().movie.medium_cover_image;
                        ytTrailer = response.body().getData().movie.yt_trailer_code;
                        movieTitle = response.body().getData().movie.title;
                        movieYear = String.valueOf(response.body().getData().movie.year);
                        movieSummary = response.body().getData().movie.description_intro;
                        movieMPARating = response.body().getData().movie.mpa_rating;
                        movieDuration = response.body().getData().movie.runtime;
                        movieRating = String.valueOf(response.body().getData().movie.rating);
                    }
                    Picasso.get().load(imgUrl)
                            .placeholder(R.drawable.sample_cover_large)
                            .into(activityMovieDetailBinding.imgv);

                    if (response.body().getData().movie.yt_trailer_code.equals("")) {
                        activityMovieDetailBinding.youtubePlayer.setVisibility(View.GONE);
                        activityMovieDetailBinding.backgroundImm.setVisibility(View.VISIBLE);
                        activityMovieDetailBinding.trailer404.setVisibility(View.VISIBLE);
                    } else {
                        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                youTubePlayer.setShowFullscreenButton(false);
                                youTubePlayer.cueVideo(ytTrailer);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                Toast.makeText(MovieDetailActivity.this, "video initialization failed", Toast.LENGTH_SHORT).show();
                            }
                        };
                        activityMovieDetailBinding.youtubePlayer.initialize(BuildConfig.API_KEY, listener);

                    }
                    activityMovieDetailBinding.movieTitleTxt.setText(movieTitle);
                    activityMovieDetailBinding.yearTxt.setText(movieYear);
                    activityMovieDetailBinding.movieSummaryTxt.setText(movieSummary);
                    activityMovieDetailBinding.durationTxt.setText(String.format(" %dmins", movieDuration));
                    activityMovieDetailBinding.movieRatingTxt.setText(movieRating);
                    if (movieMPARating.equals("")) {
                        activityMovieDetailBinding.movieMpaRatingTxt.setText("N/A");
                    } else
                        activityMovieDetailBinding.movieMpaRatingTxt.setText(movieMPARating);
                }
            }

            @Override
            public void onFailure(Call<GetMovieDetailResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);

            }
        });
    }


}