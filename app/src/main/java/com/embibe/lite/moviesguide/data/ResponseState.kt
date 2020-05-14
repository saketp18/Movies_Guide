package com.embibe.lite.moviesguide.data

sealed class ResponseState {
    data class Error(val throwable: Throwable) : ResponseState()
    data class Fail(val message: String?, val code: Int) : ResponseState()
    data class Success(val data: Any) : ResponseState()

    fun handleResponse(responseState: ResponseState,
                       onSuccess: () -> Unit,
                       onFailure: () -> Unit,
                       onError: () -> Unit) {
        when(responseState) {
            is Success -> onSuccess()
            is Fail -> onFailure()
            is Error -> onError()
        }
    }
}
