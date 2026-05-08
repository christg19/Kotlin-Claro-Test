package com.chris.contactos.ui.contactlist

import com.chris.contactos.domain.repository.ContactRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactListViewModelTest {

    private lateinit var repository: ContactRepository
    private lateinit var viewModel: ContactListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk(relaxed = true)
        every { repository.getContacts() } returns flowOf(emptyList())
        viewModel = ContactListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChanged updates searchQuery`() = runTest {
        viewModel.onSearchQueryChanged("chris")
        assertEquals("chris", viewModel.searchQuery.value)
    }

    @Test
    fun `toggleSelection adds id to selectedIds`() = runTest {
        viewModel.toggleSelection(1)
        assertTrue(1 in viewModel.selectedIds.value)
    }

    @Test
    fun `toggleSelection removes id if already selected`() = runTest {
        viewModel.toggleSelection(1)
        viewModel.toggleSelection(1)
        assertFalse(1 in viewModel.selectedIds.value)
    }

    @Test
    fun `deleteSelected clears selectedIds`() = runTest {
        viewModel.toggleSelection(1)
        viewModel.toggleSelection(2)
        viewModel.deleteSelected()
        assertTrue(viewModel.selectedIds.value.isEmpty())
    }

    @Test
    fun `deleteSelected calls repository for each selected id`() = runTest {
        viewModel.toggleSelection(3)
        viewModel.deleteSelected()
        coVerify { repository.deleteContactById(3) }
    }
}
