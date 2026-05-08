package com.chris.contactos.ui.createcontact

import com.chris.contactos.domain.repository.ContactRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateContactViewModelTest {

    private lateinit var repository: ContactRepository
    private lateinit var viewModel: CreateContactViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk(relaxed = true)
        coEvery { repository.getRandomImageUrl() } returns "https://picsum.photos/id/1/300/300"
        viewModel = CreateContactViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads a random image`() = runTest {
        advanceUntilIdle()
        assertEquals("https://picsum.photos/id/1/300/300", viewModel.imageUrl.value)
    }

    @Test
    fun `saveContact with blank field emits failure`() = runTest {
        val results = mutableListOf<Result<Unit>>()
        val job = launch { viewModel.saveEvent.collect { results.add(it) } }
        viewModel.saveContact("", "Doe", "123")
        advanceUntilIdle()
        assertTrue(results.isNotEmpty() && results.first().isFailure)
        job.cancel()
    }

    @Test
    fun `saveContact with valid fields emits success`() = runTest {
        val results = mutableListOf<Result<Unit>>()
        val job = launch { viewModel.saveEvent.collect { results.add(it) } }
        viewModel.saveContact("John", "Doe", "123456")
        advanceUntilIdle()
        assertTrue(results.isNotEmpty() && results.first().isSuccess)
        job.cancel()
    }

    @Test
    fun `saveContact with valid fields calls repository`() = runTest {
        viewModel.saveContact("John", "Doe", "123456")
        advanceUntilIdle()
        coVerify { repository.saveContact(any()) }
    }
}
