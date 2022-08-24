package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


var logging: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
    HttpLoggingInterceptor.Level.BASIC
)
var client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()
private val moshi=Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit=Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .build()

interface AsteroidWebService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query(value = "start_date") startDate:String,
        @Query(value = "end_date")  endDate:String,
        @Query(value="api_key") apiKey:String=Constants.API_KEY
    ):String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query(value="api_key") apiKey:String=Constants.API_KEY): PictureOfDay
}

object AsteroidApi{
    val asteroidWebService:AsteroidWebService by lazy {
        retrofit.create(AsteroidWebService::class.java)
    }
}