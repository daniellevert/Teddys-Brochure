package com.group17.teddysbrochure.ui.reviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.group17.teddysbrochure.R;

public class CreateReviewDialog extends AppCompatDialogFragment {

    private EditText reviewTitleInput;
    private EditText reviewerInput;
    private EditText reviewInput;
    private EditText starInput;
    private ReviewDialogListener reviewListener;
    private ReviewsFragment context;

    public CreateReviewDialog (ReviewsFragment context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.review_form, null);

        builder.setView(view)
                .setTitle("Create Review Form")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reviewTitle = reviewTitleInput.getText().toString();
                        String reviewer = reviewerInput.getText().toString();
                        String review = reviewInput.getText().toString();
                        String star = starInput.getText().toString();
                        reviewListener.applyTexts(reviewTitle, reviewer, review, star);
                    }
                });

        reviewTitleInput = (EditText) view.findViewById(R.id.reviewTitleEditText);
        reviewerInput = (EditText) view.findViewById(R.id.reviewerEditText);
        reviewInput = (EditText) view.findViewById(R.id.reviewEditText);
        starInput = (EditText) view.findViewById(R.id.starEditText);

        try {
            reviewListener = (ReviewDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.create();
    }

    public interface ReviewDialogListener {
        void applyTexts(String reviewTitle, String reviewer, String review, String star);
    }
}
