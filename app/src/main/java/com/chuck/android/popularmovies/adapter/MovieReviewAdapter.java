package com.chuck.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuck.android.popularmovies.MovieDetails;
import com.chuck.android.popularmovies.R;
import com.chuck.android.popularmovies.models.MinMovie;
import com.chuck.android.popularmovies.models.MovieReview;
import com.chuck.android.popularmovies.models.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {
    private List<MovieReview> moviesReviews;
    //private Context context;

//    public MovieReviewAdapter(Context context) {
//        this.context = context;
//    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        LinearLayout moviesLayout;
        TextView reviewText;
        TextView reviewAuthor;
        private final Context context;

        MovieReviewViewHolder(Context context,View v) {
            super(v);
            //Define Viewholder
            moviesLayout = v.findViewById(R.id.reviews_layout);
            reviewText = v.findViewById(R.id.review_text);
            reviewAuthor = v.findViewById(R.id.reviewer_name);
            this.context = context;
        }
    }
    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_list_item, parent, false);
            return new MovieReviewViewHolder(parent.getContext(),view);    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieReviewViewHolder holder, int position) {
        // review.getContent() = review_text
        // review.getAuthor() = reviewer_name

        if (moviesReviews != null) {
            holder.reviewText.setText(moviesReviews.get(position).getContent());
            holder.reviewAuthor.setText(moviesReviews.get(position).getAuthor());
            }

    }
        public void setMovies (List < MovieReview > currentMovieReviews) {
            moviesReviews = currentMovieReviews;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount () {
            if (moviesReviews != null)
                return moviesReviews.size();
            else
                return 0;
        }

}
