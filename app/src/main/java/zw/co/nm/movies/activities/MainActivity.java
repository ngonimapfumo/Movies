package zw.co.nm.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

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
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMovieResponse;
import zw.co.nm.movies.databinding.ActivityMainBinding;
import zw.co.nm.movies.models.Movie;
import zw.co.nm.movies.ui.adapters.MovieListAdapter;
import zw.co.nm.movies.utils.Utils;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.onMovieItemClick {

    private MovieListAdapter movieListAdapter;
    private List<Movie> movies;
    private ActivityMainBinding activityMainBinding;
    private List<String> movieSummary;
    private List<String> mediumCoverImage;
    private List<String> backgroundImageOriginal;
    private List<String> year;
    private List<String> runtime;
    private List<String> rating;
    private List<String> titles;
    private List<String> ytTrailerCodes;
    private List<String> genres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        testOne("", 20);

    }

    private void testOne(String query, int limit) {
        activityMainBinding.progBar.setVisibility(View.VISIBLE);
        movies = new ArrayList<>();
        movieSummary = new ArrayList<>();
        mediumCoverImage = new ArrayList<>();
        backgroundImageOriginal = new ArrayList<>();
        runtime = new ArrayList<>();
        year = new ArrayList<>();
        rating = new ArrayList<>();
        titles = new ArrayList<>();
        ytTrailerCodes = new ArrayList<>();
        genres = new ArrayList<>();
        Call<GetMovieResponse> call = Retrofit.getService().getMovies(query, limit);
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetMovieResponse> call, @NonNull Response<GetMovieResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getData().movie_count == 0) {
                        activityMainBinding.progBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("No Movies found for this search, please try again");
                        builder.setPositiveButton("OKAY", (dialogInterface, i) -> {
                                    testOne("", 20);
                                })
                                .setCancelable(false)
                                .show();

                    } else {
                        activityMainBinding.progBar.setVisibility(View.GONE);
                        String resString = null;
                        if (response.body() != null) {
                            resString = new Gson().toJson(response.body().getData().movies);
                        }
                        JSONArray jsonArray = Utils.getJsonArray(resString);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = Utils.getJsonObject(jsonArray, i);
                                Movie movie = new Gson().fromJson(Objects.requireNonNull(obj).toString(), Movie.class);
                                try {
                                    ytTrailerCodes.add(obj.getString("yt_trailer_code"));
                                    movieSummary.add(obj.getString("summary"));
                                    mediumCoverImage.add(obj.getString("medium_cover_image"));
                                    backgroundImageOriginal.add(obj.getString("background_image"));
                                    year.add(obj.getString("year"));
                                    runtime.add(obj.getString("runtime"));
                                    rating.add(obj.getString("rating"));
                                    titles.add(obj.getString("title"));
                                    genres.add(obj.getString("genres"));
                                    movies.add(movie);
                                    movieListAdapter = new MovieListAdapter(movies, MainActivity.this, MainActivity.this);
                                    activityMainBinding.movieRecycler.setHasFixedSize(true);
                                    activityMainBinding.movieRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                                    activityMainBinding.movieRecycler.setAdapter(movieListAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                } else {
                    //todo: handle this
                    activityMainBinding.progBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Error, Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                testOne(s, 50);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public void onMovieItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("summary", movieSummary.get(position));
        bundle.putSerializable("medium_cover_image", mediumCoverImage.get(position));
        bundle.putSerializable("background_image_original", backgroundImageOriginal.get(position));
        bundle.putSerializable("runtime", runtime.get(position));
        bundle.putSerializable("year", year.get(position));
        bundle.putSerializable("rating", rating.get(position));
        bundle.putSerializable("title", titles.get(position));
        bundle.putSerializable("yt_trailer_code", ytTrailerCodes.get(position));
        bundle.putSerializable("genres", genres.get(position));
        startActivity(new Intent(this, MovieDetailActivity.class).putExtras(bundle));

    }
}