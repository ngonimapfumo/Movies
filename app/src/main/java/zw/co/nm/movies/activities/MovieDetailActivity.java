package zw.co.nm.movies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import zw.co.nm.movies.R;
import zw.co.nm.movies.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private Bundle extras;
    private String imgUrl;
    private String backgroundImgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
        extras = getIntent().getExtras();
        imgUrl = extras.getString("medium_cover_image");
        backgroundImgUrl = extras.getString("background_image_original");

       // Toast.makeText(this, extras.getString("medium_cover_image"), Toast.LENGTH_SHORT).show();
      //  Picasso.get().load(imgUrl).placeholder(R.drawable.default_loading_image).into(activityMovieDetailBinding.imgv);
      //  Picasso.get().load(backgroundImgUrl).placeholder(R.drawable.sample_background).into(activityMovieDetailBinding.backgroundImg);

    }
}