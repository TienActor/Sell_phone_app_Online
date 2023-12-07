package com.example.mobile_app.Model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private int id;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("date_ship")
    private String dateShip;

    @SerializedName("total")
    private int total;

    @SerializedName("discounted")
    private int discounted;

    @SerializedName("final_total")
    private int finalTotal;

    @SerializedName("id_account")
    private int idAccount;

    @SerializedName("id_payment")
    private int idPayment;

    @SerializedName("id_sale")
    private Integer idSale;

    @SerializedName("method_name")
    private String methodName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateShip() {
        return dateShip;
    }

    public void setDateShip(String dateShip) {
        this.dateShip = dateShip;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscounted() {
        return discounted;
    }

    public void setDiscounted(int discounted) {
        this.discounted = discounted;
    }

    public int getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(int finalTotal) {
        this.finalTotal = finalTotal;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

