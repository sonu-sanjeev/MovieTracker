package com.myapp.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.api.GraphQlResponse
import com.myapp.movietracker.domain.CreateMovieUseCase
import com.myapp.movietracker.domain.GetMovieListUseCase
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isAddMovies = MutableLiveData<Boolean>()
    val isAddMovies: LiveData<Boolean>
        get() = _isAddMovies


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
                    _error.postValue(result.message)
                }
            }
        }
    }

    private fun parseMovieList(edges: List<GetMoviesQuery.Edge?>?) {
        edges?.mapNotNull { it -> it?.node }?.let {
            _movies.postValue(it)
        } ?: kotlin.run {
            _error.postValue("No movies found!")
        }
    }

    fun isAddMovies(value: Boolean) {
        _isAddMovies.value = value
    }
}