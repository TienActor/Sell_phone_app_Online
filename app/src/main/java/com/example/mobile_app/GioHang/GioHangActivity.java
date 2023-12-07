package com.example.mobile_app.GioHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Home.MainActivity;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.example.mobile_app.User.DatHangActivity;
import com.example.mobile_app.User.DatHangNoLoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GioHangActivity extends AppCompatActivity {
    private RecyclerView recyclerViewGioHang;
    private GioHang1Adapter gioHangAdapter = new GioHang1Adapter(this, new ArrayList<>(), new GioHang1Adapter.GioHangCallBack() {
        @Override
        public void onQuantityChanged(Product product, int quantity) {
            // Cập nhật số lượng sản phẩm
            Api api =    APIClient.getInstance().getMyApi();          //... lấy thực thể API của bạn
            api.updateCart(product.getId(), quantity, 1).enqueue(new Callback<GioHangResponse>() {
                @Override
                public void onResponse(Call<GioHangResponse> call, Response<GioHangResponse> response) {
                    // Xử lý khi thành công
                    if(response.isSuccessful()) {
                        // Cập nhật giỏ hàng thành công
                    }
                }

                @Override
                public void onFailure(Call<GioHangResponse> call, Throwable t) {
                    // Xử lý khi thất bại
                }
            });
        }

        @Override
        public void onItemDeleteRequested(Product product, int position) {
            // Xử lý yêu cầu xóa sản phẩm tại đây

        }
    });
    Button btndathang,btntrangchu;
    ImageButton backtt;
    //code xóa sửa
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);
        recyclerViewGioHang.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGioHang.setAdapter(gioHangAdapter);
        //List<Product> productList = new ArrayList<>();
        // Đặt Adapter cho RecyclerView sau khi đã khởi tạo Adapte
        fetchCartItems();
        //anh xa
        AndXa();
        // quay về trang chủ
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GioHangActivity.this, MainActivity.class);
                startActivity(it);
            }
        });

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    // Người dùng đã đăng nhập, mở DatHangActivity
                    Intent intent = new Intent(GioHangActivity.this, DatHangActivity.class);
                    intent.putExtra("giohang", gioHangAdapter.getProducts());
                    startActivity(intent);
                } else {
                    // Người dùng chưa đăng nhập, mở DatHangNoLoginActivity
                    Intent intent = new Intent(GioHangActivity.this, DatHangNoLoginActivity.class);
                    intent.putExtra("giohang", gioHangAdapter.getProducts());
                    startActivity(intent);
                }
            }
        });
        //

        backtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gioHangAdapter.setGioHangCallBack(new GioHang1Adapter.GioHangCallBack() {
            @Override
            public void onQuantityChanged(Product product, int change) {
                // Cập nhật số lượng sản phẩm
            }
            @Override
            public void onItemDeleteRequested(Product product, int position) {
                /// xoa code
            }
        });
        // Khi bạn muốn cập nhật dữ liệu cho adapter:
        List<Product> productList = fetchDataFromServerOrDatabase();
        gioHangAdapter.clearData();
        gioHangAdapter.updateData(productList);
    }

    private List<Product> fetchDataFromServerOrDatabase() {
        final List<Product> productList = new ArrayList<>();
        // Lấy instance của Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        // Gọi API để lấy danh sách sản phẩm trong giỏ hàng
        Call<Map<String, Product>> call = api.retrieveCart(true);
        call.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Tạo một danh sách mới để chứa các sản phẩm
                    List<Product> productList = new ArrayList<>();
                    for (Map.Entry<String, Product> entry : response.body().entrySet()) {
                        // Lấy ra sản phẩm từ entry
                        Product product = entry.getValue();
                        // Gán giá trị id_phone từ key của entry
                        product.setId_phone(Integer.parseInt(entry.getKey()));
                        // Thêm sản phẩm vào danh sách
                        productList.add(product);
                    }
                    // Cập nhật Adapter với danh sách sản phẩm mới
                    gioHangAdapter.clearData();
                    gioHangAdapter.updateData(productList);
                } else {
                    // Xử lý lỗi nếu có
                }
            }
            @Override
            public void onFailure(Call<Map<String, Product>> call, Throwable t) {
                // Gọi API thất bại
                // Hiển thị thông báo lỗi
            }
        });

        return productList;
    }
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        // Nếu email không phải là chuỗi rỗng, giả định người dùng đã đăng nhập
        String email = preferences.getString("email", ""); // "" là giá trị mặc định nếu không tìm thấy "email"
        return !email.isEmpty();
    }
    public void AndXa(){
        btntrangchu=findViewById(R.id.btntieptucmuahang);
        btndathang=findViewById(R.id.btndathang);
        backtt=findViewById(R.id.backtt);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);

    }
    private void handleIntent(Intent intent) {
        if (intent.hasExtra("quantity") && intent.hasExtra("productId")) {
            int newQuantity = intent.getIntExtra("quantity", 1);
            int productId = intent.getIntExtra("productId", -1);
            if (productId != -1 && gioHangAdapter != null) {
                gioHangAdapter.updateQuantityByProductId(productId, newQuantity);
            }
        }
    }

    private void updateUI() {
        if (gioHangAdapter.isCartEmpty()) {
            findViewById(R.id.textviewthongbao).setVisibility(View.VISIBLE);
            recyclerViewGioHang.setVisibility(View.GONE);
        } else {
            findViewById(R.id.textviewthongbao).setVisibility(View.GONE);
            recyclerViewGioHang.setVisibility(View.VISIBLE);
        }
    }

    public void updateTotal() {
        int total = gioHangAdapter.calculateTotalAmount();
        String formattedTotal = gioHangAdapter.formatTotalAmount(total);
        TextView textViewTongTien = findViewById(R.id.textviewtongtien); // Sửa lại ID phù hợp với layout của bạn
        textViewTongTien.setText(formattedTotal);
    }

    private void fetchCartItems() {

        APIClient apiClient = APIClient.getInstance();
        Api api = apiClient.getMyApi();

        Call<Map<String, Product>> call = api.retrieveCart(true);
        call.enqueue(new Callback<Map<String, Product>>() {
            @Override
            public void onResponse(Call<Map<String, Product>> call, Response<Map<String, Product>> response) {
                if (response.isSuccessful()) {

                    Map<String, Product> productMap = response.body();
                    if (productMap != null) {
                        List<Product> productList = new ArrayList<>(productMap.values());
                        gioHangAdapter.updateData(productList);
                        Log.d("GioHangActivity", "Data loaded successfully");
                    } else {
                        Log.d("GioHangActivity", "Product map is null");
                    }
                    updateUI();
                    updateTotal();
                } else {
                    try {
                        Log.e("GioHangActivity", "Lỗi kết nối : " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("GioHangActivity", "Error while reading error body", e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, Product>> call, Throwable t) {
                Log.e("GioHangActivity", "That bai : " + t.getMessage());
            }
        });

    }

}
