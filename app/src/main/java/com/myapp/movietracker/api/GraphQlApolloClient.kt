package com.myapp.movietracker.api

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object GraphQlApolloClient {

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl(BASE_URL)
        .okHttpClient(OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build())
        .build()

}