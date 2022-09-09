package zw.co.nm.movies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zw.co.nm.movies.BuildConfig;
import zw.co.nm.movies.R;
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMovieDetailResponse;
import zw.co.nm.movies.api.responses.GetMovieResponse;
import zw.co.nm.movies.databinding.ActivityMovieDetailBinding;
import zw.co.nm.movies.models.Movie;
import zw.co.nm.movies.ui.adapters.MovieListAdapter;
import zw.co.nm.movies.utils.Utils;

public class MovieDetailActivity extends YouTubeBaseActivity implements MovieListAdapter.onMovieItemClick {

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
    private MovieListAdapter movieListAdapter;
    private List<Movie> movies;
    private String movieId;
    private List<String> movieIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
        movieId = getIntent().getStringExtra("movieId");
        getMovieDetail(movieId);
        getMovieSuggestions(movieId);
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

    private void getMovieSuggestions(String movieId) {
        movies = new ArrayList<>();
        movieIds = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        Call<GetMovieResponse> call = Retrofit.getService().getMovieSuggestions(movieId);
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                String resString = null;
                if (response.body() != null) {
                    resString = new Gson().toJson(response.body().getData().movies);
                }
                JSONArray jsonArray = Utils.getJsonArray(resString);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = Utils.getJsonObject(jsonArray, i);
                        Movie movie = new Gson().fromJson(Objects.requireNonNull(obj).toString(), Movie.class);
                        movies.add(movie);
                        try {
                            movieIds.add(obj.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieListAdapter = new MovieListAdapter(movies, MovieDetailActivity.this, MovieDetailActivity.this);
                        activityMovieDetailBinding.recyclerView.setHasFixedSize(true);
                        activityMovieDetailBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        activityMovieDetailBinding.recyclerView.setAdapter(movieListAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onMovieItemClick(int position) {
        startActivity(new Intent(this, MovieDetailActivity.class).putExtra("movieId", movieIds.get(position)));
    }
}