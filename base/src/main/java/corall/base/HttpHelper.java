package corall.base;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class HttpHelper {
    private static volatile HttpHelper INSTANCE;

    private static final int DEFAULT_TIMEOUT = 10;

    public static HttpHelper getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (HttpHelper.class) {
            if (INSTANCE == null) {
                INSTANCE = new HttpHelper();
            }
        }
        return INSTANCE;
    }

    private HashMap<String, String> header;

    private boolean DEBUG = false;

    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDebug(boolean debug) {
        this.DEBUG = debug;
    }

    public boolean isDebug() {
        return this.DEBUG;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public ApiService getApiService() {
        return apiService;
    }

    private HttpHelper() {

    }

    private ApiService apiService;

    public void init(HelperParam param) {
        baseUrl = param.getUrl();
        OkHttpClient.Builder httpBuilder = new OkHttpClient().newBuilder();
        httpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpBuilder.addInterceptor(new RequestInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public <T> Observable apiSubscribe(Observable<T>  observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public void sendHttpRequest(Observable observable, HttpSubscriber listener) {
        apiSubscribe(observable).subscribe(listener);
    }


    static public class HelperParam {
        private String url;
        private HashMap<String, String> header;

        public HelperParam(String url) {
            this.url = url;
        }

        public HelperParam(String url, HashMap<String, String> header) {
            this.url = url;
            this.header = header;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public HashMap<String, String> getHeader() {
            return header;
        }

        public void setHeader(HashMap<String, String> header) {
            this.header = header;
        }
    }
}
