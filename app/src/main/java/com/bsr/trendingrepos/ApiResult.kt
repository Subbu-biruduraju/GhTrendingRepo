package com.bsr.trendingrepos

sealed class ApiResult<out R> {
    class Loading<Nothing> : ApiResult<Nothing>()
    data class Success<out T>(val response: T) : ApiResult<T>()
    data class Error(val e: Exception?) : ApiResult<Nothing>()
}
