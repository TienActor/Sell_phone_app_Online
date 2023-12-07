package com.example.mobile_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mobile_app.R;

import java.util.Arrays;
import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private Context context;
    List<Integer> listPhoto;

    public BannerAdapter(Context context, List<Integer> listPhoto) {
        this.context = context;
        this.listPhoto = listPhoto;
    }

    @Override
    public int getCount() {

        if(listPhoto != null) {
            return listPhoto.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, container, false);
        ImageView imgPhoto = view.findViewById(R.id.imgPhoto);
        imgPhoto.setImageResource(listPhoto.get(position));
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}


