package com.example.mobile_app.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.Model.Order;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<Product> productList;
    private OnCancelOrderListener onCancelOrderListener;

    public OrderDetailAdapter(List<Product> productList, OnCancelOrderListener onCancelOrderListener) {
        this.productList = productList;
        this.onCancelOrderListener = onCancelOrderListener;
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view, onCancelOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textProductName.setText("Tên: " + product.getName());
        Picasso.get().load(product.getImg_url()).into(holder.imageProduct);
        holder.textProductPrice.setText(String.format(Locale.getDefault(), "Giá :%,d đ", product.getPrice()));
        holder.textProductAmount.setText(String.format(Locale.getDefault(), "Số lượng: %d", product.getQuatity()));
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public List<Product> getproductList() {
        return productList;
    }

    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName;
        TextView textProductPrice;
        TextView textProductAmount;

        Button huydon;

        public OrderDetailViewHolder(@NonNull View itemView, OnCancelOrderListener onCancelOrderListener) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.image_product);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
            textProductAmount = itemView.findViewById(R.id.text_product_storage);


        }
    }

    // Interface để định nghĩa sự kiện hủy đơn
    public interface OnCancelOrderListener {
        void onCancelOrder(int position);
    }
}
