package com.example.mobile_app.API;

import com.example.mobile_app.Model.Product;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static APIClient instance = null;
    private final Api myApi;

    private APIClient() {
        // Tạo một OkHttpClient với HostnameVerifier tùy chỉnh
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);




        // Sử dụng OkHttpClient khi khởi tạo Retrofit



        // Tạo một CookieJar để lưu trữ cookie
        CookieJar cookieJar = new CookieJar() {
            private final List<Cookie> cookiesStore = new ArrayList<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookiesStore.addAll(cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return cookiesStore;
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi = retrofit.create(Api.class);

    }

    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }

}