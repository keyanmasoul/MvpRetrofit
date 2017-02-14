package zjj.network;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private Retrofit retrofit;
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
        retrofit = new Retrofit.Builder()
                .client(httpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public Observable packageObservable(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Subscription sendHttpRequest(Observable observable, HttpSubscriber<T> listener) {
        return observable.compose(this.<T>applySchedulers()).subscribe(listener);
    }

    /**
     * Observable 转化
     *
     * @param <T>
     * @return
     */
    <T> Observable.Transformer<BaseHttpResponse<T>, T> applySchedulers() {
        return new Observable.Transformer<BaseHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseHttpResponse<T>> baseHttpResultObservable) {
                return baseHttpResultObservable.map(new HttpFunc<T>())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 用来统一处理Http请求到的数据,并将数据解析成对应的Model返回
     *
     * @param <T> Subscriber真正需要的数据类型
     */
    private class HttpFunc<T> implements Func1<BaseHttpResponse<T>, T> {

        @Override
        public T call(BaseHttpResponse<T> baseHttpResult) {
            //获取数据失败则抛出异常 会进入到subscriber的onError中
            return baseHttpResult.getSubjects();
        }
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
