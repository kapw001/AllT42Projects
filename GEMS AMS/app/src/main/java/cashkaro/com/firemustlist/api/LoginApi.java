package cashkaro.com.firemustlist.api;


import cashkaro.com.firemustlist.App.App;
import cashkaro.com.firemustlist.model.ResultResponse;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by yasar on 18/4/17.
 */

public interface LoginApi {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(App.fire_muster + "user_login")
    Call<ResultResponse> login(@Body String json);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(App.fire_muster + "get_school_list")
    Call<ResultResponse> getSchoolList(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(App.fire_muster + "get_create_fire_muster_list")
    Call<ResultResponse> getCreateFireMusterList(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(App.fire_muster + "update_fire_muster_list")
    Call<ResultResponse> updateFireMusterList(@Body String json);

}
