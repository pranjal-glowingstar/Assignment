package com.apps.assignment.repository

import com.apps.assignment.data.remote.GithubApiService
import com.apps.assignment.common.models.FetchListModel
import com.apps.assignment.common.models.Repository
import com.apps.assignment.common.models.User
import retrofit2.Response
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val githubApiService: GithubApiService): IGithubRepository {
    override suspend fun searchPrefix(prefix: String, pageNumber: Int): Response<FetchListModel> {
        return githubApiService.getSearchUsers("$prefix in:login", pageNumber)
    }

    override suspend fun fetchUserInfo(username: String): Response<User> {
        return githubApiService.getUserData(username)
    }

    override suspend fun fetchUserRepositories(username: String, pageNumber: Int): Response<List<Repository>> {
        return githubApiService.getUserRepositories(username, pageNumber)
    }

}