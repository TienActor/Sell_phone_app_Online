package com.example.mobile_app.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.Fragment.OrderStatusFragment;
import com.example.mobile_app.GioHang.GioHangActivity;
import com.example.mobile_app.GioHang.GioHangTest;
import com.example.mobile_app.Home.MainActivity;
import com.example.mobile_app.Model.Order;
import com.example.mobile_app.Model.PaymentMethod;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.Model.User;
import com.example.mobile_app.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatHangActivity extends AppCompatActivity implements DatHangAdapter.DatHangCallBack{
    private TextView  textViewName,textViewAddress, textViewPhone, textViewEmail;
    RecyclerView rvListCode;
    ArrayList<GioHangTest> lstPro;
    EditText voucher;
    Button apply,confirm;
    ImageButton back;
    DatHangAdapter productAdapter;
    static TextView tongtienTT;
    private RadioGroup paymentMethodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);
        // Initialize the view components
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        rvListCode = findViewById(R.id.rvList3);
        tongtienTT = findViewById(R.id.payment_total_amount);
        confirm = findViewById(R.id.payment_confirm_button);
        back = findViewById(R.id.backtt);
        textViewName = findViewById(R.id.payment_tennguoidung);
        textViewAddress = findViewById(R.id.payment_diachi);
        textViewPhone = findViewById(R.id.payment_sdt);
        textViewEmail = findViewById(R.id.payment_email);
        fetchUserInfo();
        // Set up back button listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
            }
        });
        // Retrieve the cart items passed from GioHangActivity
        Intent intent = getIntent();
        ArrayList<Product> productList = (ArrayList<Product>) getIntent().getSerializableExtra("giohang");
        if(productList != null) {
            productAdapter = new DatHangAdapter(this, productList, this);
            rvListCode.setLayoutManager(new LinearLayoutManager(this));
            rvListCode.setAdapter(productAdapter);
        } else {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_LONG).show();
            // Handle empty cart scenario
        }

        // Update total amount
        EventUtil(productList);

        // Fetch payment methods from the API
        fetchPaymentMethods();

    }

    private void fetchPaymentMethods() {
        APIClient.getInstance().getMyApi().getPaymentMethods().enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, Response<List<PaymentMethod>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populatePaymentMethods(response.body());
                } else {
                    Toast.makeText(DatHangActivity.this, "Error: Unable to get payment methods", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                Toast.makeText(DatHangActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void EventUtil(ArrayList<Product> productList) {
        int totalAmount = 0;
        for (Product product : productList) {
            totalAmount += product.getPrice() * product.getQuatity();
        }
        // Định dạng số thành chuỗi định dạng tiền tệ, ví dụ: "1,000,000"
        String formattedTotal = NumberFormat.getNumberInstance(Locale.US).format(totalAmount);
        tongtienTT.setText(formattedTotal + " Đ");
    }

    private void populatePaymentMethods(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod method : paymentMethods) {
            RadioButton radioButton = new RadioButton(DatHangActivity.this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(method.getName());
            radioButton.setTag(method.getId()); // Lưu trữ id trong tag của RadioButton
            paymentMethodGroup.addView(radioButton);
        }
    }
    private void fetchUserInfo() {
        Call<User> call = APIClient.getInstance().getMyApi().getUserInfo();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();
                    textViewName.setText(user.getName());
                    textViewAddress.setText(user.getAddress());
                    textViewPhone.setText(user.getMobile_num());
                    textViewEmail.setText(user.getEmail());
                }else {
                    try {
                        int statusCode = response.code();
                        Log.e("API_ERROR", "HTTP Status Code: " + statusCode);
                        Log.e("API_ERROR", "Response Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(DatHangActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void submitOrder() {
        // Lấy thông tin phương thức thanh toán được chọn
        int selectedMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        RadioButton paymentMethodButton = findViewById(selectedMethodId);
        int idMethod = getIdMethodFromButton(paymentMethodButton); // Phương thức này cần được viết để chuyển đổi từ tên phương thức thanh toán sang id của nó

        // Tạo đối tượng Order và đặt giá trị cho nó
        Order order = new Order();
        // ... đặt các giá trị cho order từ thông tin người dùng đã nhập

        // Gửi thông tin đơn hàng lên API
        APIClient.getInstance().getMyApi().addOrder(idMethod).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xử lý khi nhận được phản hồi thành công từ API
                    ApiResponse apiResponse = response.body();
                    Toast.makeText(DatHangActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    if(apiResponse.getStatus() == 5) {
                        // Đặt hàng thành công
                        showSuccessDialog();
                    } else {
                        // Đặt hàng thất bại
                        Toast.makeText(DatHangActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(DatHangActivity.this, "Lỗi khi đặt hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Xử lý khi không thể gửi yêu cầu lên API (ví dụ: không có kết nối mạng)
                Toast.makeText(DatHangActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Trong DatHangActivity:
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đặt Hàng Thành Công")
                .setMessage("Cảm ơn bạn đã đặt hàng !!!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Gửi Intent trở lại MainActivity với thông tin bổ sung
                        Intent intent = new Intent(DatHangActivity.this, MainActivity.class);
                        intent.putExtra("SHOW_ORDER_STATUS", true); // Đánh dấu để MainActivity biết cần hiển thị OrderStatusFragment
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Cờ này sẽ đảm bảo rằng MainActivity hiện tại sẽ được tái sử dụng
                        startActivity(intent);
                        finish(); // Kết thúc DatHangActivity
                    }
                })
                .setIcon(R.drawable.baseline_check_24)
                .show();
    }



    private int getIdMethodFromButton(RadioButton paymentMethodButton) {
        return (int) paymentMethodButton.getTag(); // Ép kiểu tag để lấy id
    }
    @Override
    public void onItemLongClicked(Product product) {

    }
}
