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

import com.example.mobile_app.Model.Product;
import com.example.mobile_app.Home.ProductDetailActivity;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    Context mcontext;
    ProductCallBack productCallBack;
    private ArrayList<Product> originalList; // Danh sách ban đầu

    public ProductAdapter(Context context, ArrayList<Product> lstPro) {
        this.mcontext = context;
        this.products = lstPro;
        this.originalList = new ArrayList<>(lstPro); // Lưu trữ danh sách ban đầu
    }
    public void setMainCallBack(ProductCallBack productCallBack) {
        this.productCallBack = productCallBack;
    }
    public ProductAdapter() {
        this.products = new ArrayList<>();
        this.originalList = new ArrayList<>();
    }
    public void setProducts(List<Product> products) {
        this.products = products;
        this.originalList = new ArrayList<>(products);
    }

    public void search(String keyword) {
        products.clear();
        if (originalList != null) {
            if (keyword.isEmpty()) {
                products.addAll(originalList);
            } else {
                for (Product product : originalList) {
                    if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        products.add(product);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    // Reset danh sách về ban đầu
    public void reset() {
        products.clear();
        products.addAll(originalList);
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
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceTextView.setText("Giá: " + decimalFormat.format(product.getPrice())+"Đ");
        Picasso.get()
                .setLoggingEnabled(true);
        Picasso.get()
                .load(product.getImg_url())
                .placeholder(R.drawable.img_1) // ảnh placeholder khi đang tải
                .error(R.drawable.img_1) // ảnh sẽ hiển thị khi có lỗi
                .into(holder.imageView);
        //ctsp
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.getId()); // Assume your product model has a getId() method
                v.getContext().startActivity(intent);
            }
        });
        //ctsp
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface ProductCallBack{
        void onItemClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
}
