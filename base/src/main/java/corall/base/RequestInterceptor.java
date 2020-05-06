package corall.base;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder originalRequestBuilder = chain.request().newBuilder();
        HashMap<String, String> header = HttpHelper.getInstance().getHeader();
        if (header != null) {
            for (String key : header.keySet()) {
                originalRequestBuilder.addHeader(key, Objects.requireNonNull(header.get(key)));
            }
        }
        Request request = originalRequestBuilder.build();
        printRequestLog(request);
        Response response = null;
        try {
            response = chain.proceed(request);
            printResponseLog(response);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 打印请求日志
     *
     * @return
     * @throws IOException
     */
    private void printRequestLog(Request originalRequest) throws IOException {
        if (!HttpHelper.getInstance().isDebug()) {
            return;
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        StringBuilder msg = new StringBuilder(originalRequest.url() + "\n");
        RequestBody oidBody = originalRequest.body();
        if (oidBody instanceof FormBody) {
            FormBody formBody = (FormBody) oidBody;
            for (int i = 0; i < formBody.size(); i++) {
                String name = URLDecoder.decode(formBody.encodedName(i), "utf-8");
                String value = URLDecoder.decode(formBody.encodedValue(i), "utf-8");
                if (!TextUtils.isEmpty(value)) {
                    formBuilder.add(name, value);
                    msg.append(name).append("  =  ").append(value).append("\n");
                }
            }
        }
        Log.d("network-request", msg.toString());
    }

    /**
     * 打印返回日志
     *
     * @throws IOException
     */
    private void printResponseLog(Response response) throws IOException {
        if (!HttpHelper.getInstance().isDebug()) {
            return;
        }
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset UTF8 = StandardCharsets.UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            UTF8 = contentType.charset(UTF8);
        }
        String a = buffer.clone().readString(UTF8);
        Log.d("network-response", a);
    }
}
