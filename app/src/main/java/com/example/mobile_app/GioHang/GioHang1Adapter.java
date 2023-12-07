package com.example.mobile_app.GioHang;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;



import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GioHang1Adapter extends RecyclerView.Adapter<GioHang1Adapter.ViewHolder> {
    private List<Product> products;
    private Context context;
    private GioHangCallBack gioHangCallBack;
    private int totalPrice = 0;
    public GioHang1Adapter(Context context, List<Product> products, GioHangCallBack gioHangCallBack) {
        this.context = context;
        this.products = products;
        this.gioHangCallBack = gioHangCallBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham_giohang, parent, false);
        return new ViewHolder(view, context);
    }
    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }
    // hàm lấy thông tin
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        //holder.bindData(product);
        Glide.with(context).load(product.getImg_url()).into(holder.imgViewGioHang);
        // Populate text views and buttons
        holder.textViewTenGioHang.setText("Tên: "+product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewGiaGioHang.setText("Giá: " + decimalFormat.format(product.getPrice())+"Đ");
        holder.tv_Quantity.setText(context.getResources().getString(R.string.quantity_text, product.getQuatity()));
    }
    public void deleteProduct(Product product,int position) {
        // Xóa sản phẩm khỏi danh sách (ví dụ)  chưa lấy được id
        products.remove(product.getId());
        // Cập nhật Adapter
        Log.d("deleteCart", "Updating product ID: " + product.getId() + " with server: " );
        notifyItemRemoved(position);
    }
    // hàm tính tiền
    public int calculateTotalAmount() {
        int totalAmount = 0;
        for (Product product : products) {
            totalAmount += product.getPrice() * product.getQuatity();
        }
        return totalAmount;
    }
    public String formatTotalAmount(int totalAmount) {
        return NumberFormat.getNumberInstance(Locale.US).format(totalAmount) + " Đ";
    }
    public void setMainCallBack(GioHang1Adapter.GioHangCallBack gioHangCallBack) {
        this.gioHangCallBack = gioHangCallBack;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    public void setGioHangCallBack(GioHangCallBack gioHangCallBack) {
        this.gioHangCallBack = gioHangCallBack;
    }
    public void updateData(List<Product> newProducts) {
        products.clear(); // Xóa dữ liệu cũ
        products.addAll(newProducts); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Thông báo thay đổi dữ liệu
    }
    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgViewGioHang;
        TextView textViewTenGioHang;
        TextView textViewGiaGioHang;
        TextView tv_Quantity;
        Button delete,update;
        public ViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            imgViewGioHang = itemView.findViewById(R.id.imgviewgiohang);
            textViewTenGioHang = itemView.findViewById(R.id.textviewtengiohang);
            textViewGiaGioHang = itemView.findViewById(R.id.textviewgiagiohang);
            tv_Quantity=itemView.findViewById(R.id.tv_Quantity);
            // textviewtongtien=itemView.findViewById(R.id.textviewtongtien);
            update=itemView.findViewById(R.id.btnUpdate);
            delete =itemView.findViewById(R.id.btnCancel);


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_sl, null);
                    builder.setView(dialogView);
                    EditText editTextUpSL = dialogView.findViewById(R.id.editTextupsl);
                    // Đặt một TextWatcher để xác thực chỉ số nguyên được nhập
                    editTextUpSL.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            String input = s.toString();
                            if (!input.matches("^\\d+$")) { // Regex cho số nguyên dương
                                editTextUpSL.setError("Chỉ nhập số nguyên dương!");
                            }
                        }
                    });
                    builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String quantityStr = editTextUpSL.getText().toString();
                            if (!quantityStr.matches("^\\d+$")) {
                                editTextUpSL.setError("Chỉ nhập số nguyên dương!");
                                return; // Dừng lại nếu dữ liệu không hợp lệ
                            }
                            int newQuantity = Integer.parseInt(quantityStr);
                            Product product = products.get(getAdapterPosition());

                            // Ensure that product is not null
                            if (product != null) {
                                int currentQuantity = product.getQuatity(); // Get the current quantity from Product
                                if (newQuantity <= currentQuantity) {
                                    Log.d("UpdateCart", "Updating product ID: " + product.getId()
                                            + " with quantity: " + newQuantity);

                                    // Make the API call to update the cart
                                    APIClient.getInstance().getMyApi().updateCart(product.getId_phone(), newQuantity, 1).enqueue(new Callback<GioHangResponse>() {
                                        @Override
                                        public void onResponse(Call<GioHangResponse> call, Response<GioHangResponse> response) {
                                            if (response.isSuccessful()) {
                                                // Handle a successful response
                                                // Update the quantity in the Product object after a successful API call
                                                product.setQuatity(newQuantity);
                                                notifyItemChanged(getAdapterPosition());
                                                // Update total
                                                if (context instanceof GioHangActivity) {
                                                    ((GioHangActivity) context).updateTotal();
                                                }
                                            } else {
                                                // Handle API response error
                                                Toast.makeText(context, "Lỗi khi cập nhật giỏ hàng!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GioHangResponse> call, Throwable t) {
                                            // Handle API call failure
                                            Toast.makeText(context, "Lỗi khi gọi API!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Show an error message for invalid quantity
                                    Toast.makeText(context, "Số lượng phải nhỏ hơn hoặc bằng giá trị trước khi cập nhật!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the case where the product is null
                                Toast.makeText(context, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy vị trí và sản phẩm
                    int position = getAdapterPosition();
                    Product product = products.get(position);

                    // Hiển thị AlertDialog để xác nhận việc xóa
                    new AlertDialog.Builder(context)
                            .setTitle("Xác nhận xóa sản phẩm")
                            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int idPhone = product.getId_phone(); // Sử dụng getId_phone() thay vì getId()
                                    if (gioHangCallBack != null) {
                                        gioHangCallBack.onItemDeleteRequested(product, position);
                                    }
                                    deleteCartItem(idPhone, position); // Di chuyển gọi API vào đây

                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
            });
        }

    }
    // Thêm phương thức trong interface GioHangCallBack để xử lý yêu cầu xóa từ Adapter
    public interface GioHangCallBack {
        void onQuantityChanged(Product product, int change);
        void onItemDeleteRequested(Product product, int position); // Thêm dòng này
    }

    public void updateQuantityByProductId(int productId, int newQuantity) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                products.get(i).setAmount(newQuantity);
                notifyItemChanged(i);
                break;
            }
        }
    }
    public boolean isCartEmpty() {
        return products == null || products.isEmpty();
    }
    public void clearData() {
        products.clear();
        notifyDataSetChanged();
    }
    // Phương thức xóa sản phẩm khỏi giỏ hàng
    private void deleteCartItem(int idPhone, int position) {
        // Gọi API để xóa sản phẩm khỏi giỏ hàng
        Api api = APIClient.getInstance().getMyApi();
        api.deleteFromCart(idPhone, 1).enqueue(new Callback<GioHangResponse>() {
            @Override
            public void onResponse(Call<GioHangResponse> call, Response<GioHangResponse> response) {
                if(response.isSuccessful()) {
                    // Xóa sản phẩm khỏi giỏ hàng thành công
                    products.remove(position); // Xóa sản phẩm khỏi danh sách
                    notifyItemRemoved(position); // Cập nhật Adapter
                    if (context instanceof GioHangActivity) {
                        ((GioHangActivity) context).updateTotal(); // Cập nhật tổng giá giỏ hàng
                    }
                } else {
                    // Xử lý lỗi khi gọi API không thành công
                    Toast.makeText(context, "Lỗi khi xóa sản phẩm.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GioHangResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Toast.makeText(context, "Lỗi kết nối mạng.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
