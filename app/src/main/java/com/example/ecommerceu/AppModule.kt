package com.example.ecommerceu

import androidx.room.Room
import com.example.ecommerceu.data.database.AppDatabase
import com.example.ecommerceu.data.repository.CustomerRepository
import com.example.ecommerceu.viewmodels.CustomerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "ecommerce_U").build()
    }
    single { get<AppDatabase>().customerDao() } // Providing CustomerDao
    single { CustomerRepository(get()) } // Providing CustomerRepository
    viewModel { CustomerViewModel(get()) }
}