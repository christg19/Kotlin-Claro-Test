package com.chris.contactos.ui.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.contactos.domain.model.Contact
import com.chris.contactos.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedIds: StateFlow<Set<Int>> = _selectedIds.asStateFlow()

    val contacts: StateFlow<List<Contact>> = _searchQuery
        .flatMapLatest { query ->
            repository.getContacts().map { list ->
                if (query.isBlank()) list
                else list.filter { contact ->
                    contact.name.contains(query, ignoreCase = true) ||
                        contact.lastName.contains(query, ignoreCase = true) ||
                        contact.phone.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _selectedIds.value = emptySet()
    }

    fun toggleSelection(contactId: Int) {
        _selectedIds.update { current ->
            if (contactId in current) current - contactId else current + contactId
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val toDelete = _selectedIds.value.toSet()
            _selectedIds.value = emptySet()
            toDelete.forEach { id -> repository.deleteContactById(id) }
        }
    }
}
