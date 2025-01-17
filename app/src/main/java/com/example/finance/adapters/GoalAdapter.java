package com.example.finance.adapters;// Ahmet Sazan worked on this page

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.models.Goal;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private final List<Goal> goalList;

    public GoalAdapter(List<Goal> goalList) {
        this.goalList = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.textDescription.setText(goal.getDescription());

        double progress = (goal.getCurrentAmount() / goal.getTargetAmount()) * 100;
        holder.progressGoal.setProgress((int) progress);

        holder.textCurrentAmount.setText(String.format("$%.2f", goal.getCurrentAmount()));
        holder.textTargetAmount.setText(String.format("$%.2f", goal.getTargetAmount()));
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView textDescription, textCurrentAmount, textTargetAmount;
        ProgressBar progressGoal;

        GoalViewHolder(View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textCurrentAmount = itemView.findViewById(R.id.textCurrentAmount);
            textTargetAmount = itemView.findViewById(R.id.textTargetAmount);
            progressGoal = itemView.findViewById(R.id.progressGoal);
        }
    }
}