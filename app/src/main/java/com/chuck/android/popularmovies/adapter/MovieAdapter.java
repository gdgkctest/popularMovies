package com.chuck.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chuck.android.popularmovies.MovieDetails;
import com.chuck.android.popularmovies.R;
import com.chuck.android.popularmovies.models.MinMovie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    //declare adapter variables
    private List<MinMovie> movies;
    private int rowLayout;
    private Context context;


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Declare ViewHolder variables
        LinearLayout moviesLayout;
        ImageView moviePoster;
        private Context context;


        MovieViewHolder(Context context, View v) {
            super(v);
            //Define Viewholder and set on click listener
            moviesLayout = v.findViewById(R.id.movies_layout);
            moviePoster = v.findViewById(R.id.image_poster);
            this.context = context;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //If Movie Poster is clicked send the current movies ID to the MovieDetails Activity
            int position = getAdapterPosition();
            Intent myIntent = new Intent(context, MovieDetails.class);
            myIntent.putExtra("EXTRA_MOVIE_ID", movies.get(position).getId());
            context.startActivity(myIntent);
        }
    }

    public MovieAdapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(parent.getContext(), view);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //Check if adapter is empty before binding
        if (movies != null) {
            //Add movie title
            String movieTitle = movies.get(position).getTitle();
            //Use Picasso to add movie poster to image view
            String PosterPath = "http://image.tmdb.org/t/p/w342" + movies.get(position).getPosterPath();
            //Place the Movie Poster into the Imageview using Picasso since poster image is from web
            Picasso.get()
                    .load(PosterPath)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(holder.moviePoster);
            //Add ContentDescription for Accessibility
            holder.moviePoster.setContentDescription(movieTitle);
        }
    }

    public void setMovies(List<MinMovie> currentMovies) {
        //After Adapter is initialized add movie object to adapter, when switching movie lists
        this.movies = currentMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //only return item count if movie object is initialized
        if (movies != null)
            return movies.size();
        else
            return 0;
    }


}