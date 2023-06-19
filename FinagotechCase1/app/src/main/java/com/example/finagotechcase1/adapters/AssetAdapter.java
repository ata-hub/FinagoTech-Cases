package com.example.finagotechcase1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finagotechcase1.R;
import com.example.finagotechcase1.models.Asset;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Asset asset);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    private List<Asset> assetList;
    private Context context;

    public AssetAdapter(Context context, List<Asset> assetList) {
        this.context = context;
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        Asset asset = assetList.get(position);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(asset);
            }
        });
        holder.nameTextView.setText(asset.getName());
        holder.statusTextView.setText(asset.getStatus());
        holder.symbolTextView.setText(asset.getSymbol());
        holder.typeTextView.setText(asset.getType());

    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public class AssetViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView, symbolTextView, typeTextView;
        CardView cardView;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            symbolTextView = itemView.findViewById(R.id.symbolTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            cardView = itemView.findViewById(R.id.assetCardView);
        }
    }
}

