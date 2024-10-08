package com.example.notesappretrofit.di

import android.content.Context
import androidx.room.Room
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.example.notesappretrofit.BuildConfig
import com.example.notesappretrofit.data.local.dao.NoteDao
import com.example.notesappretrofit.data.local.database.NoteDatabase
import com.example.notesappretrofit.data.local.token.DataAssetManager
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.data.repository.NoteRepositoryImpl
import com.example.notesappretrofit.data.repository.UserRepositoryImpl
import com.example.notesappretrofit.data.workmanager.ConnectivityObserverImpl
import com.example.notesappretrofit.data.workmanager.NoteSyncWorker
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.domain.repository.UserRepository
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.io.path.name

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteApi(retrofit: Retrofit) :NoteApi {
        return retrofit.create(NoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : UserApi{
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): DataAssetManager {
        return DataAssetManager(context)
    }
    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return ConnectivityObserverImpl(context)
    }


    @Provides
    @Singleton
    fun provideUserRepository(
        api: UserApi,
        dataAssetManager: DataAssetManager
    ):UserRepository{
        return UserRepositoryImpl(api,dataAssetManager)
    }
    @Provides
    @Singleton
    fun provideDao(noteDatabase: NoteDatabase):NoteDao{
        return noteDatabase.dao
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        api: NoteApi,
        local:NoteDao,
        localDatabase: NoteDatabase,
        dataAssetManager: DataAssetManager
    ):NoteRepository{
        return  NoteRepositoryImpl(api,local,localDatabase,dataAssetManager)
    }



}