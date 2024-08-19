package com.example.notesappretrofit.di

import android.content.Context
import com.example.notesappretrofit.constant.AppConstant
import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.data.repository.NoteRepositoryImpl
import com.example.notesappretrofit.data.repository.UserRepositoryImpl
import com.example.notesappretrofit.data.workmanager.ConnectivityObserverImpl
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
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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
    fun provideTokenManager(@ApplicationContext context: Context):TokenManager{
        return TokenManager(context)
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
        tokenManager: TokenManager
    ):UserRepository{
        return UserRepositoryImpl(api,tokenManager)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        api: NoteApi
    ):NoteRepository{
        return  NoteRepositoryImpl(api)
    }



}