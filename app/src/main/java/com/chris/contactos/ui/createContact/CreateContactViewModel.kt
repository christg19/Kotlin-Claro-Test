package com.chris.contactos.ui.createcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.contactos.domain.model.Contact
import com.chris.contactos.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl.asStateFlow()

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading.asStateFlow()

    private val _saveEvent = MutableSharedFlow<Result<Unit>>()
    val saveEvent: SharedFlow<Result<Unit>> = _saveEvent.asSharedFlow()

    init {
        loadRandomImage()
    }

    fun loadRandomImage() {
        viewModelScope.launch {
            _isImageLoading.value = true
            try {
                _imageUrl.value = repository.getRandomImageUrl()
            } catch (e: Exception) {
                _imageUrl.value = ""
            } finally {
                _isImageLoading.value = false
            }
        }
    }

    fun saveContact(name: String, lastName: String, phone: String) {
        if (name.isBlank() || lastName.isBlank() || phone.isBlank()) {
            viewModelScope.launch {
                _saveEvent.emit(Result.failure(Exception("Todos los campos son obligatorios")))
            }
            return
        }
        viewModelScope.launch {
            repository.saveContact(
                Contact(
                    name = name,
                    lastName = lastName,
                    phone = phone,
                    imageUrl = _imageUrl.value
                )
            )
            _saveEvent.emit(Result.success(Unit))
        }
    }
}