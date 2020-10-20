package com.dpridoy.tvseries.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dpridoy.tvseries.R;
import com.dpridoy.tvseries.databinding.ItemContainerSliderImageBinding;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private List<String> sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(List<String> sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderImageBinding sliderImageBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_image,parent,false
        );
        return new ImageSliderViewHolder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderImages.size();
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerSliderImageBinding itemContainerSliderImageBinding;


        public ImageSliderViewHolder(@NonNull ItemContainerSliderImageBinding itemView) {
            super(itemView.getRoot());
            this.itemContainerSliderImageBinding=itemView;
        }

        public void bindSliderImage(String imageURL){
            itemContainerSliderImageBinding.setImageURL(imageURL);
        }
    }
}
