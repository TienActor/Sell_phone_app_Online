package com.example.mobile_app.DMSP;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;

import com.example.mobile_app.Adapter.AppleAdapter;

import com.example.mobile_app.Adapter.ProductAdapter;
import com.example.mobile_app.Fragment.HomeFragment;
import com.example.mobile_app.Fragment.LSDHFragment;
import com.example.mobile_app.Fragment.LogOutFragment;
import com.example.mobile_app.Home.MainActivity;
import com.example.mobile_app.Home.ProductDetailActivity;
import com.example.mobile_app.Model.Apple;
import com.example.mobile_app.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppleAdapter adapter;

    private DrawerLayout drawerLayout;

    private EditText searchEditText;

    private ImageButton backtt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple);
        //anh xa
        adapter = new AppleAdapter();

        // Tạo adapter và gán vào recyclerView

        recyclerView=findViewById(R.id.rcv_apple);
        drawerLayout= findViewById(R.id.drawer_layout);
        // cai dat adapter

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getProducts("Apple"); // Gọi phương thức với tên hãng mong muốn


        searchEditText = findViewById(R.id.search_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                adapter.search(keyword);
            }
        });

        //thọ
        adapter.setOnItemClickListener(new AppleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int productId) {
                Intent intent = new Intent(AppleActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", productId); // Correctly pass the product ID
                startActivity(intent);
            }
        });

        backtt = findViewById(R.id.backtt);
        backtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AppleActivity.this, MainActivity.class);
                startActivity(it);
            }
        });


    }
    public void getProducts(String brand) {
        Api api = APIClient.getInstance().getMyApi();
        Call<List<Apple>> call = api.getProductsByBrand(brand);
        call.enqueue(new Callback<List<Apple>>() {
            @Override
            public void onResponse(Call<List<Apple>> call, Response<List<Apple>> response) {
                List<Apple> productList = response.body();
                if (productList != null) {
                    adapter.setProducts(productList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Apple>> call, Throwable t) {
                Toast.makeText(AppleActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}