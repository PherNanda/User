package com.android.adevinta.provider

import com.android.adevinta.BuildConfig
import com.android.adevinta.provider.user.UserApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiProvider {

    private val httpClient: OkHttpClient
    private val retrofit: Retrofit
    val userApi: UserApi

    init {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header(
                            "User-Agent",
                            "Android:${BuildConfig.ADEVINTA_APP_NAME} Version:${BuildConfig.VERSION_NAME} Package:${BuildConfig.PACKAGE_NAME} Framework:${TAG}/4.4.0"
                        )
                        .build()
                )
            }
        httpClient = builder.build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()

        userApi = retrofit.create(UserApi::class.java)

    }

    companion object {
        const val CONNECTION_TIMEOUT_SECONDS = 30L
        const val READ_TIMEOUT_SECONDS = 120L
        const val WRITE_TIMEOUT_SECONDS = 120L

        private const val TAG = "OkHttp"
    }
}