package com.example.mobile_app.Adapter;

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
import com.example.mobile_app.Home.ProductDetailActivity;
import com.example.mobile_app.Model.Apple;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AppleAdapter extends RecyclerView.Adapter<AppleAdapter.ViewHolder> {
    private List<Apple> apples;
    Context mcontext;
    ProductCallBack productCallBack;
    private ArrayList<Apple> originalList; // Danh sách ban đầu

    public AppleAdapter(Context context, ArrayList<Apple> lstPro) {
        this.mcontext = context;
        this.apples= lstPro;
        this.originalList = new ArrayList<>(lstPro); // Lưu trữ danh sách ban đầu
    }
    public void setMainCallBack(ProductCallBack productCallBack) {
        this.productCallBack = productCallBack;
    }
    public AppleAdapter() {
        this.apples = new ArrayList<>();
        this.originalList = new ArrayList<>();
    }
    public void setProducts(List<Apple> apples) {
        this.apples = apples;
        this.originalList = new ArrayList<>(apples);
    }

    public void search(String keyword) {
        apples.clear();
        if (originalList != null) {
            if (keyword.isEmpty()) {
                apples.addAll(originalList);
            } else {
                for (Apple apple : originalList) {
                    if (apple.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        apples.add(apple);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    // Reset danh sách về ban đầu
    public void reset() {
        apples.clear();
        apples.addAll(originalList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_phone);
            priceTextView = itemView.findViewById(R.id.tv_Price);
            imageView = itemView.findViewById(R.id.img_phone);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutitem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apple product = apples.get(position);
        holder.nameTextView.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceTextView.setText("Giá: " + decimalFormat.format(product.getPrice())+"đ");
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(product.getImg_url()).into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.getId()); // Assume your product model has a getId() method
                v.getContext().startActivity(intent);
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return apples.size();
    }

    public interface ProductCallBack{
        void onItemClick(int id);
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private AppleAdapter.OnItemClickListener listener;

}
