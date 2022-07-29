package zw.co.nm.movies.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import zw.co.nm.movies.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private Bundle extras;
    private String imgUrl;
    private String backgroundImgUrl;
    private String summary;
    private String year;
    private String runtime;
    private String rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMovieDetailBinding.getRoot());
        extras = getIntent().getExtras();
        imgUrl = extras.getString("medium_cover_image");
        backgroundImgUrl = extras.getString("background_image_original");
        summary = extras.getString("summary");
        year = extras.getString("year");
        runtime = extras.getString("runtime");
        rating = extras.getString("rating");


        Picasso.get().load(imgUrl).into(activityMovieDetailBinding.imgv);
        activityMovieDetailBinding.summaryTxt.setText(summary);
        activityMovieDetailBinding.yearTxt.setText(year);
        activityMovieDetailBinding.ratingTxt.setText(rating);
        activityMovieDetailBinding.runtimeTxt.setText(String.format("%s minutes", runtime));


    }
}