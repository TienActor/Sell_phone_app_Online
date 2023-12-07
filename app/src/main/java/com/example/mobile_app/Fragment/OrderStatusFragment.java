package com.example.mobile_app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.Model.Order;
import com.example.mobile_app.R;
import com.example.mobile_app.Adapter.OrderListAdapter;
import com.google.android.material.tabs.TabLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class OrderStatusFragment extends Fragment {
    private TextView tvStatusMessage;
    private RecyclerView rvOrderList;
    private ProgressBar progressBar;
    private OrderListAdapter adapter;
    private int loadingCount = 0;

    private List<Order> allOrders = new ArrayList<>();
    private TabLayout tabLayoutOrderStatus;
    private boolean isAllOrdersTabSelected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);

        tabLayoutOrderStatus = view.findViewById(R.id.tabCategory);
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Chưa duyệt"));
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Đã duyệt"));
        tabLayoutOrderStatus.addTab(tabLayoutOrderStatus.newTab().setText("Tất cả"));

        tvStatusMessage = view.findViewById(R.id.tvStatusMessage);
        rvOrderList = view.findViewById(R.id.rvOrderList);
        progressBar = view.findViewById(R.id.progressBar);

        // Set up the RecyclerView
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
                        // Tab "Chưa duyệt"
                        isAllOrdersTabSelected = false;
                        loadNotConfirmedOrders(() -> filterOrdersAndUpdateUI("Chưa duyệt"));
                        break;
                    case 1:
                        // Tab "Đã duyệt"
                        isAllOrdersTabSelected = false;
                        loadOrderStatus(() -> filterOrdersAndUpdateUI("Đã duyệt", "Đang giao"));
                        break;
                    case 2:
                        // Tab "Tất cả"
                        isAllOrdersTabSelected = true;
                        loadAllOrders();
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
        loadNotConfirmedOrders(() -> filterOrdersAndUpdateUI("Chưa duyệt"));

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

    private void loadOrderStatus(final Runnable callback) {
        APIClient.getInstance().getMyApi().getOrderStatus().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (!isAllOrdersTabSelected) {
                        // Chỉ xóa danh sách đơn hàng chưa duyệt khi tab "Chưa duyệt" được chọn
                        allOrders.clear();
                    }
                    allOrders.addAll(response.body());
                    callback.run();
                } else {
                    handleResponseError(response);
                    callback.run();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                handleError(t);
            }
        });
    }

    private void handleResponseError(Response<List<Order>> response) {
        try {
            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            displayStatusMessage(errorMessage);
        } catch (IOException e) {
            displayStatusMessage("An error occurred while retrieving the error message.");
        }
    }


    private void loadNotConfirmedOrders(Runnable callback) {
        APIClient.getInstance().getMyApi().getNotConfirmedOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!isAllOrdersTabSelected) {
                        // Chỉ xóa danh sách đơn hàng chưa duyệt khi tab "Chưa duyệt" được chọn
                        allOrders.clear();
                    }
                    allOrders.addAll(response.body());
                }
                callback.run();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                handleError();
                callback.run();
            }
        });
    }


    // Refactored error handling for reuse
    // Sử dụng phương pháp này để xử lý tất cả các onFailure trong các phương thức tải khác
    private void handleError(Throwable t) {
        progressBar.setVisibility(View.GONE);
        displayStatusMessage("Error connecting to the server: " + t.getMessage());
    }




    private void displayStatusMessage(String message) {
        tvStatusMessage.setText(message);
        tvStatusMessage.setVisibility(View.VISIBLE);
        rvOrderList.setVisibility(View.GONE); // Hide the RecyclerView if there's a status message
    }


    private void handleError() {
        // Handle the error, e.g., by showing a message
        displayStatusMessage("An error occurred while loading orders.");
    }


    private synchronized void incrementLoadingCount() {
        loadingCount++;
        if (isAllOrdersTabSelected && loadingCount == 2) {
            filterOrdersAndUpdateUI("Chưa duyệt", "Đã duyệt", "Đang giao");
        }
    }
    private void loadAllOrders() {
        synchronized (this) {
            progressBar.setVisibility(View.VISIBLE);
            allOrders.clear(); // Xóa danh sách khi tab "Tất cả" được chọn
            loadingCount = 0; // Đặt lại đếm tải
            isAllOrdersTabSelected = true; // Đánh dấu rằng tab "Tất cả" đang được xử lý
            loadNotConfirmedOrders(this::incrementLoadingCount);
            loadOrderStatus(this::incrementLoadingCount);
        }
    }
}