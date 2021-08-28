package com.myapp.movietracker.domain

import com.apollographql.apollo.coroutines.await
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlApolloClient
import com.myapp.movietracker.api.GraphQlResponse

class MoviesRepositoryImpl : MoviesRepository {

    override suspend fun getMovies(first: Int): GraphQlResponse<GetMoviesQuery.Movies> {
        return try {
            val response =
                GraphQlApolloClient.apolloClient.query(GetMoviesQuery(first)).await()

            if (!response.hasErrors() && response.data != null) {
                GraphQlResponse.create(response.data!!.movies)
            } else {
                GraphQlResponse.create(Exception())
            }
        } catch (exception: Exception) {
            GraphQlResponse.create(exception)
        }
    }
}