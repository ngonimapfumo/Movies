package zw.co.nm.movies.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zw.co.nm.movies.R;
import zw.co.nm.movies.databinding.ItemMovieDetailBinding;
import zw.co.nm.movies.models.Movie;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<Movie> localDataSet;
    private ItemMovieDetailBinding itemMovieDetailBinding;
    private onMovieItemClick onMovieItemClick;
    private Context context;

    public MovieListAdapter(List<Movie> localDataSet, MovieListAdapter.onMovieItemClick onMovieItemClick, Context context) {
        this.localDataSet = localDataSet;
        this.onMovieItemClick = onMovieItemClick;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemMovieDetailBinding = ItemMovieDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieListAdapter.ViewHolder(itemMovieDetailBinding, onMovieItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
        if(localDataSet.get(position).getLarge_cover_image()==null){
            Picasso.get().load(localDataSet.get(position).getMedium_cover_image()).into(holder.itemMovieDetailBinding.imageView);
        }
       else Picasso.get().load(localDataSet.get(position).getLarge_cover_image()).placeholder(R.drawable.sample_cover_large)
                .error(R.drawable.sample_cover_large).into(holder.itemMovieDetailBinding.imageView);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemMovieDetailBinding itemMovieDetailBinding;
        private onMovieItemClick onMovieItemClick;

        public ViewHolder(ItemMovieDetailBinding itemMovieDetailBinding, MovieListAdapter.onMovieItemClick onMovieItemClick) {
            super(itemMovieDetailBinding.getRoot());
            this.itemMovieDetailBinding = itemMovieDetailBinding;
            this.onMovieItemClick = onMovieItemClick;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onMovieItemClick.onMovieItemClick(getAdapterPosition());
        }
    }

    public interface onMovieItemClick {
        void onMovieItemClick(int position);
    }
}
