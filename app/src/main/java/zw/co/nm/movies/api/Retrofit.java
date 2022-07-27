package zw.co.nm.movies.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    private static retrofit2.Retrofit retrofit = null;
    private static String baseUrl = "https://yts.mx/api/v2/";


    public static retrofit2.Retrofit getClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            return chain.proceed(requestBuilder.build());
        });
        OkHttpClient okHttpClient = builder.addInterceptor(httpLoggingInterceptor).build();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static ApiService getService() {
        return getClient().create(ApiService.class);
    }
}
