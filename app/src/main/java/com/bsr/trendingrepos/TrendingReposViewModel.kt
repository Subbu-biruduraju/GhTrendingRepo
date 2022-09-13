package com.bsr.trendingrepos

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TrendingReposViewModel(val trendingRepo: TrendingRepo) : ViewModel() {

    private val repoList = MutableLiveData<List<RepoResponse>>()
    fun getRepoList(): LiveData<List<RepoResponse>> = repoList

    private val repoListState = MutableLiveData<ApiResult<List<RepoResponse>>>()
    fun repoListState(): LiveData<ApiResult<List<RepoResponse>>> = repoListState

    init {
        getTrendingRepos()
    }

    fun getTrendingRepos() = viewModelScope.launch {
        repoListState.value = ApiResult.Loading()

        try {
            val response = trendingRepo.getTrendingRepos()

            response.takeIf { it.isSuccessful }?.body()?.let {
                repoList.value = it
                repoListState.value = ApiResult.Success(it)
            } ?: kotlin.run {
                repoListState.value = ApiResult.Error(null)
            }

        } catch (e: Exception) {
            repoListState.value = ApiResult.Error(e)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class TrendingReposViewModelFactory(
    private val trendingRepo: TrendingRepo
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (TrendingReposViewModel(trendingRepo) as T)
}
