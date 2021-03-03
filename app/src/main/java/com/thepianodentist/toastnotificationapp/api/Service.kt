package com.thepianodentist.toastnotificationapp.api

import com.thepianodentist.toastnotificationapp.data.ApiResponse
import com.thepianodentist.toastnotificationapp.data.PostUserRequestBody
import com.thepianodentist.toastnotificationapp.data.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface Service {

    @Headers("Content-Type: application/json")
    @POST("/register/")
    suspend fun postUser(@Body body: PostUserRequestBody): ApiResponse<UserResponse>

}
