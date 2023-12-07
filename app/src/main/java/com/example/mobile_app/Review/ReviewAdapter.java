package com.example.mobile_app.Review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.userNameTextView.setText(review.getCustomerName());
        holder.ratingBar.setRating(review.getScore());
        holder.commentTextView.setText(review.getComment());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public RatingBar ratingBar;
        public TextView commentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }
}