////package com.example.kingpho.adapter;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.AdapterView;
////import android.widget.ImageView;
////import android.widget.TextView;
////import androidx.annotation.NonNull;
////import androidx.constraintlayout.widget.ConstraintLayout;
////import androidx.core.content.ContextCompat;
////import androidx.recyclerview.widget.RecyclerView;
////import com.bumptech.glide.Glide;
////import com.example.kingpho.R;
////import com.example.kingpho.model.Category;
////
////import java.util.ArrayList;
////
////public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
////    ArrayList<Category> categoryList;
////    private OnCategoryClickListener listener;
////
////
////    public interface OnCategoryClickListener {
////        void onCategoryClick(Category category);
////    }
////
////    public CategoryAdapter(ArrayList<Category> categoryList, OnCategoryClickListener listener) {
////        this.categoryList = categoryList;
////        this.listener = listener;
////    }
////
////    @NonNull
////    @Override
////    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
////        return new ViewHolder(inflate);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
////        Category category = categoryList.get(position);
////        holder.categoryName.setText(categoryList.get(position).getCategoryTitle());
////        String picUrl = categoryList.get(position).getCategoryImage();
////
////        holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
////
////        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
////
////        Glide.with(holder.itemView.getContext())
////                .load(drawableResourceId)
////                .into(holder.categoryPic);
////
////        holder.mainLayout.setOnClickListener(v -> listener.onCategoryClick(category));
////    }
////
////    @Override
////    public int getItemCount() {
////        return categoryList.size();
////    }
////
////    public class ViewHolder extends RecyclerView.ViewHolder {
////        TextView categoryName;
////        ImageView categoryPic;
////        ConstraintLayout mainLayout;
////
////        public ViewHolder(@NonNull View itemView) {
////            super(itemView);
////            categoryName = itemView.findViewById(R.id.categoryName);
////            categoryPic = itemView.findViewById(R.id.categoryPic);
////            mainLayout = itemView.findViewById(R.id.cardView);
////        }
////    }
////}
////
////
////
////
////
////
//////package com.example.kingpho.adapter;
//////
//////import android.content.Context;
//////import android.view.LayoutInflater;
//////import android.view.View;
//////import android.view.ViewGroup;
//////import android.widget.ImageView;
//////import android.widget.TextView;
//////
//////import androidx.annotation.NonNull;
//////import androidx.recyclerview.widget.RecyclerView;
//////
//////import com.bumptech.glide.Glide;
//////import com.example.kingpho.R;
//////import com.example.kingpho.model.Category;
//////
//////import java.util.List;
//////
//////public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
//////
//////    private List<Category> categoryList;
//////    private OnCategoryClickListener listener;
//////    private Context context;
//////
//////    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
//////        this.context = context;
//////        this.categoryList = categoryList;
//////        this.listener = listener;
//////    }
//////
//////    @NonNull
//////    @Override
//////    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//////        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
//////        return new CategoryViewHolder(itemView);
//////    }
//////
//////    @Override
//////    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
//////        Category category = categoryList.get(position);
//////        holder.bind(category);
//////    }
//////
//////    @Override
//////    public int getItemCount() {
//////        return categoryList.size();
//////    }
//////
//////    public void updateData(List<Category> categoryList) {
//////        this.categoryList = categoryList;
//////        notifyDataSetChanged();
//////    }
//////
//////    public class CategoryViewHolder extends RecyclerView.ViewHolder {
//////
//////        private TextView textViewCategoryTitle;
//////        private ImageView imageViewCategoryImage;
//////
//////        public CategoryViewHolder(View itemView) {
//////            super(itemView);
//////            textViewCategoryTitle = itemView.findViewById(R.id.categoryName);
//////            imageViewCategoryImage = itemView.findViewById(R.id.categoryPic);
//////
//////            itemView.setOnClickListener(new View.OnClickListener() {
//////                @Override
//////                public void onClick(View v) {
//////                    int position = getAdapterPosition();
//////                    if (position != RecyclerView.NO_POSITION && listener != null) {
//////                        listener.onCategoryClick(categoryList.get(position));
//////                    }
//////                }
//////            });
//////        }
//////
//////        public void bind(Category category) {
//////            textViewCategoryTitle.setText(category.getCategoryTitle());
//////            // Load category image using Glide or Picasso for smooth image loading
//////            // Example with Glide:
//////             Glide.with(context).load(category.getCategoryImage()).into(imageViewCategoryImage);
//////        }
//////    }
//////
//////    public interface OnCategoryClickListener {
//////        void onCategoryClick(Category category);
//////    }
//////}
//
//
//
//
//
package com.example.kingpho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kingpho.R;
import com.example.kingpho.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> categoryList;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(ArrayList<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        if (category.getImage() != null) {
            if (!category.getImage().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(category.getImage())
                        .into(holder.categoryPic);
            }
        }


        if (position == selectedPosition) {
            holder.mainLayout.setBackgroundResource(R.drawable.bg_change);
        } else {
            holder.mainLayout.setBackgroundResource(R.drawable.bg_default);
        }
        holder.bind(category, listener, position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
            mainLayout = itemView.findViewById(R.id.cardView);
        }

        void bind(Category category, OnCategoryClickListener listener, int position) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}


