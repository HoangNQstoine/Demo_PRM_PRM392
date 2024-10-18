package com.example.kingpho.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.Comment;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private final List<Comment> commentList;
    private Context context;
    private int layout;
    public CommentAdapter(Context context, int layout, List<Comment> commentList) {
        this.context = context;
        this.layout = layout;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        ImageView imgReviewer = convertView.findViewById(R.id.imgReviewer);
        TextView nameReviewer = convertView.findViewById(R.id.nameReviewer);
        TextView emailReviewer = convertView.findViewById(R.id.emailReviewer);
        TextView numberRating = convertView.findViewById(R.id.numberRating);
        TextView contentReview = convertView.findViewById(R.id.contentReview);
        TextView foodOrder = convertView.findViewById(R.id.foodOrder);
        TextView timeOrder = convertView.findViewById(R.id.timeOrder);

        Comment comment = commentList.get(position);
        imgReviewer.setImageResource(comment.getImgReviewer());
        nameReviewer.setText(comment.getNameReviewer());
        emailReviewer.setText(comment.getEmailReviewer());
        numberRating.setText(String.format("%s", comment.getNumberRating()));
        contentReview.setText(comment.getContentReview());
        foodOrder.setText(comment.getFoodOrder());
        timeOrder.setText(comment.getTimeOrder());

        return convertView;
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//    Comment comment = commentList.get(position);
//    holder.imgReviewer.setImageResource(comment.getImgReviewer());
//    holder.nameReviewer.setText(comment.getNameReviewer());
//    holder.emailReviewer.setText(comment.getEmailReviewer());
//    holder.numberRating.setText(String.format("%s", comment.getNumberRating()));
//    holder.contentReview.setText(comment.getContentReview());
//    holder.foodOrder.setText(comment.getFoodOrder());
//    holder.timeOrder.setText(comment.getTimeOrder());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (commentList != null) {
//            return commentList.size();
//        }
//        return 0;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private final ImageView imgReviewer;
//        private final TextView nameReviewer;
//        private final TextView emailReviewer;
//        private final TextView numberRating;
//        private final TextView contentReview;
//        private final TextView foodOrder;
//        private final TextView timeOrder;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgReviewer = itemView.findViewById(R.id.imgReviewer);
//            nameReviewer = itemView.findViewById(R.id.nameReviewer);
//            emailReviewer = itemView.findViewById(R.id.emailReviewer);
//            numberRating = itemView.findViewById(R.id.numberRating);
//            contentReview = itemView.findViewById(R.id.contentReview);
//            foodOrder = itemView.findViewById(R.id.foodOrder);
//            timeOrder = itemView.findViewById(R.id.timeOrder);
//        }
//    }
}
