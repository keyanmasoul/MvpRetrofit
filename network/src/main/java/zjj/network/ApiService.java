package zjj.network;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public interface ApiService{

    @GET("movie/top250")
    Observable<BaseHttpResponse> getTopMovie(@Query("start") int start,
                                                          @Query("count") int count);

    @FormUrlEncoded
    @POST("distance?")
    Observable<BaseHttpResponse> getDistance(@Field("waypoints") String waypoints,
                                                         @Field("ak") String ak,
                                                         @Field("output") String output);
}
