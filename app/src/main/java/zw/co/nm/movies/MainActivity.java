package zw.co.nm.movies;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        testOne();

    }

    private void testOne() {
        activityMainBinding.progBar.setVisibility(View.VISIBLE);
        movies = new ArrayList<>();
        Call<GetMovieResponse> call = Retrofit.getService().getMovies();
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                activityMainBinding.progBar.setVisibility(View.GONE);
                String resString = new Gson().toJson(response.body().getData().movies);
                JSONArray jsonArray = Utils.getJsonArray(resString);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = Utils.getJsonObject(jsonArray, i);
                         Movie movie = new Gson().fromJson(obj.toString(), Movie.class);
                        movies.add(movie);
                        movieListAdapter = new MovieListAdapter(movies, MainActivity.this, MainActivity.this);
                        activityMainBinding.movieRecycler.setHasFixedSize(true);
                        activityMainBinding.movieRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        activityMainBinding.movieRecycler.setAdapter(movieListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void onMovieItemClick(int position) {

    }
}