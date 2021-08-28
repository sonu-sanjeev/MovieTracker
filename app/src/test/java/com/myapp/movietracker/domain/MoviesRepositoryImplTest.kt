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
    fun `test getMovies query on success`() {
        val response = runBlocking {
            moviesRepository.getMovies(10)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Success::class.java)
    }

    @Test
    fun `test getMovies query with negative count`() {
        val response = runBlocking {
            moviesRepository.getMovies(-5)
        }

        assertThat(response).isNotNull()
        assertThat(response).isInstanceOf(GraphQlResponse.Error::class.java)
    }

    @Test
    fun  `test createMovie mutation on success`() {

    }

    @Test
    fun `test date conversion`() {
        val str = "2014-11-07T00:00:00.000Z"
        val r = str.getDate()
        assertThat(r).isEqualTo("07-11-2014")
    }
}