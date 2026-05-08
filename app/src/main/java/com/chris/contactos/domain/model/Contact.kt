package com.chris.contactos.domain.model

data class Contact(
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val phone: String,
    val imageUrl: String
)
