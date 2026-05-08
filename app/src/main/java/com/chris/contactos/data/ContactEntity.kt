package com.chris.contactos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chris.contactos.domain.model.Contact

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val lastName: String,
    val phone: String,
    val imageUrl: String
)

fun ContactEntity.toDomain() = Contact(
    id = id,
    name = name,
    lastName = lastName,
    phone = phone,
    imageUrl = imageUrl
)

fun Contact.toEntity() = ContactEntity(
    id = id,
    name = name,
    lastName = lastName,
    phone = phone,
    imageUrl = imageUrl
)
