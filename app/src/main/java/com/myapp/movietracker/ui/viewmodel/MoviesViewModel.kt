package com.myapp.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Input
import com.myapp.movietracker.CreateMovieMutation
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.domain.CreateMovieUseCase
import com.myapp.movietracker.domain.GetMovieListUseCase
import com.myapp.movietracker.type.CreateMovieFieldsInput
import com.myapp.movietracker.type.CreateMovieInput
import com.myapp.movietracker.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val createMovieUseCase: CreateMovieUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<List<GetMoviesQuery.Node>>()
    val movies: LiveData<List<GetMoviesQuery.Node>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>>
        get() = _error

    private val _isAddMovies = MutableLiveData<Boolean>()
    val isAddMovies: LiveData<Boolean>
        get() = _isAddMovies

    private val _newMovie = MutableLiveData<Event<CreateMovieMutation.Movie>>()
    val newMovie: LiveData<Event<CreateMovieMutation.Movie>>
        get() = _newMovie

    init {
        getMoviesListFromServer()
    }

    private fun getMoviesListFromServer() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = getMovieListUseCase(20)
            _isLoading.postValue(false)
            when (result) {
                is GraphQlResponse.Success -> {
                    parseMovieList(result.data.edges)
                }

                is GraphQlResponse.Error -> {
                    _error.postValue(Event(result.message))
                }
            }
        }
    }

    private fun parseMovieList(edges: List<GetMoviesQuery.Edge?>?) {
        edges?.mapNotNull { it -> it?.node }?.let {
            _movies.postValue(it)
        } ?: kotlin.run {
            _error.postValue(Event("No movies found!"))
        }
    }

    fun isAddMovies(value: Boolean) {
        _isAddMovies.value = value
    }

    fun addNewMovie(movieName: String, releaseDate: String, season: String) {
        if (movieName.isNotBlank() && releaseDate.isNotBlank() && season.isNotBlank()) {
            val createMovieFieldsInput = CreateMovieFieldsInput(
                title = movieName,
                releaseDate = Input.fromNullable(releaseDate),
                seasons = Input.fromNullable(season.toDoubleOrNull())
            )
            val createMovieInput = CreateMovieInput(Input.fromNullable(createMovieFieldsInput))
            createMovie(createMovieInput)
        } else {
            _error.value = Event("Kindly provide all information.")
        }
    }

    private fun createMovie(createMovieInput: CreateMovieInput) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                createMovieUseCase(createMovieInput)
            _isLoading.postValue(false)
            when (result) {
                is GraphQlResponse.Success -> {
                    _newMovie.postValue(Event(result.data))
                }

                is GraphQlResponse.Error -> {
                    _error.postValue(Event(result.message))
                }
            }
        }
    }
}