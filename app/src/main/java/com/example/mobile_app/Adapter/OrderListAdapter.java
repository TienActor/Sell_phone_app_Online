package com.example.mobile_app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Fragment.ChitietFragment;
import com.example.mobile_app.Model.Order;
import com.example.mobile_app.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<Order> orderList;
    private FragmentManager fragmentManager;
    private List<Order> filteredOrderList;

    // Constructor
    public OrderListAdapter(FragmentManager fragmentManager, List<Order> orderList) {
        this.fragmentManager = fragmentManager;
        this.orderList = orderList;
        this.filteredOrderList = new ArrayList<>(orderList);
    }


    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        this.filteredOrderList = new ArrayList<>(orders);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return filteredOrderList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = filteredOrderList.get(position);
        holder.bind(order);
    }

    private static String formatDate(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(dateString);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderId, tvOrderStatus, tvOrderDate, tvDeliveryDate, tvTotalAmount, tvPaymentMethod;
        Button chitiet;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            chitiet = itemView.findViewById(R.id.Btnchitiet);

            chitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Order clickedOrder = filteredOrderList.get(position);
                        final Context context = v.getContext();
                        Api api = APIClient.getInstance().getMyApi();
                        int orderId = clickedOrder.getId(); // Use getId() for the order ID

                        Call<Order> call = api.getcuthdh(orderId);

                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Order order = response.body();
                                    ChitietFragment fragment = new ChitietFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", orderId); // Use "id" as the key
                                    fragment.setArguments(bundle);

                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.user_fragment, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                } else {
                                    handleErrorResponse(response, context);
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                handleFailure(t, context);
                            }
                        });
                    }
                }
            });
        }

        public void bind(Order order) {
            tvOrderId.setText(String.format(Locale.getDefault(), "ID đơn hàng: %d", order.getId()));
            tvOrderStatus.setText(String.format("Tình Trạng: %s", order.getStatus()));

            // Check if date and dateShip are not null before formatting
            if (order.getDate() != null) {
                tvOrderDate.setText(String.format("Ngày đặt: %s", formatDate(order.getDate())));
            } else {
                tvOrderDate.setText("Ngày đặt: N/A");
            }

            if (order.getDateShip() != null) {
                tvDeliveryDate.setText(String.format("Ngày giao hàng: %s", formatDate(order.getDateShip())));
            } else {
                tvDeliveryDate.setText("Ngày giao hàng: N/A");
            }

            tvPaymentMethod.setText(String.format("Phương thức thanh toán: %s", order.getMethodName()));
            tvTotalAmount.setText(String.format(Locale.getDefault(), "Tổng tiền: %,d VND", order.getFinalTotal()));
        }

        private void handleErrorResponse(Response<Order> response, Context context) {
            String errorBody = "";
            try {
                errorBody = response.errorBody() != null ? response.errorBody().string() : "";
            } catch (IOException e) {
                Log.e("API_ERROR", "Error reading error body", e);
            }
            Log.e("API_ERROR", "Error response from server: " + response.code() + ", body: " + errorBody);
            Toast.makeText(context, "Lỗi khi lấy dữ liệu từ server: " + errorBody, Toast.LENGTH_LONG).show();
        }

        private void handleFailure(Throwable t, Context context) {
            Log.e("API_ERROR", "Lỗi kết nối đến server", t);
            Toast.makeText(context, "Lỗi kết nối đến server: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
