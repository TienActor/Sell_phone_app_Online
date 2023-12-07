package com.example.mobile_app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


import com.example.mobile_app.Home.MainActivity;
import com.example.mobile_app.R;
import com.example.mobile_app.User.LoginActivity;


public class LogOutFragment extends Fragment {
    private Button btnConfirm;
    private Button btnCancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_out, container, false);

        btnConfirm = view.findViewById(R.id.btn_logout_confirm);
        btnCancel = view.findViewById(R.id.btn_logout_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                Intent i=new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getFragmentManager().popBackStack();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đóng fragment và quay lại màn hình trước
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
    private void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFO", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Xóa thông tin đăng nhập
        editor.remove("email");
        editor.apply();
    }
}