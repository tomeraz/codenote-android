package com.codenote.tikal.example.client;


public class ApiConfig {

    public static final String API_ENDPOINT_DEFAULT = "http://api.themoviedb.org";
    //    https://api.themoviedb.org/3/movie/550?api_key=
    private String mApiKey = "2008dbfe01abd836bb422f5d123a1630";
    private String mEndpoint = API_ENDPOINT_DEFAULT;
    private HttpLoggingInterceptor.Logger logger;

    private boolean mDebug;

    public String getApiKey() {
        return mApiKey;
    }

    public String getEndpoint() {
        return mEndpoint;
    }

    public void setEndpoint(String endpoint) {
        mEndpoint = endpoint;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public void setDebug(boolean debug) {
        mDebug = debug;
    }


    public HttpLoggingInterceptor.Logger getLogger() {
        return logger;
    }

    public void setLogger(HttpLoggingInterceptor.Logger logger) {
        this.logger = logger;
    }
}
