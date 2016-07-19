package com.codenote.tikal.example.layouts;

import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codenote.tikal.example.R;
import com.codenote.tikal.example.client.DiscoverResponse;
import com.codenote.tikal.example.client.RetrofitCallback;
import com.codenote.tikal.example.data.DbContract;
import com.codenote.tikal.example.data.Movie;
import com.codenote.tikal.example.data.MoviesJobService;
import com.codenote.tikal.example.utils.EndlessRecyclerView;
import com.codenote.tikal.example.utils.ListAdapter;
import com.squareup.okhttp.ResponseBody;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int JOB_ID = 1;
    @Bind(android.R.id.list)
    EndlessRecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private JobScheduler mJobScheduler;
    private LinearLayoutManager mLayoutManager;

    private int page;
    private ListAdapter mAdapter;
    private RetrofitCallback<DiscoverResponse> mResultsCallback = new RetrofitCallback<DiscoverResponse>() {
        @Override
        protected void failure(ResponseBody response, boolean isOffline) {
            return;
        }

        @Override
        protected void success(DiscoverResponse discoverResponse, Response<DiscoverResponse> response) {
            mAdapter.addMovies(discoverResponse.results);
            mRefresh.setRefreshing(false);
        }

    };

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setOnLoadMoreListener(new EndlessRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadSearchResults(page++);
            }
        });
        mJobScheduler = JobScheduler.getInstance(getActivity());
        constructJob();
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeView();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        page = 1;
        if (isOnline()) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mAdapter = new ListAdapter(getActivity(), (MovieViewHolder.Listener) getActivity());
            mRecyclerView.init(mLayoutManager, mAdapter, 4);
            mRecyclerView.setHasMoreData(true);
            ((MainActivity) getActivity()).getApi().getMovies(page++).enqueue(mResultsCallback);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mAdapter = new ListAdapter(getActivity(), (MovieViewHolder.Listener) getActivity());
            mRecyclerView.init(mLayoutManager, mAdapter, 4);
            mRecyclerView.setHasMoreData(true);
            getActivity().getSupportLoaderManager().initLoader(1, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri CONTENT_URI = DbContract.Movies.CONTENT_URI;
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, null);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void loadSearchResults(int offset) {
        try {
            ((MainActivity) getActivity()).getApi().getMovies(offset).enqueue(mResultsCallback);
        } catch (InvalidParameterException e) {
            getActivity().finish();
        }
    }

    private void constructJob() {
        // TODO: customize to job as the requirements
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(getActivity(), MoviesJobService.class));
        builder.setOverrideDeadline(3600000 * 4)// max every 4 hours
                .setMinimumLatency(3600000)// max every 1 hours
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);


        mJobScheduler.schedule(builder.build());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        cursor.moveToFirst();
        List<Movie> results = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            results.add(new Movie(
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.OVERVIEW)),
                    Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.IS_ADULT))),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.RELEASE_DATE)),
                    Integer.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.MOVIE_ID))),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.ORIGINAL_LANGUAGE)),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.TITLE)),
                    Float.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.POPULARITY))),
                    0, false,
                    Float.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.MoviesColumns.VOTE_AVERAGE)))
            ));
            cursor.moveToNext();
        }
        mAdapter.addMovies(results);
        mRecyclerView.setHasMoreData(false);
        mRefresh.setRefreshing(false);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the Cursor is being placed in a CursorAdapter, you should use the
        // swapCursor(null) method to remove any references it has to the
        // Loader's data.
    }
}
