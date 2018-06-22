package com.pappayaed.data.remote;

import com.pappayaed.data.model.ResultResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by yasar on 6/3/18.
 */

public interface ApiService {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/login")
    Observable<String> loginValidate(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/profile")
    Observable<String> getStudentProfile(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/student_fee_terms_list")
    Observable<ResultResponse> getStudentFeeList(@Body String json);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/student_attendance_list")
    Observable<ResultResponse> getStudentAtttendanceList(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/parent_circular")
    Observable<ResultResponse> getCircularAndHomeWork(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/attachment_id_data")
    Observable<ResultResponse> getAttachment_id_data(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/attachment_id_data")
    <T> Observable<Response<ResultResponse>> getAttachment_id_data1(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("mobile/rest/standard_fee_collection_list")
    Observable<ResultResponse> getStandard_fee_collection(@Body String json);

}
