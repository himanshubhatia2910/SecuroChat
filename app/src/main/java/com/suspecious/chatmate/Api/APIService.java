package com.suspecious.chatmate.Api;


import com.suspecious.chatmate.Utility.MyResponse;
import com.suspecious.chatmate.Utility.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADWx4O0w:APA91bFssAmcIx10J_Jt4yslhUVg6Tsplncg-K1Omd937lAhNZ_LfE0BVr9SVFl5KJaJn0Aq08QPUFVA3hvbhDqZ3PzRh97mQHuc831mXS0e__U1Bk_kqsBfLAe6F8iXtYOAn6wMUn5z"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
