package cashkaro.com.firemustlist.retrofitservice;

import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cashkaro.com.firemustlist.App.App;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yasar on 6/9/17.
 */

public class RetrofitRequest {
    //    private static final String url = "https://amspreprod.pappaya.co.uk/vms/";
    private static final String vms = "/vms/";
    //    private static final String url = "http://192.168.1.70:8072/vms/fire_muster/";
    public static final String device = "device/";
    public static final String fire_muster = "fire_muster/";
    private static Retrofit retrofit;
    private static HttpLoggingInterceptor httpLoggingInterceptor;
    private static Interceptor provideOfflineCacheInterceptor;
    private static Interceptor provideCacheInterceptor;
    private static OkHttpClient okHttpClient;
    private static final String CACHE_CONTROL = "Cache-Control";
    private SharedPreferences sharedPreferences;

    private static RetrofitRequest retrofitRequest;

    public RetrofitRequest(Context context) {
        if (retrofitRequest == null) {
            sharedPreferences = context.getSharedPreferences("firemustlist", Context.MODE_PRIVATE);
            provideCacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());

                    // re-write response header to force use of cache
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(2, TimeUnit.MINUTES)
                            .build();

                    return response.newBuilder()
                            .header(CACHE_CONTROL, cacheControl.toString())
                            .build();
                }
            };

            provideOfflineCacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request request = chain.request();

                    if (!App.getApp().hasNetwork()) {
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale(7, TimeUnit.DAYS)
                                .build();

                        request = request.newBuilder()
                                .cacheControl(cacheControl)
                                .build();
                    }

                    return chain.proceed(request);

                }
            };

            httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {

                }
            });

            File cache_file = new File(context.getCacheDir(), "response");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache catche = new Cache(cache_file, cacheSize);

//            okHttpClient = new OkHttpClient().newBuilder().addInterceptor(provideOfflineCacheInterceptor).addInterceptor(httpLoggingInterceptor).addNetworkInterceptor(provideCacheInterceptor).cache(catche).build();
            okHttpClient = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).addInterceptor(httpLoggingInterceptor).cache(catche).build();

        }
    }


    public static RetrofitRequest getRetrofitRequest(Context context) {
        retrofitRequest = new RetrofitRequest(context);
        return retrofitRequest;
    }

    public Retrofit getRetrofit() {
        String url = sharedPreferences.getString("url", "") + vms;

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

}
