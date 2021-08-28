package com.myapp.movietracker.api

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object GraphQlApolloClient {

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl(BASE_URL)
        .okHttpClient(OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build())
        .build()

}