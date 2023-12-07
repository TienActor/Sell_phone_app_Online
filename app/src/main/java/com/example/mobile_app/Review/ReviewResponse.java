package com.example.mobile_app.Review;

public class ReviewResponse {
    private boolean success;
    private String message;

    public ReviewResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
