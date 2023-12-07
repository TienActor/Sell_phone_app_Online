package com.example.mobile_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Adapter.AppleAdapter;
import com.example.mobile_app.Adapter.BannerAdapter;
import com.example.mobile_app.DMSP.AppleActivity;
import com.example.mobile_app.DMSP.NokiaActivity;
import com.example.mobile_app.DMSP.OppoActivity;
import com.example.mobile_app.DMSP.SamsungActivity;
import com.example.mobile_app.DMSP.XiaomeActivity;
import com.example.mobile_app.Home.ProductDetailActivity;
import com.example.mobile_app.Model.Apple;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.Adapter.ProductAdapter;

import com.example.mobile_app.R;
import com.example.mobile_app.User.SearchResultsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    String query = "test query";
    private boolean isScrolling;
    private boolean isLastItem;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private TextView hourFlashSaleTextView, minFlashSaleTextView, secFlashSaleTextView,dayFlashSaleTextView;
    private RecyclerView recyclerView,flashSale;
    private ProductAdapter productAdapter;
    private AppleAdapter appleAdapter;
    private List<Product> productList;

    public static ArrayList<Product> productSale = new ArrayList<>();
    private EditText searchEditText;
    private ImageView searchButton;
    private ViewPager bannerViewPager;
    private BannerAdapter bannerAdapter;
    BannerAdapter photoAdapter;
    CircleIndicator circleIndicator;
    List<Integer> listPhoto;
    ViewPager viewPager;
    ViewPager viewPager_2;
    Toolbar toolbar, toolbar_0;
    Timer timer, timer_2;
    private StaggeredGridLayoutManager manager;
    private Handler handler = new Handler();
    private int currentPage = 0;
    public ImageView imappleC, imnokiaC,imoppoC,imsamsungC,imxiaomeC;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (bannerViewPager.getAdapter().getCount() != 0) {
                currentPage = (currentPage + 1) % bannerViewPager.getAdapter().getCount();
                bannerViewPager.setCurrentItem(currentPage, true);
                handler.postDelayed(runnable, 3000);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        anhXa(view);
        // Initialize RecyclerView, Adapter, and other views
        productList = new ArrayList<>(); // Initialize the productList
        productSale = new ArrayList<>();
        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Khởi tạo và thiết lập cho ViewPager
        bannerAdapter = new BannerAdapter(getContext(), listPhoto);
        bannerViewPager.setAdapter(bannerAdapter);
        // Bắt đầu chạy banner tự động
        handler.postDelayed(runnable, 3000);
        // Load products
        getProducts();
        listPhoto = listBanner();
        //set Adapter
        photoAdapter = new BannerAdapter(getContext(), listPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        searchEditText = view.findViewById(R.id.search_text);
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                searchProducts(s.toString()); // Gọi API tìm kiếm với từ khóa mới
//            }
//        });
        // Trong onCreateView của HomeFragment...
        searchButton = view.findViewById(R.id.btn_search);
        searchEditText = view.findViewById(R.id.search_text);
        setupFlashSaleTimer();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    searchProducts(searchQuery); // Gọi hàm tìm kiếm sản phẩm với từ khóa
                } else {
                    Toast.makeText(getActivity(), "Vui lòng nhập từ khóa tìm kiếm.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Api productService = APIClient.getInstance().getMyApi();
        Call<List<Apple>> call = productService.getProductsByBrand("apple");
        call.enqueue(new Callback<List<Apple>>() {
            @Override
            public void onResponse(Call<List<Apple>> call, Response<List<Apple>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Apple> appleProducts = response.body();
                    updateFlashSaleWithAppleProducts(appleProducts);
                } else {
                    // Xử lý trường hợp không nhận được phản hồi hoặc phản hồi lỗi
                }
            }

            @Override
            public void onFailure(Call<List<Apple>> call, Throwable t) {
                // Xử lý lỗi
            }
        });

        // Khởi tạo và thiết lập cho ViewPager2
        //gan Adapter

        // Inflate the layout for this fragment

        //thọ


        //tien
        imappleC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppleActivity.class);

                startActivity(intent);
            }
        });
        imnokiaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NokiaActivity.class);

                startActivity(intent);
            }
        });
        imxiaomeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), XiaomeActivity.class);

                startActivity(intent);
            }
        });
        imoppoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OppoActivity.class);

                startActivity(intent);
            }
        });
        imsamsungC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SamsungActivity.class);

                startActivity(intent);
            }
        });
        return view;
    }
    private void setupFlashSaleTimer() {
        // Thời điểm kết thúc flash sale, tính từ thời điểm hiện tại cộng thêm 4 ngày và 12 tiếng
        long fourDaysInMillis = TimeUnit.DAYS.toMillis(4);
        long twelveHoursInMillis = TimeUnit.HOURS.toMillis(13);
        long endTime = System.currentTimeMillis() + fourDaysInMillis + twelveHoursInMillis;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long millisUntilFinished = endTime - System.currentTimeMillis();
                if (millisUntilFinished > 0) {
                    // Cập nhật TextViews
                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                    // Giả sử bạn có TextView cho ngày
                    dayFlashSaleTextView.setText(String.format(Locale.getDefault(), "%02d", days));
                    hourFlashSaleTextView.setText(String.format(Locale.getDefault(), "%02d", hours));
                    minFlashSaleTextView.setText(String.format(Locale.getDefault(), "%02d", minutes));
                    secFlashSaleTextView.setText(String.format(Locale.getDefault(), "%02d", seconds));

                    // Lặp lại sau mỗi 1000ms
                    handler.postDelayed(this, 1000);
                } else {
                    // Flash sale đã kết thúc, cập nhật UI tương ứng
                    dayFlashSaleTextView.setText("00");
                    hourFlashSaleTextView.setText("00");
                    minFlashSaleTextView.setText("00");
                    secFlashSaleTextView.setText("00");
                }
            }
        }, 0);
    }


    public void getProducts() {
        Api productService = APIClient.getInstance().getMyApi();
        Call<List<Product>> call = productService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                if (productList != null) {
                    productAdapter.setProducts(productList);
                    productAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }
    private List<Integer> listBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.banner_01);
        list.add(R.drawable.banner_02);
        list.add(R.drawable.banner_03);
        return list;
    }

    private void anhXa(View view){
        recyclerView = view.findViewById(R.id.rcv_Phone);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        viewPager = view.findViewById(R.id.banner_viewpager);
        bannerViewPager = view.findViewById(R.id.banner_viewpager);
        imappleC = view.findViewById(R.id.imapple);
        imnokiaC = view.findViewById(R.id.imnokia);
        imxiaomeC = view.findViewById(R.id.imxiaome);
        imoppoC = view.findViewById(R.id.imoppo);
        imsamsungC = view.findViewById(R.id.imsamsung);
        flashSale = view.findViewById(R.id.recyclerViewFlashSale);
        // Lấy tham chiếu đến các TextView từ layout
        dayFlashSaleTextView = view.findViewById(R.id.day_flashsale);
        hourFlashSaleTextView = view.findViewById(R.id.hour_flashsale);
        minFlashSaleTextView = view.findViewById(R.id.min_flashsale);
        secFlashSaleTextView = view.findViewById(R.id.sec_flashsale);
    }
    private void updateFlashSaleWithAppleProducts(List<Apple> appleProducts) {
        appleAdapter = new AppleAdapter(getContext(), new ArrayList<>(appleProducts));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        flashSale.setLayoutManager(layoutManager);
        flashSale.setAdapter(appleAdapter);
        appleAdapter.notifyDataSetChanged();
    }
    private List<Apple> convertProductListToAppleList(List<Product> products) {
        // Giả định rằng Apple là subclass của Product hoặc có thể chuyển đổi từ Product
        List<Apple> apples = new ArrayList<>();
        for (Product product : products) {
            // Bạn cần viết logic chuyển đổi từ Product sang Apple ở đây
            // Ví dụ:
            Apple apple = new Apple(product.getId(), product.getName(), product.getPrice(), product.getImg_url());
            apples.add(apple);
        }
        return apples;
    }
    private void searchProducts(String query) {
        Api productService = APIClient.getInstance().getMyApi();
        Call<List<Product>> call = productService.searchProducts(query);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> searchResults = response.body();
                    Intent searchIntent = new Intent(getActivity(), SearchResultsActivity.class);
                    // Dùng putExtra với Serializable
                    searchIntent.putExtra("SEARCH_RESULTS", (Serializable) searchResults);
                    startActivity(searchIntent);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy sản phẩm.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}