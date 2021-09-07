package com.group17.teddysbrochure.ui.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.group17.teddysbrochure.R;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> reviewTitles;
    List<String> reviewers;
    List<String> reviewStars;
    List<String> reviewTexts;

    public ReviewAdapter (Context c, List<String> reviewTitles, List<String> reviewers, List<String> reviewStars, List<String> reviewTexts) {
        super(c, R.layout.review, R.id.reviewTitle, reviewTitles);
        this.context = c;
        this.reviewTitles = reviewTitles;
        this.reviewStars = reviewStars;
        this.reviewTexts = reviewTexts;
        this.reviewers = reviewers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View review = layoutInflater.inflate(R.layout.review, parent, false);
        TextView reviewTitle = review.findViewById(R.id.reviewTitle);
        TextView reviewer = review.findViewById(R.id.reviewer);
        TextView reviewStar = review.findViewById(R.id.reviewStar);
        TextView reviewText = review.findViewById(R.id.reviewText);

        //set current data in position in view
        reviewTitle.setText(reviewTitles.get(position));
        reviewStar.setText(reviewStars.get(position));
        reviewText.setText(reviewTexts.get(position));
        reviewer.setText(reviewers.get(position));

        return review;
    }
}
