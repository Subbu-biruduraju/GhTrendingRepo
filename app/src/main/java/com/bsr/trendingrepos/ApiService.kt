package com.bsr.trendingrepos

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(UrlConstants.GET_TRENDING_REPOS)
    suspend fun getTrendingRepos(): Response<List<RepoResponse>>

}