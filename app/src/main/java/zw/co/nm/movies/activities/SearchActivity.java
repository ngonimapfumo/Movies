package zw.co.nm.movies.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import zw.co.nm.movies.ui.fragments.MovieDetailFragment;
import zw.co.nm.movies.utils.NetworkChangeListener;
import zw.co.nm.movies.utils.Utils;

public class SearchActivity extends AppCompatActivity implements MovieListAdapter.onMovieItemClick {

    private MovieListAdapter movieListAdapter;
    private List<Movie> movies;
    private ActivityMainBinding activityMainBinding;
    private List<String> movieId;
    private NetworkChangeListener networkChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        networkChangeListener = new NetworkChangeListener();
       // testOne("", 20);
        getSupportActionBar().setTitle("Search");
    }

    private void testOne(String query, int limit) {
        activityMainBinding.progBar.setVisibility(View.VISIBLE);
        movieId = new ArrayList<>();
        movies = new ArrayList<>();
        Call<GetMovieResponse> call = Retrofit.getService().getMovies(query, limit);
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetMovieResponse> call, @NonNull Response<GetMovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().movie_count == 0) {
                        activityMainBinding.progBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
                                    movieId.add(obj.getString("id"));
                                    movies.add(movie);
                                    movieListAdapter = new MovieListAdapter(movies, SearchActivity.this, SearchActivity.this);
                                    activityMainBinding.movieRecycler.setHasFixedSize(true);
                                    activityMainBinding.movieRecycler.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
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
                    Toast.makeText(SearchActivity.this, "Error, Please try again later", Toast.LENGTH_SHORT).show();
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
        searchView.setQueryHint("Search Movies");
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
        handleFragment(new MovieDetailFragment(), movieId.get(position));

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

    public void handleFragment(Fragment fragment, String id) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromActivity",true);
        bundle.putString("movieId", id);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
