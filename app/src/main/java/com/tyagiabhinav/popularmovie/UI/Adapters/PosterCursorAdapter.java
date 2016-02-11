package com.tyagiabhinav.popularmovie.UI.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tyagiabhinav.popularmovie.DB.MovieContract;
import com.tyagiabhinav.popularmovie.R;

/**
 * Created by abhinavtyagi on 09/02/16.
 */
public class PosterCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = PosterCursorAdapter.class.getSimpleName();

    public static class ViewHolder {
        public final ImageView poster;

        public ViewHolder(View view) {
            poster = (ImageView) view.findViewById(R.id.posterImg);
        }
    }

    public PosterCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_poster_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        final String POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185/";
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_POSTER_PATH))).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(viewHolder.poster);
    }
}
