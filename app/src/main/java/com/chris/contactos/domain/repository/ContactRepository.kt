package com.chris.contactos.domain.repository

import com.chris.contactos.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContacts(): Flow<List<Contact>>

    suspend fun saveContact (contact: Contact)

    suspend fun deleteContactById(id: Int)

    suspend fun getRandomImageUrl(): String
}