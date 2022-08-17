package zw.co.nm.movies.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zw.co.nm.movies.api.responses.GetMovieResponse;

public interface ApiService {

    @GET("list_movies.json")
    Call<GetMovieResponse> getMovies();

    @GET("list_movies.json")
    Call<GetMovieResponse> getMovies(@Query("query_term") String query, @Query("limit")int limit);

}
