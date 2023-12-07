package com.example.mobile_app.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.GioHang.GioHangTest;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DatHangAdapter extends RecyclerView.Adapter<DatHangAdapter.DatHangViewHolder>{
    ArrayList<Product> lstgio;
    Context context;
    DatHangCallBack proCallBack;
    public void updateData(ArrayList<Product> lstgio) {
        this.lstgio = lstgio;
        notifyDataSetChanged();
    }
    public void setProCallBack(DatHangCallBack proCallBack) {
        this.proCallBack = proCallBack;
    }
    public DatHangAdapter(ArrayList<Product> lstgio) {
        this.lstgio = lstgio;
    }
    public DatHangAdapter(Context context, ArrayList<Product> lstPro, DatHangCallBack callBack) {
        this.context = context;
        this.lstgio= lstPro;
        this.proCallBack = callBack;
    }


    @NonNull
    @Override
    public DatHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //nạp layout cho View biểu diễn phần tử user
        View proView= inflater.inflate(R.layout.thanh_toan_item, parent, false);
        //
        DatHangViewHolder productViewHolder = new DatHangViewHolder(proView);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DatHangViewHolder holder, int position) {
        Product item = lstgio.get(position);
        //Gán vào item của View
        Picasso.get()
                .setLoggingEnabled(true);
        Picasso.get()
                .load(item.getImg_url())
                .placeholder(R.drawable.img_1) // ảnh placeholder khi đang tải
                .error(R.drawable.img_1) // ảnh sẽ hiển thị khi có lỗi
                .into(holder.imPro);
        holder.proName.setText("Tên: "+item.getName());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.proPrice.setText("Giá: "+decimalFormat.format(item.getPrice())+" Đ");
        holder.proQuantity.setText("Số lượng: "+item.getQuatity());
        //Lay su kien

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (proCallBack != null) {
                    proCallBack.onItemLongClicked(item);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return lstgio.size();
    }

    public class DatHangViewHolder extends RecyclerView.ViewHolder{
        ImageView imPro;
        TextView proName;
        TextView proPrice;
        TextView proQuantity;

        public DatHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imPro = itemView.findViewById(R.id.ivProductImage);
            proName = itemView.findViewById(R.id.tvName4);
            proPrice = itemView.findViewById(R.id.tvPrice2);
            proQuantity=itemView.findViewById(R.id.tvQuantity);
        }
    }

    public interface DatHangCallBack{

        void onItemLongClicked(Product product);
    }
}
