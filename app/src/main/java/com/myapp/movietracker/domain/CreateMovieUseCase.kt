package com.myapp.movietracker.domain

import com.myapp.movietracker.CreateMovieMutation
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.type.CreateMovieInput

interface CreateMovieUseCase {
    suspend operator fun invoke(createMovieInput: CreateMovieInput): GraphQlResponse<CreateMovieMutation.Movie>
}

class CreateMovieUseCaseImpl(private val moviesRepository: MoviesRepository) : CreateMovieUseCase {
    override suspend fun invoke(createMovieInput: CreateMovieInput): GraphQlResponse<CreateMovieMutation.Movie> =
        moviesRepository.createMovie(createMovieInput)
}