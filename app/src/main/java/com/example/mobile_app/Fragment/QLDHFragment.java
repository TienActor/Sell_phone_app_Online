package com.example.mobile_app.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Adapter.QldhAdapter;
import com.example.mobile_app.Model.QLDH;
import com.example.mobile_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QLDHFragment extends Fragment {

    ///code cua tien

    private RecyclerView recyclerView;
    private QldhAdapter qldhAdapter;

    private List<QLDH> orderList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_l_d_h, container, false);
//        return inflater.inflate(R.layout.fragment_q_l_d_h, container, false);
        anhXa(view);
        orderList = new ArrayList<>(); // Initialize the productList
        qldhAdapter = new QldhAdapter();
        recyclerView.setAdapter(qldhAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        // Load products
        getQLDH();
        return view;
    }




    public void getQLDH() {
        Api OrderHService = APIClient.getInstance().getMyApi();
        Call<List<QLDH>> call = OrderHService.getQLDH();

        call.enqueue(new Callback<List<QLDH>>() { // <--- Sửa lại kiểu dữ liệu ở đây
            @Override
            public void onResponse(Call<List<QLDH>> call, Response<List<QLDH>> response) {
                orderList = response.body();
                if (orderList != null) {
                    qldhAdapter.setQLDH(orderList); // <-- Bạn cần một phương thức trong QLDHAdapter để set dữ liệu
                    qldhAdapter.notifyDataSetChanged();
                }

            }


            @Override
            public void onFailure(Call<List<QLDH>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }


    private void anhXa(View view){
        recyclerView = view.findViewById(R.id.recycleview_qldh);

    }
}