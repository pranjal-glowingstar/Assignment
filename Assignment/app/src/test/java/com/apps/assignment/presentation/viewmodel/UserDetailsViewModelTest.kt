package com.apps.assignment.presentation.viewmodel

import com.apps.assignment.common.models.Repository
import com.apps.assignment.common.models.User
import com.apps.assignment.repository.IGithubRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailsViewModelTest {

    private lateinit var viewModel: UserDetailsViewModel
    private val githubRepository = mockk<IGithubRepository>()
    private val user = mockk<User>()
    private val repository = mockk<Repository>()

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns Dispatchers.Main
        viewModel = UserDetailsViewModel(githubRepository)
    }

    @Test
    fun resetStates() = runTest{
        viewModel.resetStates()
        assertEquals(viewModel.userInfo.first(), null)
        assertEquals(viewModel.error.first(), false)
        assertEquals(viewModel.repoError.first(), false)
    }
    @Test
    fun fetchUserInfoSuccess() = runTest{
        coEvery { githubRepository.fetchUserInfo(any()) } returns Response.success(user)
        viewModel.fetchUserInfo("test")
        advanceUntilIdle()
        assertNotNull(viewModel.userInfo.first())
    }
    @Test
    fun fetchUserInfoFailure() = runTest{
        coEvery { githubRepository.fetchUserInfo(any()) } returns Response.error(401, ResponseBody.Companion.create(null, ""))
        viewModel.fetchUserInfo("test")
        advanceUntilIdle()
        assertTrue(viewModel.error.first())
    }
    @Test
    fun fetchUserRepositoriesSuccess() = runTest{
        coEvery { githubRepository.fetchUserRepositories(any(), any()) } returns Response.success(listOf(repository))
        viewModel.fetchUserRepositories("test")
        advanceUntilIdle()
        assertTrue(viewModel.userRepos.first().isNotEmpty())
    }
    @Test
    fun fetchUserRepositoriesFailure() = runTest{
        coEvery { githubRepository.fetchUserRepositories(any(), any()) } returns Response.error(401, ResponseBody.Companion.create(null, ""))
        viewModel.fetchUserRepositories("test")
        advanceUntilIdle()
        assertTrue(viewModel.repoError.first())
    }
}