package com.codenote.tikal.example.client;

import android.util.Log;

/**
 * @author ortal
 * @date 2015-10-02
 */
public class RetrofitLogger implements HttpLoggingInterceptor.Logger {

    public static HttpLoggingInterceptor create() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;//: HttpLoggingInterceptor.Level.BASIC;
        return new HttpLoggingInterceptor(new RetrofitLogger(), level);
    }

    @Override
    public void log(String message) {
        Log.d("RetrofitLogger", message);
    }
}
