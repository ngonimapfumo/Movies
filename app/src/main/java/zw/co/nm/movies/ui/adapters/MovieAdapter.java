package zw.co.nm.movies.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import zw.co.nm.movies.databinding.ItemMovieDetailBinding;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ItemMovieDetailBinding itemMovieDetailBinding;
    private onMovieItemClick onMovieItemClick;


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemMovieDetailBinding = ItemMovieDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieAdapter.ViewHolder(itemMovieDetailBinding, onMovieItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemMovieDetailBinding itemMovieDetailBinding;
        private onMovieItemClick onMovieItemClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        public ViewHolder(ItemMovieDetailBinding itemMovieDetailBinding, MovieAdapter.onMovieItemClick onMovieItemClick) {
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
