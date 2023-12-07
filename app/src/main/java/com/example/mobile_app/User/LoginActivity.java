package com.example.mobile_app.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app.API.APIClient;
import com.example.mobile_app.API.Api;
import com.example.mobile_app.Home.MainActivity;
import com.example.mobile_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btLoginC;
    TextView btRegister;
    EditText edemail,edPasswordC;
    private Api api = APIClient.getInstance().getMyApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = APIClient.getInstance().getMyApi();
        edemail=findViewById(R.id.edEmail);
        edPasswordC=findViewById(R.id.edPass);
        btLoginC=findViewById(R.id.btLogin);
        btRegister=findViewById(R.id.btregisterss);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        btLoginC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
    private void loginUser() {
        String email = edemail.getText().toString();
        String password = edPasswordC.getText().toString();

        if (!checkEmail(email) || !checkPassword(password)) {
            return; // Dừng việc gọi API nếu có lỗi
        }

        Call<LoginResponse> call = api.loginUser(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    if ("Sai email hoặc mật khẩu".equals(message)) {
                        Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        saveLoginStatus(email);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean checkEmail(String emailR){
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

    boolean checkPassword(String password){
        if(password.isEmpty()){
            edPasswordC.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        return true;
    }
    public void saveLoginStatus(String email) {
        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", email);
        editor.apply();
    }

}