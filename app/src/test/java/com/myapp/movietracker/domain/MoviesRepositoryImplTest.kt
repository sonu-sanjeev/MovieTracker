package com.myapp.movietracker.domain

import com.apollographql.apollo.api.Input
import com.google.common.truth.Truth.assertThat
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.type.CreateMovieFieldsInput
import com.myapp.movietracker.type.CreateMovieInput
import com.myapp.movietracker.util.getDate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {

    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        moviesRepository = MoviesRepositoryImpl()
    }

    @Test
    fun `query graphql to fetch movies`() {
        val response = runBlocking {
            moviesRepository.getMovies(10)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Success::class.java)
    }

    @Test
    fun `query graphql with negative count`() {
        val response = runBlocking {
            moviesRepository.getMovies(-5)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Error::class.java)
    }

    @Test
    fun  `mutate graphql to create a movie entry`() {
        val createMovieInput =
            CreateMovieInput(Input.fromNullable(CreateMovieFieldsInput(title = "A new movie")))
        val response = runBlocking {
            moviesRepository.createMovie(createMovieInput)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Success::class.java)
    }

    @Test
    fun  `mutate graphql to create a movie entry with invalid inputs`() {
        val createMovieInput = CreateMovieInput(Input.absent())
        val response = runBlocking {
            moviesRepository.createMovie(createMovieInput)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Error::class.java)
    }

    @Test
    fun `test date conversion`() {
        val str = "2014-11-07T00:00:00.000Z"
        val date = str.getDate()
        assertThat(date).isEqualTo("07-11-2014")
    }
}