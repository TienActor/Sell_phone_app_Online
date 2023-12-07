package com.example.mobile_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.Model.OrderHistory;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    /// code cua tien

    private List<OrderHistory> Orders;
    Context mcontext;
    OrderAdapter.OrderCallBack orderCallBack;
    private ArrayList<OrderHistory> originalList; // Danh sách ban đầu


    public OrderAdapter(Context context, ArrayList<OrderHistory> lstPro) {
        this.mcontext = context;
        this.Orders = lstPro;
        this.originalList = new ArrayList<>(lstPro); // Lưu trữ danh sách ban đầu
    }

    public OrderAdapter(OrderCallBack productCallBack) {
        this.orderCallBack = productCallBack;
    }

    public OrderAdapter() {
        this.Orders = new ArrayList<>();
        this.originalList = new ArrayList<>();
    }

    public void setOrders(List<OrderHistory> Orders) {
        this.Orders = Orders;
        this.originalList = new ArrayList<>(Orders);
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_donhang, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderHistory orderHistory = Orders.get(position);
        holder.iddonhang.setText("ID: "+orderHistory.getId());
        holder.trangthai.setText("Trạng thái:"+orderHistory.getStatus());
        holder.ngaydat.setText(String.format("Ngày đặt: %s", formatDate(orderHistory.getDate())));
        String deliveryDate = orderHistory.getDate_ship() != null ? formatDate(orderHistory.getDate_ship()) : "Chưa có";
        holder.ngaygiao.setText(String.format("Ngày giao: %s", deliveryDate));
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView iddonhang,trangthai,ngaydat,ngaygiao;
        public ViewHolder(View itemView) {
            super(itemView);
            iddonhang=itemView.findViewById(R.id.iddonhang);
            trangthai=itemView.findViewById(R.id.tranngthai);
            ngaydat=itemView.findViewById(R.id.ngaydat);
            ngaygiao=itemView.findViewById(R.id.ngaygiao);


        }
    }


    private String formatDate(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(dateString);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Return the original date string if parsing fails
            return dateString;
        }
    }
    public class OrderCallBack {
    }
}
