package zw.co.nm.movies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MovieFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        view.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_movieFragment_to_movieDetailFragment);
        });
        return view;
    }
}