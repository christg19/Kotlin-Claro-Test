package com.chris.contactos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chris.contactos.data.local.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}