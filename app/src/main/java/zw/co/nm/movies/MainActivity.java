package zw.co.nm.movies;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zw.co.nm.movies.api.Retrofit;
import zw.co.nm.movies.api.responses.GetMoviesResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testOne();

    }

    private void testOne() {
        Call<GetMoviesResponse> call = Retrofit.getService().getMovies();
        call.enqueue(new Callback<GetMoviesResponse>() {
            @Override
            public void onResponse(Call<GetMoviesResponse> call, Response<GetMoviesResponse> response) {
                /*Toast.makeText(MainActivity.this, response.body().getData().movies.get(0).description_full+"", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onFailure(Call<GetMoviesResponse> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());

            }
        });
    }
}