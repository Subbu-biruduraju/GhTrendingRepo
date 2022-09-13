package com.bsr.trendingrepos

data class RepoResponse(
    val username: String,
    val repositoryName: String,
    val url: String,
    val description: String,
    val language: String,
    var selected: Boolean = false
)
