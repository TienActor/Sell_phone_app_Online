package com.example.mobile_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.Model.Product;
import com.example.mobile_app.Model.QLDH;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QldhAdapter extends RecyclerView.Adapter<QldhAdapter.ViewHolder> {

    /// code cua tien

    private List<QLDH> Orders;
    Context mcontext;
    QldhAdapter.OrderCallBack orderCallBack;
    private ArrayList<QLDH> originalList; // Danh sách ban đầu


    public QldhAdapter(Context context, ArrayList<QLDH> lstPro) {
        this.mcontext = context;
        this.Orders = lstPro;
        this.originalList = new ArrayList<>(lstPro); // Lưu trữ danh sách ban đầu
    }

    public QldhAdapter(OrderCallBack productCallBack) {
        this.orderCallBack = productCallBack;
    }

    public QldhAdapter() {
        this.Orders = new ArrayList<>();
        this.originalList = new ArrayList<>();
    }

    public void setQLDH(List<QLDH> Orders) {
        this.Orders = Orders;
        this.originalList = new ArrayList<>(Orders);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_donhang, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QLDH qldh = Orders.get(position);
        holder.iddonhang.setText("ID: "+qldh.getId());
        holder.trangthai.setText("Trạng thái:"+qldh.getStatus());
        holder.ngaydat.setText("Ngày đặt:"+qldh.getDate());
        holder.ngaygiao.setText("Ngày giao"+qldh.getDate_ship());

//        Picasso.get()
//                .setLoggingEnabled(true);
//        Picasso.get()
//                .load(product.getImg_url())
//                .placeholder(R.drawable.img_1) // ảnh placeholder khi đang tải
//                .error(R.drawable.img_1) // ảnh sẽ hiển thị khi có lỗi
//                .into(holder.imageView);

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
    public class OrderCallBack {
    }
}
