package com.example.mobile_app.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import com.example.mobile_app.Adapter.OrderAdapter;
import com.example.mobile_app.Adapter.ProductAdapter;
import com.example.mobile_app.Fragment.HomeFragment;
import com.example.mobile_app.Fragment.InfoFragment;
import com.example.mobile_app.Fragment.LSDHFragment;
import com.example.mobile_app.Fragment.LogOutFragment;
import com.example.mobile_app.Fragment.OrderStatusFragment;
import com.example.mobile_app.Fragment.QLDHFragment;
import com.example.mobile_app.GioHang.GioHangTest;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;
import com.example.mobile_app.GioHang.GioHangActivity;
import com.example.mobile_app.User.LoginActivity;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductCallBack {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    private OrderAdapter orderAdapter;
    List<Product> productList;

    Button backl;


    Button Bshopcart;
    private DrawerLayout drawerLayout;
//    public static ArrayList<GioHang> manggiohang;


    /// code gio hang  cua tien


    public static ArrayList<GioHangTest> manggiohang;

    // code cua tien

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcv_Phone);
        drawerLayout= findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        loadFragment(new HomeFragment());
        // Tạo adapter và gán vào recyclerView
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        initMenu();
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Cập nhật menu ngay lúc khởi tạo activity
        updateMenu(navigationView);

        Bshopcart=findViewById(R.id.BShopcart);


        Bshopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, GioHangActivity.class);
                startActivity(it);
            }
        });
        // code gio hang cua tien

        if(manggiohang!=null){

        }else{
            manggiohang=new ArrayList<>();
        }
        //
        if (getIntent().getBooleanExtra("SHOW_ORDER_STATUS", false)) {
            displayOrderStatusFragment();
        }

    }

    void initMenu()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColorFilter(Color.parseColor("#d95a00"), PorterDuff.Mode.SRC_ATOP);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fmNew;
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        fmNew = new HomeFragment();
                        loadFragment(fmNew);
                        return  true;
                    case R.id.nav_mnaccount:
                        fmNew = new InfoFragment();
                        loadFragment(fmNew);
                        return  true;
                    case R.id.nav_lsdh:
                        fmNew= new LSDHFragment();
                        loadFragment(fmNew);
                        return true;
//                    case R.id.nav_qldh:
//                        fmNew= new QLDHFragment();
//                        loadFragment(fmNew);
                    case R.id.nav_ttdh:
                        fmNew= new OrderStatusFragment();
                        loadFragment(fmNew);
                        return true;
                    case R.id.nav_logout:
                        fmNew = new LogOutFragment();
                        loadFragment(fmNew);
                        return  true;
                    case R.id.nav_login: // Assuming that you have a login menu item
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                        return true;

                }
                return true;
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // As this is called every time the menu is displayed, it allows you to update the menu every time it is opened.
        NavigationView navigationView = findViewById(R.id.nav_view);
        updateMenu(navigationView); // Update the visibility of menu items.
        return super.onPrepareOptionsMenu(menu);
    }
    private void updateMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);
        MenuItem loginItem = menu.findItem(R.id.nav_login);
        MenuItem InfoItem = menu.findItem(R.id.nav_mnaccount);
        MenuItem LSDHItem = menu.findItem(R.id.nav_lsdh);
        MenuItem TTDHItem = menu.findItem(R.id.nav_ttdh);
        if (isLoggedIn()) {
            logoutItem.setVisible(true);
            loginItem.setVisible(false);
            InfoItem.setVisible(true);
            LSDHItem.setVisible(true);
			 TTDHItem.setVisible(true);
        } else {
            logoutItem.setVisible(false);
            loginItem.setVisible(true);
            InfoItem.setVisible(false);
            LSDHItem.setVisible(false);
			TTDHItem.setVisible(false);
        }
    }
    void loadFragment(Fragment fmNew)
    {
        FragmentTransaction fmTran= getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.user_fragment, fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();
        // thu nhỏ drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onItemClick(int id) {

    }
    private boolean isLoggedIn() {
        invalidateOptionsMenu();
        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        return preferences.contains("email");

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Kiểm tra xem có cần hiển thị OrderStatusFragment không
        if (intent.getBooleanExtra("SHOW_ORDER_STATUS", false)) {
            displayOrderStatusFragment();
        }
    }
    // Trong MainActivity:
    public void displayOrderStatusFragment() {
        OrderStatusFragment orderStatusFragment = new OrderStatusFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_fragment, orderStatusFragment)
                .commit();
    }

}