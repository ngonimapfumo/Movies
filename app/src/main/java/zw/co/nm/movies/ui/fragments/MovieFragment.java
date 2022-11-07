package zw.co.nm.movies.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMovieResponse;
import zw.co.nm.movies.databinding.FragmentMovieBinding;
import zw.co.nm.movies.models.Movie;
import zw.co.nm.movies.ui.adapters.MovieListAdapter;
import zw.co.nm.movies.utils.Utils;


public class MovieFragment extends Fragment implements MovieListAdapter.onMovieItemClick, SearchView.OnQueryTextListener {
    private FragmentMovieBinding fragmentMovieBinding;
    private MovieListAdapter movieListAdapter;
    private List<Movie> movies;
    private List<String> movieId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false);
        testOne("", 20);

        setUpSearch();
        return fragmentMovieBinding.getRoot();
    }

    private void setUpSearch() {
        fragmentMovieBinding.search.setQueryHint("Search Movies");
       fragmentMovieBinding.search.requestFocusFromTouch();
        fragmentMovieBinding.search.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMovieBinding = null;
    }

    public void testOne(String query, int limit) {
        fragmentMovieBinding.progBar.setVisibility(View.VISIBLE);
        movieId = new ArrayList<>();
        movies = new ArrayList<>();
        Call<GetMovieResponse> call = Retrofit.getService().getMovies(query, limit);
        call.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetMovieResponse> call, @NonNull Response<GetMovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().movie_count == 0) {
                        fragmentMovieBinding.progBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("No Movies found for this search, please try again");
                        builder.setPositiveButton("OKAY", (dialogInterface, i) -> {
                                    testOne("", 20);
                                })
                                .setCancelable(false)
                                .show();

                    } else {
                        fragmentMovieBinding.progBar.setVisibility(View.GONE);
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
                                    movieListAdapter = new MovieListAdapter(movies, MovieFragment.this, getContext());
                                    fragmentMovieBinding.movieRecycler.setHasFixedSize(true);
                                    fragmentMovieBinding.movieRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                    fragmentMovieBinding.movieRecycler.setAdapter(movieListAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                } else {
                    //todo: handle this
                    fragmentMovieBinding.progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error, Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onMovieItemClick(int position) {
        MovieFragmentDirections.ActionMovieFragmentToMovieDetailFragment actionMovieFragmentToMovieDetailFragment =
                MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId.get(position));
        Navigation.findNavController(requireView()).navigate(actionMovieFragmentToMovieDetailFragment);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        testOne(query, 50);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}