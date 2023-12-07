package com.example.mobile_app.API;

import com.example.mobile_app.Model.Apple;
import com.example.mobile_app.Model.Order;
import com.example.mobile_app.Model.OrderHistory;
import com.example.mobile_app.Model.PaymentMethod;
import com.example.mobile_app.Model.Product;
import com.example.mobile_app.Model.QLDH;
import com.example.mobile_app.Model.User;
import com.example.mobile_app.Review.Review;
import com.example.mobile_app.Review.ReviewResponse;
import com.example.mobile_app.User.ApiResponse;
import com.example.mobile_app.GioHang.GioHangResponse;
import com.example.mobile_app.User.LoginResponse;
import com.example.mobile_app.User.RegisterResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
 String BASE_URL = "https://vutt94.io.vn/mobile_shop/api/";

 ;

 @GET("api_allphone.php")
 Call<List<Product>> getProducts();

 //Register
 @FormUrlEncoded
 @POST("api_register.php")
 Call<RegisterResponse> registerUser(
         @Field("email") String email,
         @Field("password") String password,
         @Field("mobile_num") String mobileNum,
         @Field("address") String address,
         @Field("name") String name
 );


 //Login
 @FormUrlEncoded
 @POST("api_login.php")
 Call<LoginResponse> loginUser(
         @Field("email") String email,
         @Field("password") String password
 );

 //dssp
 @GET("api_phone_by_brand/{brand}")
 Call<List<Apple>> getProductsByBrand(@Path("brand") String brand);

 //ctsp
 @GET("api_phone_by_id/{id}")
 Call<List<Product>> getProductDetails(@Path("id") int productId);

 //gio hang

 //lsdh
 //  @GET("api_getorder_confirmed_customer.php")
 @GET("api_getorder_delivered_customer.php")
 Call<List<OrderHistory>> getOrderHistory();

 //thong tin khach hang
 @GET("api_getcustomerinfo.php")
 Call<User> getUserInfo();


 //chinh sua thong tin khach hang
 @FormUrlEncoded
 @POST("api_updatecustomer.php")
 Call<ApiResponse> updateUserInfo(
         @Field("name") String name,
         @Field("address") String address,
         @Field("mobile_num") String mobileNum,
         @Field("password") String password
 );


 // gio hang api thêm sản phẩm vào giỏ hàng
 @FormUrlEncoded
 @POST("api_cart.php")
 Call<GioHangResponse> addToCart(
         @Field("id_phone") int idPhone,
         @Field("quantity") int quantity,
         @Field("add_to_cart") int addToCart
 );

 // giỏ hàng api cập nhật số lượng sản phẩm
 @FormUrlEncoded
 @POST("api_cart.php")
 Call<GioHangResponse> updateCart(
         @Field("id_phone") int idPhone,
         @Field("quantity") int quantity,
         @Field("update_cart") int updateCart
 );


 // giỏ hàng api xóa giỏ hàng
 @FormUrlEncoded
 @POST("api_cart.php")
 Call<GioHangResponse> deleteFromCart(
         @Field("id_phone") int idPhone,
         @Field("delete_from_cart") int deleteFromCart
 );


 //api lấy giỏ hàng
 @GET("api_cart.php")
 Call<Map<String, Product>> retrieveCart(@Query("retrieve_cart") boolean retrieveCart);

 //api lấy phương thức thanh toán
 @GET("api_getpaymethod.php")
 Call<List<PaymentMethod>> getPaymentMethods();

 // Quản lý đơn hàng
 @GET("api_getorder_notconfirmed_customer.php")
 Call<List<QLDH>> getQLDH();

 //Xem tiến trình giao hàng
 @GET("api_getorder_confirmed_customer.php")
 Call<List<Order>> getOrderStatus();

 @GET("api_getorder_notconfirmed_customer.php")
 Call<List<Order>> getNotConfirmedOrders();

 @GET("api_getorder_delivered_customer.php")
 Call<List<Order>> getDeliveredOrders();

 @FormUrlEncoded
 @POST("api_order_add.php")
 Call<ApiResponse> addOrder(
         @Field("id_method") int idMethod
 );

 //Chưa đăng nhập
 @FormUrlEncoded
 @POST("api_order_add_nologin.php")
 Call<ApiResponse> addOrderNoLogin(
         @Field("email") String email,
         @Field("mobile_num") String mobileNum,
         @Field("address") String address,
         @Field("name") String name,
         @Field("id_method") int idMethod
 );

 // xem đơn hàng đã hủy
 @GET("api_getorder_cancelled_customer.php")
 Call<List<Order>> getCancelledOrders();

 @GET("api_getorder_id/{id}")
 Call<Order> getcuthdh(@Path("id") int orderId);

 @GET("api_getorderitems_id/{id}")
 Call<List<Product>> getOrderItemsById(@Path("id") int orderId);


 // giỏ hàng api xóa hóa đơn
 @FormUrlEncoded
 @POST("api_order_delete.php")
 Call<Product> cancelOrder(
         @Field("id") int orderId,
         @Field("delete_from_cart") int deleteFromCart
 );

 // tìm kiếm sản phẩm
 @FormUrlEncoded
 @POST("api_searchphones.php") // Sửa lại endpoint nếu cần
 Call<List<Product>> searchProducts(@Field("encodedQuery") String query);


 //đăng review
 @FormUrlEncoded
 @POST("api_post_review.php")
 Call<ReviewResponse> postReview(
         @Field("score") int rating,
         @Field("comment") String comment,
         @Field("id_phone") int idPhone,
         @Field("id_account") int customerId,
         @Field("customer_name") String customerName
 );
 @GET("api_getFeedback/{id}")
 Call<List<Review>> getReview(@Path("id") int productId);
}


