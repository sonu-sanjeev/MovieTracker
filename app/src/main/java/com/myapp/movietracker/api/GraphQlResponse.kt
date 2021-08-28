package com.myapp.movietracker.api

import java.lang.Exception

sealed class GraphQlResponse<T> {

    data class Success<T>(val data: T) : GraphQlResponse<T>()

    data class Error<T>(val message: String) : GraphQlResponse<T>()

    companion object {
        private const val ERROR_MESSAGE = "Something went wrong!"

        fun <T> success(response: T) = Success(response)

        fun <T> error(exception: Exception = Exception(ERROR_MESSAGE)) = Error<T>(exception.message ?: ERROR_MESSAGE)
    }
}