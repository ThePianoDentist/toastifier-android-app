package com.thepianodentist.toastnotificationapp.api

// Other imported classes

import android.util.Log
import com.thepianodentist.toastnotificationapp.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class Retriever {
    private val service: Service

    companion object {
        const val BASE_URL = "http://dev.kettle-on.com/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // how do this in modern retrofit?
//        builder.addInterceptor { chain ->
//            val request: Request = chain.request().newBuilder().addHeader("key", "value").build()
//            chain.proceed(request)
//        }
        service = retrofit.create(Service::class.java)
    }

    suspend fun postUser(reqBody: PostUserRequestBody){
        service.postUser(reqBody)
    }
}