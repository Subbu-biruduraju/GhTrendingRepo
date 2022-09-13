package com.bsr.trendingrepos

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrendingRepo(
    val apiService: ApiService,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getTrendingRepos() = withContext(dispatcher) {
        apiService.getTrendingRepos()
    }

}