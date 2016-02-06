package com.tyagiabhinav.popularmovie.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.R;

import java.util.ArrayList;

/**
 * Created by abhinavtyagi on 04/02/16.
 */
public class PosterAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();
//    private Context context;
//    private List<Movie> movieList;


    public PosterAdapter(Context context, ArrayList<Movie> movies) {
        super(context,0,movies);
//        this.context = context;
//        this.movieList = movies;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_poster_item, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.moviePoster = (ImageView) convertView.findViewById(R.id.posterImg);
            convertView.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.moviePoster.setImageResource(getItem(position).getIcon());
        Picasso.with(getContext()).load(getItem(position).getPosterPath()).placeholder(R.mipmap.ic_launcher).into(holder.moviePoster);

        return convertView;
    }

    static class ViewHolder {
        ImageView moviePoster;
    }
}
