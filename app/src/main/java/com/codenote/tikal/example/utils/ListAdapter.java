package com.codenote.tikal.example.utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.codenote.tikal.example.R;
import com.codenote.tikal.example.data.Movie;
import com.codenote.tikal.example.layouts.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Movie, RecyclerView.ViewHolder> implements Filterable {
    protected final MovieViewHolder.Listener mListener;

    protected Context mContext;
    protected List<Movie> mMovies = new ArrayList<>();

    public ListAdapter(Context activity, MovieViewHolder.Listener listener) {
        super(new ArrayList<Movie>());
        mContext = activity;
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(items().get(position).id);
    }


    @Override
    public int getItemCount() {
        return items().size();
    }

    protected List<Movie> items() {
        return mMovies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(view, mContext, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie item = items().get(position);
        if (item != null && holder instanceof MovieViewHolder) {
            ((MovieViewHolder) holder).assignItem(item, position);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                return new FilterResults();
            }
        };
    }

    @Override
    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        int objectsCount = getItemCount();
        int accommodationsCount = movies == null ? 0 : movies.size();
        if (movies != null) {
            mMovies.addAll(movies);
        }
        notifyItemRangeInserted(objectsCount, accommodationsCount);
    }


}
