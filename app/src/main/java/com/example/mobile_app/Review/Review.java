package com.example.mobile_app.Review;

import java.io.Serializable;
public class Review implements Serializable {
    private int score;
    private String comment;
    private int idPhone;
    private int customerId;
    private String customerName;

    public Review(int score, String comment, int idPhone, int customerId, String customerName) {
        this.score = score;
        this.comment = comment;
        this.idPhone = idPhone;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getter và Setter cho score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Getter và Setter cho comment
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter và Setter cho idPhone
    public int getIdPhone() {
        return idPhone;
    }

    public void setIdPhone(int idPhone) {
        this.idPhone = idPhone;
    }

    // Getter và Setter cho customerId
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // Getter và Setter cho customerName
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
