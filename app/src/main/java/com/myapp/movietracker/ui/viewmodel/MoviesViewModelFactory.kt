package com.myapp.movietracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.movietracker.domain.CreateMovieUseCase
import com.myapp.movietracker.domain.GetMovieListUseCase
import java.lang.IllegalArgumentException

class MoviesViewModelFactory(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val createMovieUseCase: CreateMovieUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoviesViewModel::class.java))
            MoviesViewModel(getMovieListUseCase, createMovieUseCase) as T
        else
            throw IllegalArgumentException("Unknown ViewModel Class")
    }
}