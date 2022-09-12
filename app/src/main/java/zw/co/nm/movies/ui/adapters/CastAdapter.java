package zw.co.nm.movies.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zw.co.nm.movies.databinding.ItemCastDesignBinding;
import zw.co.nm.movies.models.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    private List<Cast> localDataSet;
    private ItemCastDesignBinding itemCastDesignBinding;
    private onCastItemClick onCastItemClick;
    private Context context;

    public CastAdapter(List<Cast> localDataSet, CastAdapter.onCastItemClick onCastItemClick, Context context) {
        this.localDataSet = localDataSet;
        this.onCastItemClick = onCastItemClick;
        this.context = context;

    }


    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemCastDesignBinding = ItemCastDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CastAdapter.ViewHolder(itemCastDesignBinding, onCastItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.ViewHolder holder, int position) {
        holder.itemCastDesignBinding.characterNameTxt.setText(localDataSet.get(position).getName());
        Picasso.get().load(localDataSet.get(position).getUrl_small_image()).into(holder.itemCastDesignBinding.characterImg);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemCastDesignBinding itemCastDesignBinding;
        private CastAdapter.onCastItemClick onCastItemClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewHolder(ItemCastDesignBinding itemCastDesignBinding, CastAdapter.onCastItemClick onCastItemClick) {
            super(itemCastDesignBinding.getRoot());
            this.itemCastDesignBinding = itemCastDesignBinding;
            this.onCastItemClick = onCastItemClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface onCastItemClick {
        void onCastItemClick(int position);
    }
}
