package com.codenote.tikal.example.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author ortal
 * @date 2015-07-13
 */
public class DbContract {
    public static final String CONTENT_AUTHORITY = "com.popular.movies.ortal.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_MOVIES = "movies";


    interface Tables {
        String TABLE_MOVIES = "movies";
    }

    public interface MoviesColumns extends BaseColumns {
        String MOVIE_ID = "id";
        String IS_ADULT = "is_adult";
        String ORIGINAL_LANGUAGE = "original_lang";
        String TITLE = "original_title";
        String OVERVIEW = "original_overview";
        String RELEASE_DATE = "release_date";
        String VOTE_AVERAGE = "vote_average";
        String POSTER_PATH = "poster_path";
        String POPULARITY = "popularity";

    }


    public static class Movies implements MoviesColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/int";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
