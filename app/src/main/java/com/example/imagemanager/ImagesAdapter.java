package com.example.imagemanager;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.DataViewHolder> {
    Activity acc;
    ArrayList<Image> listImages;
    public ImagesAdapter(Activity acc,ArrayList<Image> list){
        this.acc = acc;
        this.listImages = list;
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new DataViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.iv_image.setImageBitmap(BitmapFactory.decodeFile(listImages.get(position).getDataImage()));
    }
    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image=(ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
