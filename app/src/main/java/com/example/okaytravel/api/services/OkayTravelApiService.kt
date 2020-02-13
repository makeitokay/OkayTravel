package com.example.okaytravel.api.services

import com.example.okaytravel.api.responses.okaytravelserver.SyncResponse
import com.example.okaytravel.api.models.okaytravelserver.AuthBody
import com.example.okaytravel.api.models.okaytravelserver.CreateUserBody
import com.example.okaytravel.api.models.okaytravelserver.SyncBody
import com.example.okaytravel.api.responses.okaytravelserver.SignUpResponse
import com.example.okaytravel.api.responses.okaytravelserver.UserInfoResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface OkayTravelApiService {

    @POST("auth")
    fun auth(@Body authBody: AuthBody): Observable<UserInfoResponse>

    @POST("create")
    fun createUser(@Body createUserBody: CreateUserBody): Observable<SignUpResponse>

    @POST("sync")
    fun sync(@Body syncBody: SyncBody): Observable<SyncResponse>

    companion object Factory {

        private const val apiUrl: String = "https://okaytravel.pythonanywhere.com"

        fun create(): OkayTravelApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiUrl)
                .build()

            return retrofit.create(OkayTravelApiService::class.java)
        }
    }
}