package com.example.mobile_app.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.mobile_app.Review.Review;
import com.example.mobile_app.Review.ReviewActivity;
import com.example.mobile_app.Review.ReviewAdapter;
import com.example.mobile_app.User.LoginActivity;

import com.example.mobile_app.Model.User;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.GioHang.GioHangResponse;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private Api api = APIClient.getInstance().getMyApi(); //
    private Product product;
    private User user;
    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productStorageTextView,
            productMemoryTextView, productYearReleaseTextView, productAmountTextView,
            productBrandTextView, productOSTextView;
    private Button  addToCartButton;

    private Button  reviewPostButton;
    private ImageButton btnGoBack;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewsList = new ArrayList<>();


    //tien

    private EditText edtQuan;
    private ImageView backButton;
    //code mới
    private Product productDetail; // Declare productDetail as a class-level variable



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ các view
        initializeViews();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về Activity trước đó
                finish();
            }
        });
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            fetchProductDetails(productId);
            fetchReviews(productId);
        }

        // chuyen sang trang gio hang
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = edtQuan.getText().toString();
                if (!quantityStr.isEmpty()) {
                    int quantity = Integer.parseInt(quantityStr);
                    if (productDetail != null) {
                        // Check if the entered quantity is valid
                        if (quantity > 0 && quantity <= productDetail.getAmount()) {
                            addToCart(productDetail.getId(), quantity); // Pass product ID to addToCart
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Không thể thêm vào giỏ hàng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //chuyền sang trang đánh giá sản phẩm
        reviewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    // Truyền ID sản phẩm và URL hình sản phẩm qua Intent
                    Intent intent = new Intent(ProductDetailActivity.this, ReviewActivity.class);
                    intent.putExtra("product_id", product.getId());
                    intent.putExtra("product_image_url", product.getImg_url());
                    startActivity(intent);
                    // Bắt đầu ReviewActivity
                    startActivity(intent);
                } else {
                    // Nếu người dùng chưa đăng nhập, chuyển đến LoginActivity
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addToCart(int productId, int quantity) {
        api.addToCart(productId, quantity, 1).enqueue(new Callback<GioHangResponse>() {
            @Override
            public void onResponse(Call<GioHangResponse> call, Response<GioHangResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GioHangResponse gioHangResponse = response.body();
                    if (gioHangResponse.getMessage() != null && !gioHangResponse.getMessage().isEmpty()) {
                        Toast.makeText(ProductDetailActivity.this, gioHangResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Thêm giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GioHangResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        productImageView = findViewById(R.id.product_img_url);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        productStorageTextView = findViewById(R.id.product_storage);
        productMemoryTextView = findViewById(R.id.product_memory);
        productYearReleaseTextView = findViewById(R.id.product_year_release);
        productAmountTextView = findViewById(R.id.product_amount);
        productBrandTextView = findViewById(R.id.product_brand);
        productOSTextView = findViewById(R.id.product_os);
        btnGoBack = findViewById(R.id.btnGoBack);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        reviewPostButton = findViewById(R.id.reviewPost);
        reviewsRecyclerView = findViewById(R.id.rcvReview);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewsList);
        reviewsRecyclerView.setAdapter(reviewAdapter);
        // tien 2 11
        edtQuan = findViewById(R.id.editTextQuantity);
    }



    // yêu cầu trả dữ liệu theo id
    private void fetchProductDetails(int productId) {
        Call<List<Product>> call = api.getProductDetails(productId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    productDetail = response.body().get(0); // Assign productDetail
                    updateUIWithProductDetail(productDetail);
                   // Product productDetail = response.body().get(0);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Error fetching details", Toast.LENGTH_SHORT).show();
                    Log.d("API_RESPONSE", "Error: " + response.message() + " | Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Log.d("API_RESPONSE", "Failure: " + t.getMessage());
            }
        });
    }

    private void updateUIWithProductDetail(Product productDetail) {
        if (productDetail != null) {
            this.product = productDetail;
            Picasso.get().load(productDetail.getImg_url()).into(productImageView);
            productNameTextView.setText(productDetail.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            productPriceTextView.setText("Giá: " + String.valueOf(decimalFormat.format(productDetail.getPrice()) + "đ"));
            productStorageTextView.setText(String.valueOf("Dung lượng: " + productDetail.getStorage())+" GB");
            productMemoryTextView.setText("Bộ nhớ: " + String.valueOf(productDetail.getMemory())+" GB");
            productYearReleaseTextView.setText(String.valueOf("Năm ra mắt:năm " + productDetail.getYear_release()));
            productAmountTextView.setText(String.valueOf("Số lượng: " + productDetail.getAmount()));
            productBrandTextView.setText("Hãng sản xuất: " + productDetail.getBrand_name());
            productOSTextView.setText("Hệ điều hành: " + productDetail.getOs_name());
        }
    }

    private void fetchReviews(int productId) {
        api.getReview(productId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewsList.clear();
                    reviewsList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không thể tải đánh giá.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    //kiểm tra đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String userEmail = preferences.getString("email", null);
        return userEmail != null;
    }
}
