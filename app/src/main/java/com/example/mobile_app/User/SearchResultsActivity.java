package com.example.mobile_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mobile_app.Adapter.ProductAdapter;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Product> searchResults;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        back = findViewById(R.id.backtt);
        recyclerView = findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Lấy danh sách sản phẩm từ Intent
        searchResults = (ArrayList<Product>) getIntent().getSerializableExtra("SEARCH_RESULTS");
        productAdapter = new ProductAdapter(this, searchResults);
        recyclerView.setAdapter(productAdapter);
    }

    private void performSearch(String query) {
        // TODO: Replace with your actual search logic
        // For example, call your API and update the searchResults list
        // Then notify the adapter
        // productAdapter.notifyDataSetChanged();
    }
}