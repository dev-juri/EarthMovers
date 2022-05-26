package com.earthmovers.www.data

sealed class NetworkResult<out R> {

    data class Success<out T>(val data : T) : NetworkResult<T>()
    data class Error(val exception: String) : NetworkResult<Nothing>()
    object Loading: NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}