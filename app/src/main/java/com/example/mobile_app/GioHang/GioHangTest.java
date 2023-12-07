package com.example.mobile_app.GioHang;

public class GioHangTest {

    public int Id;

    public String Namesp;

    public int Pricesp;

    public String Imgsp;

    public int Quantitysp;

    private boolean isDiscountApplied;

    public GioHangTest(int id, String namesp, int pricesp, String imgsp, int quantitysp) {
        Id = id;
        Namesp = namesp;
        Pricesp = pricesp;
        Imgsp = imgsp;
        Quantitysp = quantitysp;
    }

    public GioHangTest(int id, String namesp, String imgsp, int quantitysp) {
        Id = id;
        Namesp = namesp;
        Imgsp = imgsp;
        Quantitysp = quantitysp;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNamesp() {
        return Namesp;
    }

    public void setNamesp(String namesp) {
        Namesp = namesp;
    }

    public int getPricesp() {
        return Pricesp;
    }

    public void setPricesp(int pricesp) {
        Pricesp = pricesp;
    }

    public String getImgsp() {
        return Imgsp;
    }

    public void setImgsp(String imgsp) {
        Imgsp = imgsp;
    }

    public int getQuantitysp() {
        return Quantitysp;
    }

    public void setQuantitysp(int quantitysp) {
        Quantitysp = quantitysp;
    }

    public boolean isDiscountApplied() {
        return isDiscountApplied;
    }

    public void setDiscountApplied(boolean discountApplied) {
        isDiscountApplied = discountApplied;
    }
}
