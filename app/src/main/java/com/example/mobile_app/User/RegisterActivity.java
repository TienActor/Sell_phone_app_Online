package com.example.mobile_app.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Model.User;
import com.example.mobile_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Api api = APIClient.getInstance().getMyApi();

    Button register;
    EditText eduserName, edpassword, edphone, edrepassword, edemail,edAdd;
    ImageButton regback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        api = APIClient.getInstance().getMyApi();
        register = findViewById(R.id.btRegister);
        eduserName = findViewById(R.id.edUserNameRe);
        edpassword = findViewById(R.id.edPassword);
        edphone = findViewById(R.id.edPhone);
        edrepassword = findViewById(R.id.edRePassword);
        edemail = findViewById(R.id.edEmail);
        edAdd =findViewById(R.id.edAdd);
        regback=findViewById(R.id.ibReg);
        regback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            registerUser();
        }
    });
    }
    // Trong RegisterActivity
    private void registerUser() {
        String emailR = edemail.getText().toString().trim();
        String passwordR = edpassword.getText().toString().trim();
        String repassword = edrepassword.getText().toString().trim();
        String phoneR = edphone.getText().toString().trim();
        String addressR = edAdd.getText().toString().trim();
        String usernameR = eduserName.getText().toString().trim();

        if (!checkEmailR(emailR) ||
                !CheckPass(passwordR, repassword) ||
                !checkPhoneR(phoneR) ||
                !checkAddressR(addressR) ||
                !checkUsernameR(usernameR)) {
            return; // Dừng việc gọi API nếu có lỗi
        }

        User userObj = new User(emailR, passwordR, phoneR, addressR, usernameR);
        Call<RegisterResponse> call = api.registerUser(
                userObj.getEmail(),
                userObj.getPassword(),
                userObj.getMobile_num(),
                userObj.getAddress(),
                userObj.getName()
        );

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();

                    if ("Đã có tài khoản với email này".equals(message)) {
                        Toast.makeText(RegisterActivity.this, "Đã có tài khoản với email này", Toast.LENGTH_SHORT).show();
                    } else if ("Đăng ký không thành công".equals(message)) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                } else {
                    // Xử lý trường hợp phản hồi không thành công từ máy chủ
                    Toast.makeText(RegisterActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối", Toast.LENGTH_LONG).show();
            }
        });
    }
    boolean CheckPass(String passwordR, String repassword){
        if(passwordR.isEmpty()){
            edpassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if(passwordR.length() <= 5) {
            edpassword.setError("Vui lòng nhập trên 6 ký tự");
            return false;  // Đã thêm return false vào đây
        }
        if(repassword.isEmpty()){
            edrepassword.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }

        if(!passwordR.equals(repassword)) {
            edrepassword.setError("xác nhận mật khẩu không khớp");
            return false;
        }
        return true;
    }
    boolean checkEmailR(String emailR){
        if(emailR.isEmpty()){
            edemail.setError("Vui lòng nhập email");
            return false;
        }

        // Sử dụng biểu thức chính quy để kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@gmail.com$";
        if (!emailR.matches(emailRegex)) {
            edemail.setError("Email không hợp lệ");
            return false;
        }

        return true;
    }

    boolean checkPhoneR(String phoneR){
        if(phoneR.isEmpty()){
            edphone.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        return true;
    }boolean checkUsernameR(String usernameR){
        if (usernameR.isEmpty()) {
            eduserName.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }
        return true;
    }
    boolean checkAddressR(String addR){
        if(addR.isEmpty()){
            edAdd.setError("Vui lòng nhập địa chỉ");
            return false;
        }
        return true;
}
}