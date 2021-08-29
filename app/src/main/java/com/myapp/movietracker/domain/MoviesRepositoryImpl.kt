package com.myapp.movietracker.domain

import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.myapp.movietracker.CreateMovieMutation
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlApolloClient
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.type.CreateMovieInput

class MoviesRepositoryImpl : MoviesRepository {

    override suspend fun getMovies(count: Int): GraphQlResponse<GetMoviesQuery.Movies> {
        return try {
            val response =
                GraphQlApolloClient.apolloClient.query(GetMoviesQuery(count)).await()

            val data = response.data
            if (!response.hasErrors() && data != null) {
                GraphQlResponse.success(data.movies)
            } else {
                val error = response.errors?.firstOrNull()
                error?.let { GraphQlResponse.error(Exception(it.message)) } ?: kotlin.run {
                    GraphQlResponse.error()
                }
            }
        } catch (exception: ApolloException) {
            GraphQlResponse.error(exception)
        }
    }

    override suspend fun createMovie(createMovieInput: CreateMovieInput): GraphQlResponse<CreateMovieMutation.Movie> {
        return try {
            val response =
                GraphQlApolloClient.apolloClient.mutate(CreateMovieMutation(createMovieInput)).await()
            val data = response.data?.createMovie
            if (!response.hasErrors() && data != null) {
                GraphQlResponse.success(data.movie)
            } else {
                val error = response.errors?.firstOrNull()
                error?.let { GraphQlResponse.error(Exception(it.message)) } ?: kotlin.run {
                    GraphQlResponse.error()
                }
            }
        } catch (exception: ApolloException) {
            GraphQlResponse.error(exception)
        }
    }
}