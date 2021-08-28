package com.myapp.movietracker.api

import com.myapp.movietracker.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(X_PARSE_APPLICATION_ID, BuildConfig.X_Parse_Application_Id)
            .addHeader(X_PARSE_MASTER_KEY, BuildConfig.X_Parse_Master_Key)
            .build()

        return chain.proceed(request)
    }
}