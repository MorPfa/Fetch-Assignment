package com.example.fetch_assignment.di

import com.example.fetch_assignment.data.ListRepositoryImpl
import com.example.fetch_assignment.data.network.ListService
import com.example.fetch_assignment.domain.ListRepository
import com.example.fetch_assignment.presentation.ListViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {
    singleOf(::ListRepositoryImpl).bind<ListRepository>()
    viewModelOf(::ListViewModel)

}

val networkModule = module {
    single<ListService> { get<Retrofit>().create(ListService::class.java) }
    single { GsonBuilder().create() }
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
}