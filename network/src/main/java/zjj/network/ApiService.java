package zjj.network;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public interface ApiService{

    @GET("/api/xjti")
    Observable<BaseHttpResponse> getAdConfig();

}
