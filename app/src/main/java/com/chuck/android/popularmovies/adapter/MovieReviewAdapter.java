package com.chuck.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuck.android.popularmovies.R;
import com.chuck.android.popularmovies.models.MovieReview;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {
    private List<MovieReview> moviesReviews;

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        LinearLayout moviesLayout;
        TextView reviewText;
        TextView reviewAuthor;
        private final Context context;

        MovieReviewViewHolder(Context context, View v) {
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
        return new MovieReviewViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieReviewViewHolder holder, int position) {
        //check if review object is null before binding
        if (moviesReviews != null) {
            //Bind author name and review text
            holder.reviewText.setText(moviesReviews.get(position).getContent());
            holder.reviewAuthor.setText(moviesReviews.get(position).getAuthor());
        }

    }

    public void setMovies(List<MovieReview> currentMovieReviews) {
        //allows list to be set after adapter is initialized
        moviesReviews = currentMovieReviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //check if review is empty
        if (moviesReviews != null)
            return moviesReviews.size();
        else
            return 0;
    }

}
