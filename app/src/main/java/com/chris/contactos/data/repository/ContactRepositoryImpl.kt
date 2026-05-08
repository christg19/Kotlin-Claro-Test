package com.chris.contactos.data.repository

import com.chris.contactos.data.local.ContactDao
import com.chris.contactos.data.remote.PicsumApi
import com.chris.contactos.data.local.toDomain
import com.chris.contactos.data.local.toEntity
import com.chris.contactos.domain.model.Contact
import com.chris.contactos.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor (
    private val dao: ContactDao,
    private val api: PicsumApi
) : ContactRepository {

    override fun getContacts(): Flow<List<Contact>> =
        dao.getAllContacts().map { entities ->
            entities.map {it.toDomain() }


    }

    override suspend fun saveContact(contact: Contact) =
        dao.insertContact(contact.toEntity())


    override suspend fun deleteContactById(id: Int) =
        dao.deleteContact(id)

    override suspend fun getRandomImageUrl(): String {
        val photos = api.getPhotos()
        val randomPhoto = photos.random()
        return "https://picsum.photos/id/${randomPhoto.id}/300/300"
    }
}