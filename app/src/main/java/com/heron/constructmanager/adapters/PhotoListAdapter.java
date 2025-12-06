package com.heron.constructmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.views.PhotoViewActivity;
import com.heron.constructmanager.models.Photo;

import java.util.ArrayList;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {

    private final Context context;
    private final ArrayList<Photo> list;

    private final String constructionUid;

    public PhotoListAdapter(Context context, ArrayList<Photo> list, String constructionUid) {
        this.context = context;
        this.list = list;
        this.constructionUid = constructionUid; // зберігаємо для чогось
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo p = list.get(position);

        Glide.with(context)
                .load(p.getUrl())                   // тепер працює
                .placeholder(R.drawable.loading)
                .into(holder.image);

        holder.desc.setText(p.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoViewActivity.class);
            intent.putExtra("url", p.getUrl());
            intent.putExtra("desc", p.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView desc;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_photo_img);
            desc = itemView.findViewById(R.id.item_photo_desc);
        }
    }
}
