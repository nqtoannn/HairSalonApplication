package com.example.hairsalon.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hairsalon.R;
import com.example.hairsalon.model.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(@NonNull Context context, @NonNull List<Review> reviewList) {
        super(context, 0, reviewList);
        this.mContext = context;
        this.mReviewList = reviewList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
        }

        Review currentReview = mReviewList.get(position);

        TextView userTextView = listItem.findViewById(R.id.text_comment_user);
        userTextView.setText(currentReview.getCustomerName());

        TextView contentTextView = listItem.findViewById(R.id.text_comment_content);
        contentTextView.setText(currentReview.getComment());

        ImageView star1ImageView = listItem.findViewById(R.id.star1);
        ImageView star2ImageView = listItem.findViewById(R.id.star2);
        ImageView star3ImageView = listItem.findViewById(R.id.star3);
        ImageView star4ImageView = listItem.findViewById(R.id.star4);
        ImageView star5ImageView = listItem.findViewById(R.id.star5);

        // Set tint color for star images
        setStarColor(star1ImageView, 1, currentReview.getRating());
        setStarColor(star2ImageView, 2, currentReview.getRating());
        setStarColor(star3ImageView, 3, currentReview.getRating());
        setStarColor(star4ImageView, 4, currentReview.getRating());
        setStarColor(star5ImageView, 5, currentReview.getRating());

        TextView dateTextView = listItem.findViewById(R.id.text_comment_date);
        dateTextView.setText("Thời gian: " +currentReview.getReviewDate());

        return listItem;
    }

    private void setStarColor(ImageView imageView, int starPosition, int rating) {
        if (starPosition <= rating) {
            // If star position is less than or equal to rating, set yellow color
            imageView.setColorFilter(Color.YELLOW);
        } else {
            // Otherwise, set gray color
            imageView.setColorFilter(Color.GRAY);
        }
    }
}
