package com.example.kingpho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.TrackingStep;

import java.util.List;

public class TrackingStepsAdapter extends RecyclerView.Adapter<TrackingStepsAdapter.TrackingStepViewHolder> {

    private List<TrackingStep> trackingSteps;

    public TrackingStepsAdapter(List<TrackingStep> trackingSteps) {
        this.trackingSteps = trackingSteps;
    }

    @NonNull
    @Override
    public TrackingStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracking_step, parent, false);
        return new TrackingStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingStepViewHolder holder, int position) {
        TrackingStep trackingStep = trackingSteps.get(position);
        holder.stepTitle.setText(trackingStep.getTitle());
        holder.stepSubtitle.setText(trackingStep.getSubtitle());
        holder.stepIcon.setImageResource(trackingStep.getIconResource());

        int titleColor;
        int dotDrawable;

        switch (trackingStep.getStatus()) {
            case "completed":
                titleColor = holder.itemView.getContext().getResources().getColor(R.color.completed_color);
                dotDrawable = R.drawable.dot_completed_orange;
                break;
            case "ongoing":
                titleColor = holder.itemView.getContext().getResources().getColor(R.color.ongoing_color);
                dotDrawable = R.drawable.dot_ongoing_orange;
                break;
            default:
                titleColor = holder.itemView.getContext().getResources().getColor(R.color.pending_color);
                dotDrawable = R.drawable.dot_pending;
                break;
        }

        holder.stepTitle.setTextColor(titleColor);
        holder.statusDot.setBackgroundResource(dotDrawable);
    }


    @Override
    public int getItemCount() {
        return trackingSteps.size();
    }

    public static class TrackingStepViewHolder extends RecyclerView.ViewHolder {
        TextView stepTitle, stepSubtitle;
        ImageView stepIcon;
        View statusDot;

        public TrackingStepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepTitle = itemView.findViewById(R.id.step_title);
            stepSubtitle = itemView.findViewById(R.id.step_subtitle);
            stepIcon = itemView.findViewById(R.id.step_icon);
            statusDot = itemView.findViewById(R.id.status_dot);
        }
    }
}
