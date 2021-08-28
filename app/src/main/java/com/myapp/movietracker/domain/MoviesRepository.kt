package com.myapp.movietracker.domain

import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlResponse

interface MoviesRepository {
    suspend fun getMovies(count: Int): GraphQlResponse<GetMoviesQuery.Movies>

    //fun createMovie(): GraphQlResponse<GetMoviesQuery.Movies>
}