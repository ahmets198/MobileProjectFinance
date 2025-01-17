package com.example.finance.adapters; // Murat Görkem Kahya worked on this page

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.models.Asset;
import java.util.List;
import java.util.Locale;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {
    private final List<Asset> assetList;

    public AssetAdapter(List<Asset> assetList) {
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asset, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        Asset asset = assetList.get(position);
        holder.textType.setText(asset.getType());

        // Format amount based on asset type
        if ("Cash".equals(asset.getType())) {
            holder.textAmount.setText(String.format(Locale.getDefault(), "$%.2f", asset.getAmount()));
        } else {
            holder.textAmount.setText(String.format(Locale.getDefault(), "%.2f", asset.getAmount()));
        }

        holder.textWorth.setText(String.format(Locale.getDefault(), "$%.2f", asset.getWorth()));
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    static class AssetViewHolder extends RecyclerView.ViewHolder {
        final TextView textType;
        final TextView textAmount;
        final TextView textWorth;

        AssetViewHolder(View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textType);
            textAmount = itemView.findViewById(R.id.textAmount);
            textWorth = itemView.findViewById(R.id.textWorth);
        }
    }
}