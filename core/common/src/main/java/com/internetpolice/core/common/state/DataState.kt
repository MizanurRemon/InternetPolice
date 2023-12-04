package com.internetpolice.core.common.state



sealed class DataState<T> {
    class Success<T>(val dataResponse : DataSuccessResponse<T>) : DataState<T>()
    class Error<T>(val dataResponse : DataErrorResponse<T>) : DataState<T>()

}