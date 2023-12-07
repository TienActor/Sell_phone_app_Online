package com.example.mobile_app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.Adapter.OrderDetailAdapter;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChitietFragment extends Fragment implements OrderDetailAdapter.OnCancelOrderListener {

    private RecyclerView recyclerView;
    private OrderDetailAdapter orderDetailAdapter;
    private boolean viewingProducts = true;
    private int orderId;
    Button huydonn ;

    public ChitietFragment() {
        // Constructor rỗng được yêu cầu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chitiet, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChitiet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderDetailAdapter = new OrderDetailAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(orderDetailAdapter);


        ToggleButton toggleView = view.findViewById(R.id.toggle_view);
        toggleView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewingProducts = isChecked;
            setAdapterBasedOnCondition();
        });
        huydonn = view.findViewById(R.id.bthhuydonn);
        huydonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for "Hủy đơn" button
                if (orderDetailAdapter.getItemCount() > 0) {
                    // Assuming you want to cancel the order for the first item in the list
                    onCancelOrder(0);
                }
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getInt("id", -1);
            if (orderId != -1) {
                fetchOrderDetails();
            } else {
                handleInvalidOrderId();
            }
        } else {
            handleMissingBundle();
        }

        return view;
    }

    private void setAdapterBasedOnCondition() {
        // Do nothing, vì chỉ có một adapter
    }

    private void fetchOrderDetails() {
        APIClient.getInstance().getMyApi().getOrderItemsById(orderId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();
                    orderDetailAdapter.setProducts(productList);
                } else {
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }

    private void handleInvalidOrderId() {
        Log.e("ChitietFragment", "Invalid orderId");
        Toast.makeText(getContext(), "Invalid orderId. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private void handleMissingBundle() {
        Log.e("ChitietFragment", "Bundle is null");
        Toast.makeText(getContext(), "Error loading order details. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private void handleApiError(int errorCode) {
        Log.e("ChitietFragment", "Error fetching order details. Code: " + errorCode);
        Toast.makeText(getContext(), "Error fetching order details. Please try again later.", Toast.LENGTH_SHORT).show();
    }

    private void handleNetworkError(Throwable throwable) {
        Log.e("ChitietFragment", "Error fetching order details", throwable);
        Toast.makeText(getContext(), "Error fetching order details. Please check your network connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelOrder(int position) {
        // Thực hiện cuộc gọi API hủy đơn hàng và xóa từ giỏ hàng
        APIClient.getInstance().getMyApi().cancelOrder(orderId, 1).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();

                    // Handle the canceled order product as needed
                    // ...

                    // Remove the product from the list and update the RecyclerView
                    orderDetailAdapter.getproductList().remove(position);
                    orderDetailAdapter.notifyItemRemoved(position);

                    // Optional: Notify the user about the cancellation
                    Toast.makeText(getContext(), "Order canceled successfully.", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng người dùng về QLDHFragment
                    redirectToQLDHFragment();
                } else {
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }

    private void redirectToQLDHFragment() {
        // Sử dụng FragmentManager để chuyển hướng về QLDHFragment
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStackImmediate(); // Xóa Fragment hiện tại khỏi Back Stack
        }

    }

}

