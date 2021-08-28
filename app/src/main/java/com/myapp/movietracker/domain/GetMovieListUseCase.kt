package com.myapp.movietracker.domain

import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlResponse

interface GetMovieListUseCase {
    suspend operator fun invoke(count: Int): GraphQlResponse<GetMoviesQuery.Movies>
}

class GetMovieListUseCaseImpl(private val moviesRepository: MoviesRepository) :
    GetMovieListUseCase {

    override suspend fun invoke(count: Int): GraphQlResponse<GetMoviesQuery.Movies> =
        moviesRepository.getMovies(count)

}