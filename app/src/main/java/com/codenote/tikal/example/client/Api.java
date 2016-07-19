package com.codenote.tikal.example.client;

import android.util.ArrayMap;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.Reference;
import java.security.InvalidParameterException;
import java.util.Map;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

public class Api {


    public static final String PATH_MOVIE = "/3/movie/{movie_id}";
    private OkHttpClient mHttpClient;

    private ApiConfig mConfig;

    private Interceptor mRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            HttpUrl url = request.httpUrl().newBuilder()
                    .addQueryParameter("api_key", mConfig.getApiKey())
                    .build();


            return chain.proceed(request.newBuilder().url(url).build());
        }
    };

    public Api(ApiConfig config) {
        this(config, null);
    }

    public Api(ApiConfig config, OkHttpClient httpClient) {
        mConfig = config;
        mHttpClient = httpClient == null ? new OkHttpClient() : httpClient;
        mHttpClient.interceptors().add(0, mRequestInterceptor);

    }

    public ApiConfig getConfig() {
        return mConfig;
    }

    private Service create() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mConfig.getEndpoint());

        Retrofit restAdapter = builder.build();

        return restAdapter.create(Service.class);
    }

    public Call<DiscoverResponse> getSlack() throws InvalidParameterException {

        Service service = create();


        return service.slack();
    }

    public Call<DiscoverResponse> getTrello() {
        Service service = create();


        return service.trello();
    }


    public interface Service {

        @GET(PATH_MOVIE)
        Call<DiscoverResponse> slack();

        @GET(PATH_MOVIE)
        Call<DiscoverResponse> trello();

    }

}
