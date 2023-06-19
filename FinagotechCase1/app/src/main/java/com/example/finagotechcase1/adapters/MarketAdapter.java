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
import com.example.finagotechcase1.models.Market;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private List<Market> marketList;
    private Context context;

    public MarketAdapter(Context context, List<Market> marketList) {
        this.context = context;
        this.marketList = marketList;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        Market market = marketList.get(position);
        holder.baseAssetIdTextView.setText("AssetId: "+market.getBaseAssetId());
        holder.idTextView.setText("id: "+market.getId());
        holder.quoteIdTextView.setText("quoteId: "+market.getQuoteId());
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public class MarketViewHolder extends RecyclerView.ViewHolder {
        TextView baseAssetIdTextView, idTextView, quoteIdTextView;
        CardView cardView;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            baseAssetIdTextView = itemView.findViewById(R.id.baseAssetIdTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            quoteIdTextView = itemView.findViewById(R.id.quoteIdTextView);
            cardView = itemView.findViewById(R.id.marketCardView);
        }
    }
}

