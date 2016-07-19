package com.codenote.tikal.example.client;

import android.content.Context;

import com.squareup.okhttp.ResponseBody;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public abstract class RetrofitCallback<T> implements Callback<T> {
    protected WeakReference<Context> mWeakContext;

    public void attach(Context context) {
        mWeakContext = new WeakReference<>(context);
    }

    public void detach() {
        mWeakContext.clear();
    }

    protected abstract void failure(ResponseBody errorBody, boolean isOffline);

    protected abstract void success(T body, Response<T> response);

    protected void notifyFailure(Throwable exception) {

    }


    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            success(response.body(), response);
        } else {
//            notifyFailure(new RetrofitError(response));
            failure(response.errorBody(), false);

        }
    }

    @Override
    public void onFailure(Throwable t) {


        notifyFailure(t);

    }


}
