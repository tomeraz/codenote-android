package com.codenote.tikal.example.layouts;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codenote.tikal.example.R;
import com.codenote.tikal.example.data.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private static final String IMAGE_BASE_URL_SMALL = "http://image.tmdb.org/t/p/w185";
    private final Context mContext;
    private final MovieViewHolder.Listener mListener;
    @Bind(R.id.image)
    public ImageView mImageView;
    @Bind(android.R.id.title)
    public TextView mTitleView;
    private Movie mItem;
    private int mPosition;

    public MovieViewHolder(View v, Context context, MovieViewHolder.Listener listener) {
        super(v);
        mListener = listener;
        ButterKnife.bind(this, v);

        mContext = context;
    }

    public void assignItem(final Movie item, final int position) {
        mPosition = position;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMovieClick(item, position);
            }
        });
        mItem = item;
        Picasso.with(mContext)
                .load(IMAGE_BASE_URL_SMALL + mItem.poster_path)
                .fit().centerCrop()

                .into(mImageView);

        mTitleView.setText(mItem.title);
    }

    public interface Listener {
        void onMovieClick(Movie acc, int position);
    }

}