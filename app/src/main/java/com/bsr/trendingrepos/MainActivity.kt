package com.bsr.trendingrepos

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsr.trendingrepos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val apiService by lazy { RetrofitClient.getInstance().create(ApiService::class.java) }
    private val trendingRepo by lazy { TrendingRepo(apiService) }
    private val mViewModel by viewModels<TrendingReposViewModel> {
        TrendingReposViewModelFactory(trendingRepo)
    }

    private lateinit var mAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        with(mBinding) {
            tvRetry.setOnClickListener { getTrendingRepos() }

            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
            mAdapter = RepoListAdapter()
            rvList.adapter = mAdapter

            mViewModel.repoListState().observe(this@MainActivity) {
                parseRepoResponse(it)
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query ?: return false

                    mViewModel.getRepoList().value?.filter {
                        it.username.contains(query) || it.repositoryName.contains(query) || it.description.contains(
                            query
                        )
                    }?.let {
                        mAdapter.addItems(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText ?: return false

                    mViewModel.getRepoList().value?.filter {
                        it.username.contains(newText, true)
                                || it.repositoryName.contains(newText, true)
                                || it.description.contains(newText, true)
                    }?.let {
                        mAdapter.addItems(it)
                    }
                    return true
                }

            })
        }

    }

    private fun getTrendingRepos() {
        mViewModel.getTrendingRepos()
    }

    private fun parseRepoResponse(resp: ApiResult<List<RepoResponse>>) {
        with(mBinding) {
            when (resp) {
                is ApiResult.Loading -> {
                    enableSearchView(searchView, false)
                    tvRetry.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    rvList.visibility = View.INVISIBLE
                }

                is ApiResult.Success -> {
                    progressBar.visibility = View.GONE
                    rvList.visibility = View.VISIBLE

                    resp.response.takeIf { it.isNotEmpty() }?.let {
                        enableSearchView(searchView, true)
                        rvList.visibility = View.VISIBLE
                        mAdapter.addItems(it)
                    } ?: kotlin.run {
                        Toast.makeText(
                            this@MainActivity,
                            "No items to display currently",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is ApiResult.Error -> {
                    progressBar.visibility = View.GONE
                    tvRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun enableSearchView(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                enableSearchView(child, enabled)
            }
        }
    }
}