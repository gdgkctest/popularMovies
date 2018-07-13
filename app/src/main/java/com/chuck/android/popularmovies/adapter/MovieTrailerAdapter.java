package com.chuck.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuck.android.popularmovies.R;
import com.chuck.android.popularmovies.models.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {
    private List<MovieTrailer> moviesTrailers;
    //private Context context;

//    public MovieTrailerAdapter(Context context) {
//        this.context = context;
//    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout moviesLayout;
        ImageView videoPoster;
        TextView movieLinkDescription;
        private Context context;

        MovieTrailerViewHolder(Context context, View v) {
            super(v);
            //Define Viewholder
            moviesLayout = v.findViewById(R.id.trailers_layout);
            videoPoster = v.findViewById(R.id.video_poster);
            movieLinkDescription = v.findViewById(R.id.video_link);
            this.context = context;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //check if there is a populated trailer object
            if (moviesTrailers != null) {
                //Links trailer object to web or youtube(if installed)
                int position = getAdapterPosition();
                String youTubeShortCode = moviesTrailers.get(position).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/" + youTubeShortCode));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, context.getString(R.string.youtube_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_list_item, parent, false);
        return new MovieTrailerViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        //holder.videoPoster = use piccasso with https://img.youtube.com/vi/youTubeShortCode/mqdefault.jpg;
        //holder.movieLink movieTrailers.getkey;

        if (moviesTrailers != null) {
            //Get youtube id
            String youTubeShortCode = moviesTrailers.get(position).getKey();
            String trailerName = moviesTrailers.get(position).getName();
            //Set trailer description
            holder.movieLinkDescription.setText(trailerName);
            //Create trailer thumbnail image
            String posterPath = "https://img.youtube.com/vi/" + youTubeShortCode + "/mqdefault.jpg";
            Picasso.get()
                    .load(posterPath)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(holder.videoPoster);
            //Add ContentDescription for Accessibility
            holder.videoPoster.setContentDescription(trailerName);
        }

    }

    public void setMovies(List<MovieTrailer> currentMovieTrailers) {
        moviesTrailers = currentMovieTrailers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (moviesTrailers != null)
            return moviesTrailers.size();
        else
            return 0;
    }
}
