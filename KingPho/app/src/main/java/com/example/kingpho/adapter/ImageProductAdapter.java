package com.example.kingpho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kingpho.R;

import java.util.List;

public class ImageProductAdapter extends RecyclerView.Adapter<ImageProductAdapter.ImageHolder> {
    private final List<String> imgList;

    public ImageProductAdapter(List<String> imgList) {
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_product, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        String imgProduct = imgList.get(position);
//        if (imgProduct == null) {
//            return;
//        }
//        holder.image.setImageResource(imgProduct.getResouceId());

        if (imgProduct != null) {
            if (!imgProduct.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(imgProduct)
                        .into(holder.image);
            }
            else {
                holder.image.setImageResource(R.drawable.icon_pho);
            }
        }
        else {
            holder.image.setImageResource(R.drawable.icon_pho);
        }
    }

    @Override
    public int getItemCount() {
        if (imgList != null) {
            return imgList.size();
        }
        return 0;
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        // Khai báo các view trong item layout
        private final ImageView image;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
            image = itemView.findViewById(R.id.image);
        }
    }
}
