package com.myapp.movietracker.domain

import com.myapp.movietracker.CreateMovieMutation
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.type.CreateMovieInput

interface MoviesRepository {
    suspend fun getMovies(count: Int): GraphQlResponse<GetMoviesQuery.Movies>

    suspend fun createMovie(createMovieInput: CreateMovieInput): GraphQlResponse<CreateMovieMutation.Movie>
}