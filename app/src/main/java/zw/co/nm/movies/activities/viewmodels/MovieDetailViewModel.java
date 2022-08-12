package zw.co.nm.movies.activities.viewmodels;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MovieDetailViewModel extends AndroidViewModel {
    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public Bundle extras;
    public String imgUrl;
    public String backgroundImgUrl;
    public String summary;
    public String year;
    public String runtime;
    public String rating;
    public String title;
    public String trailerCode;

}
