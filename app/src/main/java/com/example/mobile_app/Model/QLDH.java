package com.example.mobile_app.Model;

import java.util.Date;




public class QLDH {
    /// code cua tien

    private int id;
    private Date date;
    private String status;

    private Date date_ship;

    private int total;
    private int id_account;
    private  int id_payment;

    private int id_sale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate_ship() {
        return date_ship;
    }

    public void setDate_ship(Date date_ship) {
        this.date_ship = date_ship;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public int getId_payment() {
        return id_payment;
    }

    public void setId_payment(int id_payment) {
        this.id_payment = id_payment;
    }

    public int getId_sale() {
        return id_sale;
    }

    public void setId_sale(int id_sale) {
        this.id_sale = id_sale;
    }

    public QLDH(int id, Date date, String status, Date date_ship) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.date_ship = date_ship;
    }
}

