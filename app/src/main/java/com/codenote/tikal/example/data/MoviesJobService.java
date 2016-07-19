package com.codenote.tikal.example.data;


import android.content.ContentValues;

import com.codenote.tikal.example.BuildConfig;
import com.codenote.tikal.example.client.Api;
import com.codenote.tikal.example.client.ApiConfig;
import com.codenote.tikal.example.client.DefaultHttpClient;
import com.codenote.tikal.example.client.DiscoverResponse;
import com.codenote.tikal.example.client.RetrofitCallback;
import com.codenote.tikal.example.client.RetrofitLogger;
import com.squareup.okhttp.ResponseBody;

import java.util.concurrent.TimeUnit;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;
import retrofit.Response;


public class MoviesJobService extends JobService {
    private Api mApi;
    private RetrofitCallback<DiscoverResponse> mResultsCallback = new RetrofitCallback<DiscoverResponse>() {
        @Override
        protected void failure(ResponseBody response, boolean isOffline) {
            return;
        }

        @Override
        protected void success(DiscoverResponse discoverResponse, Response<DiscoverResponse> response) {
            getBaseContext().getContentResolver().delete(DbContract.Movies.CONTENT_URI, null, null);

            for (Movie movie : response.body().results) {
                ContentValues values = new ContentValues();

                values.put(DbContract.MoviesColumns.MOVIE_ID, movie.id);
                values.put(DbContract.MoviesColumns.IS_ADULT, movie.adult);
                values.put(DbContract.MoviesColumns.ORIGINAL_LANGUAGE, movie.original_language);
                values.put(DbContract.MoviesColumns.TITLE, movie.title);
                values.put(DbContract.MoviesColumns.OVERVIEW, movie.overview);
                values.put(DbContract.MoviesColumns.RELEASE_DATE, movie.release_date);
                values.put(DbContract.MoviesColumns.VOTE_AVERAGE, movie.vote_average);
                values.put(DbContract.MoviesColumns.POSTER_PATH, movie.poster_path);
                values.put(DbContract.MoviesColumns.POPULARITY, movie.popularity);

                getBaseContext().getContentResolver().insert(DbContract.Movies.CONTENT_URI, values);
            }
        }

    };

    public MoviesJobService() {
        initApi();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mApi.getMovies(1).enqueue(mResultsCallback);
        jobFinished(jobParameters, false);
        return false;
    }

    private void initApi() {//todo move to application
        if (mApi == null) {
            ApiConfig cfg = new ApiConfig();
            cfg.setDebug(BuildConfig.DEBUG);
            cfg.setLogger(new RetrofitLogger());
            DefaultHttpClient httpClient = new DefaultHttpClient(this);
            httpClient.setConnectTimeout(30000, TimeUnit.MILLISECONDS);
            httpClient.setReadTimeout(30000, TimeUnit.MILLISECONDS);
            httpClient.interceptors().add(RetrofitLogger.create());
            mApi = new Api(cfg, httpClient);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
