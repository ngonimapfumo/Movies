package zw.co.nm.movies.api;


import retrofit2.Call;
import retrofit2.http.GET;
import zw.co.nm.movies.api.responses.GetMoviesResponse;

public interface ApiService {

    @GET("list_movies.json")
    Call<GetMoviesResponse> getMovies();

}
