package com.example.mobile_app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.API.APIClient;

import com.example.mobile_app.Model.User;
import com.example.mobile_app.R;
import com.example.mobile_app.User.ApiResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InfoFragment extends Fragment {
    private TextView  textViewName, textViewEmail, textViewAddress, textViewPhone, textViewPassword;
    private EditText editTextName,editTextAddress,editTextPhone,editTextPassword,editTextRePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewPassword = view.findViewById(R.id.textViewPassword);
        Button btnUpdateInfo = view.findViewById(R.id.btnUpdate);
        // Gọi API để lấy thông tin người dùng
        fetchUserInfo();
        btnUpdateInfo.setOnClickListener(v -> {
            // Lấy thông tin hiện tại của User
            User currentUser = new User();
            currentUser.setName(textViewName.getText().toString());
            currentUser.setAddress(textViewAddress.getText().toString());
            currentUser.setMobile_num(textViewPhone.getText().toString());
            currentUser.setPassword(textViewPassword.getText().toString());
            // Gọi hàm hiển thị dialog cập nhật
            showUpdateDialog(currentUser);
        });
        return view;
    }
    private void fetchUserInfo() {
        Call<User> call = APIClient.getInstance().getMyApi().getUserInfo();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();
                    textViewName.setText(user.getName());
                    textViewEmail.setText(user.getEmail());
                    textViewAddress.setText(user.getAddress());
                    textViewPhone.setText(user.getMobile_num());
                    textViewPassword.setText(user.getPassword());
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
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(User user) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_user_info_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextName = dialogView.findViewById(R.id.editTextName);
        editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        editTextRePassword = dialogView.findViewById(R.id.editTextRePassword);

        // Set current values
        editTextName.setText(user.getName());
        editTextAddress.setText(user.getAddress());
        editTextPhone.setText(user.getMobile_num());
        editTextPassword.setText(user.getPassword());
        editTextRePassword.setText(user.getPassword());

        dialogBuilder.setTitle("Cập nhật thông tin");
        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cập nhật", (dialog, which) -> {
            // Không làm gì ở đây vì chúng ta sẽ ghi đè hành động onclick sau
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy", (dialog, which) -> dialog.dismiss());

        alertDialog.show();

        // Ghi đè hành động onclick để dialog không tự đóng khi có lỗi
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String address = editTextAddress.getText().toString();
            String mobileNum = editTextPhone.getText().toString();
            String password = editTextPassword.getText().toString();
            String rePassword = editTextRePassword.getText().toString();

            if ( checkName(name) && checkAddress(address) && CheckPhone(mobileNum) &&CheckPass(password, rePassword)) {
                user.setName(name);
                user.setAddress(address);
                user.setMobile_num(mobileNum);
                user.setPassword(password);

                Call<ApiResponse> call = APIClient.getInstance().getMyApi().updateUserInfo(name, address, mobileNum, password);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse apiResponse = response.body();
                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            fetchUserInfo();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Lỗi phản hồi từ API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    boolean CheckPass(String passwordR, String repassword){
        if(passwordR.isEmpty()){
            editTextPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if(passwordR.length() <= 5) {
            editTextPassword.setError("Vui lòng nhập trên 6 ký tự");
            return false;  // Đã thêm return false vào đây
        }
        if(repassword.isEmpty()){
            editTextRePassword.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }

        if(!passwordR.equals(repassword)) {
            editTextRePassword.setError("xác nhận mật khẩu không khớp");
            return false;
        }
        return true;
    }
    boolean CheckPhone(String phone) {
        if (phone.length() != 10 || !phone.matches("[0-9]+")) {
            editTextPhone.setError("Số điện thoại phải là 10 chữ số");
            return false;
        }
        return true;
    }
    private boolean checkName(String name) {
        if (name.isEmpty()) {
            editTextName.setError("Vui lòng nhập tên");
            return false;
        }
        return true;
    }

    private boolean checkAddress(String address) {
        if (address.isEmpty()) {
            editTextAddress.setError("Vui lòng nhập địa chỉ");
            return false;
        }
        return true;
    }
}