package com.chris.contactos.di

import android.content.Context
import androidx.room.Room
import com.chris.contactos.data.local.AppDatabase
import com.chris.contactos.data.local.ContactDao
import com.chris.contactos.data.remote.PicsumApi
import com.chris.contactos.data.repository.ContactRepositoryImpl
import com.chris.contactos.domain.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePicsumApi(retrofit: Retrofit): PicsumApi =
        retrofit.create(PicsumApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "contacts_db").build()

    @Provides
    @Singleton
    fun provideContactDao(database: AppDatabase): ContactDao =
        database.contactDao()

    @Provides
    @Singleton
    fun provideContactRepository(dao: ContactDao, api: PicsumApi): ContactRepository =
        ContactRepositoryImpl(dao, api)
}