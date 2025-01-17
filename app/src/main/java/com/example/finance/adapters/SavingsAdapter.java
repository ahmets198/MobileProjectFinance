package com.example.finance.adapters; // Ahmet Sazan worked on this page

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.models.Saving;
import java.util.List;
import java.util.Locale;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.SavingViewHolder> {
    private List<Saving> savingsList;

    public SavingsAdapter(List<Saving> savingsList) {
        this.savingsList = savingsList;
    }

    @NonNull
    @Override
    public SavingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saving, parent, false);
        return new SavingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingViewHolder holder, int position) {
        Saving saving = savingsList.get(position);

        holder.textViewType.setText(saving.getType());
        holder.textViewAmount.setText(String.format(Locale.getDefault(),
                "$%.2f", saving.getAmount()));
        holder.textViewReturnRate.setText(String.format(Locale.getDefault(),
                "Return Rate: %.1f%%", saving.getReturnRate() * 100));

        // Show start date if available
        if (saving.getStartDate() != null && !saving.getStartDate().isEmpty()) {
            holder.textViewStartDate.setText(String.format("Started: %s",
                    saving.getStartDate()));
            holder.textViewStartDate.setVisibility(View.VISIBLE);
        } else {
            holder.textViewStartDate.setVisibility(View.GONE);
        }

        // Show target date and amount if available
        if (saving.getTargetDate() != null && !saving.getTargetDate().isEmpty()) {
            holder.textViewTargetDate.setText(String.format("Target: %s",
                    saving.getTargetDate()));
            holder.textViewTargetAmount.setText(String.format(Locale.getDefault(),
                    "Target Amount: $%.2f", saving.getTargetAmount()));
            holder.textViewTargetDate.setVisibility(View.VISIBLE);
            holder.textViewTargetAmount.setVisibility(View.VISIBLE);
        } else {
            holder.textViewTargetDate.setVisibility(View.GONE);
            holder.textViewTargetAmount.setVisibility(View.GONE);
        }

        // Calculate and show progress if target amount is set
        if (saving.getTargetAmount() > 0) {
            double progress = saving.getProgress();
            holder.textViewProgress.setText(String.format(Locale.getDefault(),
                    "Progress: %.1f%%", progress));
            holder.textViewProgress.setVisibility(View.VISIBLE);
        } else {
            holder.textViewProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return savingsList.size();
    }

    public void updateData(List<Saving> newSavings) {
        this.savingsList = newSavings;
        notifyDataSetChanged();
    }

    static class SavingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewAmount;
        TextView textViewReturnRate;
        TextView textViewStartDate;
        TextView textViewTargetDate;
        TextView textViewTargetAmount;
        TextView textViewProgress;

        SavingViewHolder(View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewReturnRate = itemView.findViewById(R.id.textViewReturnRate);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewTargetDate = itemView.findViewById(R.id.textViewTargetDate);
            textViewTargetAmount = itemView.findViewById(R.id.textViewTargetAmount);
            textViewProgress = itemView.findViewById(R.id.textViewProgress);
        }
    }
}