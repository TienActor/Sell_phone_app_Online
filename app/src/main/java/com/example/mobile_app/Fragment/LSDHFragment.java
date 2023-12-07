package com.example.mobile_app.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Adapter.OrderAdapter;
import com.example.mobile_app.Adapter.OrderListAdapter;
import com.example.mobile_app.Model.Order;
import com.example.mobile_app.Model.OrderHistory;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LSDHFragment extends Fragment {

// code mới

    private RecyclerView rvOrderList;
    private TextView tvStatusMessage;
    private ProgressBar progressBar;
    private OrderListAdapter adapter;
    private Spinner spinnerOrderStatus;
    private List<Order> allOrders = new ArrayList<>();

    private int loadingCount = 0;
    // code mới
    private TabLayout tabLayoutOrderStatus;
    private boolean isAllOrdersTabSelected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_l_s_d_h, container, false);

        tabLayoutOrderStatus = view.findViewById(R.id.tabCategory_11);
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Đã hủy"));
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Đã giao"));
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Tất cả"));

        tvStatusMessage = view.findViewById(R.id.tvStatusMessage);
        rvOrderList = view.findViewById(R.id.rvOrderList);
        progressBar = view.findViewById(R.id.progressBar);
        //spinnerOrderStatus = view.findViewById(R.id.spinnerOrderStatus);


        rvOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderListAdapter(getFragmentManager(), new ArrayList<Order>());
        rvOrderList.setAdapter(adapter);


        TabLayout.Tab defaultTab = tabLayoutOrderStatus.getTabAt(0);
        if (defaultTab != null) {
            defaultTab.select();

        }

        tabLayoutOrderStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        // Tab "Đã giao"
                        isAllOrdersTabSelected = false;
                        loadDeliveredOrders(() -> filterOrdersAndUpdateUI("Đã giao"));
                        break;
                    case 1:
                        // Tab "Đã hủy"
                        isAllOrdersTabSelected = false;
                        loadCancelledOrders(() -> filterOrdersAndUpdateUI("Đã hủy"));
                        break;
                    case 2:
                        // Tab "Tất cả"
                        isAllOrdersTabSelected = true;
                        loadDeliveredAndCancelledOrders();
//                        loadAllOrders();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected state if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected state if needed
            }
        });
        //rvOrderList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        loadDeliveredOrders(()->filterOrdersAndUpdateUI("Đã giao"));
        return view;
    }
    private void filterOrdersAndUpdateUI(String... statuses) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : allOrders) {
            for (String status : statuses) {
                if (order.getStatus().equals(status)) {
                    filteredList.add(order);
                    break;
                }
            }
        }
        getActivity().runOnUiThread(() -> {
            updateUIWithFilteredList(filteredList);
            progressBar.setVisibility(View.GONE); // Ẩn progress bar khi hoàn thành cập nhật UI
        });
    }
    private void updateUIWithFilteredList(List<Order> filteredList) {
        adapter.setOrders(filteredList);
        adapter.notifyDataSetChanged();
        toggleVisibility(filteredList.isEmpty());
    }
    private void toggleVisibility(boolean isEmpty) {
        rvOrderList.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvStatusMessage.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        tvStatusMessage.setText(isEmpty ? "Không có đơn hàng nào." : "");
    }
    private synchronized void incrementLoadingCount() {
        loadingCount++;
        if (isAllOrdersTabSelected && loadingCount == 2) {
            filterOrdersAndUpdateUI("Chưa duyệt", "Đã duyệt", "Đang giao");
        }
    }

    private void handleError() {
        // Handle the error, e.g., by showing a message
        displayStatusMessage("An error occurred while loading orders.");
    }
    private void loadDeliveredOrders(Runnable callback) {
        APIClient.getInstance().getMyApi().getDeliveredOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allOrders.clear();
                    allOrders.addAll(response.body());
                }
                if (callback != null) {
                    callback.run();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                handleError();
                if (callback != null) {
                    callback.run();
                }
            }
        });
    }

    private void loadCancelledOrders(Runnable callback){
        APIClient.getInstance().getMyApi().getCancelledOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() !=null)
                {
                    allOrders.clear();
                    allOrders.addAll(response.body());
                }
                if (callback != null)
                {
                    callback.run();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                handleError();
                if (callback!=null)
                {
                    callback.run();
                }
            }
        });
    }
    private void loadDeliveredAndCancelledOrders() {
        progressBar.setVisibility(View.VISIBLE);
        allOrders.clear(); // Clear all current orders
        loadDeliveredOrders(() -> {
            // After delivered orders are loaded, load cancelled orders
            loadCancelledOrders(() -> {
                // After cancelled orders are loaded, update the UI
                updateUIWithAllOrders();
                progressBar.setVisibility(View.GONE);
            });
        });
    }

    private void updateUIWithAllOrders() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                adapter.setOrders(allOrders);
                adapter.notifyDataSetChanged(); // Đảm bảo RecyclerView cập nhật UI
                boolean noOrders = allOrders.isEmpty();
                rvOrderList.setVisibility(noOrders ? View.GONE : View.VISIBLE);
                tvStatusMessage.setVisibility(noOrders ? View.VISIBLE : View.GONE);
                tvStatusMessage.setText(noOrders ? "Không có đơn hàng nào." : "");
            });
        }
    }

    private void displayStatusMessage(String message) {
        tvStatusMessage.setText(message);
        tvStatusMessage.setVisibility(View.VISIBLE);
        rvOrderList.setVisibility(View.GONE); // Hide the RecyclerView if there's a status message
    }

    private void loadAllOrders() {
        synchronized (this) {
            progressBar.setVisibility(View.VISIBLE);
            allOrders.clear(); // Xóa danh sách khi tab "Tất cả" được chọn
            loadingCount = 0; // Đặt lại đếm tải
            isAllOrdersTabSelected = true; // Đánh dấu rằng tab "Tất cả" đang được xử lý
            loadCancelledOrders(this::incrementLoadingCount);
            loadDeliveredOrders(this::incrementLoadingCount);
        }
    }
}