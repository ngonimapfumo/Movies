package zw.co.nm.movies.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import zw.co.nm.movies.R;
import zw.co.nm.movies.activities.YoutubeActivity;
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMovieDetailResponse;
import zw.co.nm.movies.api.responses.GetMovieResponse;
import zw.co.nm.movies.databinding.FragmentMovieDetailBinding;
import zw.co.nm.movies.models.Cast;
import zw.co.nm.movies.models.Movie;
import zw.co.nm.movies.ui.adapters.MovieListAdapter;
import zw.co.nm.movies.utils.Utils;

public class MovieDetailFragment extends Fragment implements MovieListAdapter.onMovieItemClick, View.OnClickListener {

    private FragmentMovieDetailBinding fragmentMovieDetailBinding;
    private String movieId;
    private ArrayList castList;
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
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
    private List<String> movieIds;
    private List<String> genres;
    private LinearLayoutManager linearLayoutManager;
    private String description_full;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMovieDetailBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        if(getArguments().getBoolean("fromActivity")){
            movieId= getArguments().getString("movieId");
        }
        else {movieId = MovieDetailFragmentArgs.fromBundle(getArguments()).getMovieId();}
        getMovieDetail(movieId);
        getMovieSuggestions(movieId);
        fragmentMovieDetailBinding.trailerBtn.setOnClickListener(this);
        fragmentMovieDetailBinding.detailedSummaryTxt.setOnClickListener(this);
        return fragmentMovieDetailBinding.getRoot();
    }

    private void getMovieDetail(String id) {
        genres = new ArrayList<>();
        castList= new ArrayList();
        Call<GetMovieDetailResponse> call = Retrofit.getService().getMovieDetail(id, true);
        call.enqueue(new Callback<GetMovieDetailResponse>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<GetMovieDetailResponse> call, @NonNull Response<GetMovieDetailResponse> response) {
                if (response.isSuccessful()) {
                    fragmentMovieDetailBinding.trailerBtn.setVisibility(View.VISIBLE);
                    fragmentMovieDetailBinding.mainLayout.setVisibility(View.VISIBLE);
                    fragmentMovieDetailBinding.llAbout.setVisibility(View.VISIBLE);
                    fragmentMovieDetailBinding.llSuggested.setVisibility(View.VISIBLE);
                    fragmentMovieDetailBinding.constLayInfo.setVisibility(View.VISIBLE);

                    if (response.body() != null) {
                        imgUrl = response.body().getData().movie.large_cover_image;
                        ytTrailer = response.body().getData().movie.yt_trailer_code;
                        movieTitle = response.body().getData().movie.title;
                        movieYear = String.valueOf(response.body().getData().movie.year);
                        movieSummary = response.body().getData().movie.description_full;
                        movieMPARating = response.body().getData().movie.mpa_rating;
                        movieDuration = response.body().getData().movie.runtime;
                        movieRating = String.valueOf(response.body().getData().movie.rating);
                        genres = response.body().getData().movie.genres;
                        description_full= response.body().getData().movie.description_full;
                    }
                    Picasso.get().load(imgUrl)
                            .placeholder(R.drawable.sample_cover_large)
                            .into(fragmentMovieDetailBinding.backgroundImm);

                    if (ytTrailer.equals("")) {
                        fragmentMovieDetailBinding.trailerBtn.setText(R.string.trailer_404);
                        fragmentMovieDetailBinding.trailerBtn.setEnabled(false);
                    } else {
                        fragmentMovieDetailBinding.trailerBtn.setEnabled(true);
                        fragmentMovieDetailBinding.trailerBtn.setText(R.string.watch_trailer_txt);
                    }
                    fragmentMovieDetailBinding.movieTitleTxt.setText(movieTitle);
                    if (movieSummary.equals("")) {
                        fragmentMovieDetailBinding.movieSummaryTxt.setText(R.string.no_info_avail);
                    } else {
                        fragmentMovieDetailBinding.movieSummaryTxt.setText(movieSummary);
                    }
                    fragmentMovieDetailBinding.runtimeTxt.setText(String.format(" %dmins", movieDuration));
                    fragmentMovieDetailBinding.yearTxt.setText(movieYear);
                    fragmentMovieDetailBinding.infoYear.setText(movieYear);
                    if (genres == null) {
                        fragmentMovieDetailBinding.genre.setText("");
                    } else fragmentMovieDetailBinding.genre.setText(genres.get(0));

                    fragmentMovieDetailBinding.detailedSummaryTxt.setText(description_full);

                    fragmentMovieDetailBinding.movieRatingTxt.setText(movieRating);
                    if (movieMPARating.equals("")) {
                        fragmentMovieDetailBinding.movieMpaRatingTxt.setText("N/A");
                    } else fragmentMovieDetailBinding.movieMpaRatingTxt.setText(movieMPARating);
                    JSONArray jsonArray = Utils.getJsonArray(new Gson().toJson(response.body().getData().movie.cast));
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = Utils.getJsonObject(jsonArray, i);
                            Cast cast = new Gson().fromJson(Objects.requireNonNull(obj).toString(), Cast.class);
                            castList.add(cast.getName());
                            fragmentMovieDetailBinding.castTxt.setText(castList.toString()
                                    .replace("[", "").replace("]", ""));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMovieDetailResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);

            }
        });
    }

    private void getMovieSuggestions(String movieId) {
        // fragmentMovieDetailBinding.moreMoviesTxt.setVisibility(View.GONE);
        movies = new ArrayList<>();
        movieIds = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        Call<GetMovieResponse> call = Retrofit.getService().getMovieSuggestions(movieId);
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                if (response.isSuccessful()) {
                    //  fragmentMovieDetailBinding.moreMoviesTxt.setVisibility(View.VISIBLE);
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
                            movieListAdapter = new MovieListAdapter(movies, MovieDetailFragment.this, getContext());
                            fragmentMovieDetailBinding.recyclerView.setLayoutManager(linearLayoutManager);
                            fragmentMovieDetailBinding.recyclerView.setAdapter(movieListAdapter);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.trailerBtn) {
            startActivity(new Intent(getContext(), YoutubeActivity.class).putExtra("trailerCode", ytTrailer));
        }
        else if(viewId==R.id.detailed_summaryTxt){
            AlertDialog.Builder a = new AlertDialog.Builder(getContext());
            a.setPositiveButton("Dismiss", null)
                    .setMessage(description_full)
                    .show();
        }

    }

    @Override
    public void onMovieItemClick(int position) {
        getMovieDetail(movieIds.get(position));
        getMovieSuggestions(movieIds.get(position));

    }
}