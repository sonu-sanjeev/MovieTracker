package com.myapp.movietracker.domain

import com.apollographql.apollo.coroutines.await
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlApolloClient
import com.myapp.movietracker.api.GraphQlResponse

class MoviesRepositoryImpl : MoviesRepository {

    override suspend fun getMovies(count: Int): GraphQlResponse<GetMoviesQuery.Movies> {
        return try {
            val response =
                GraphQlApolloClient.apolloClient.query(GetMoviesQuery(count)).await()

            val data = response.data
            if (!response.hasErrors() && data != null) {
                GraphQlResponse.success(data.movies)
            } else {
                GraphQlResponse.error()
            }
        } catch (exception: Exception) {
            GraphQlResponse.error(exception)
        }
    }
}