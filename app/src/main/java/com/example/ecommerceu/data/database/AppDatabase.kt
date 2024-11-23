package com.example.ecommerceu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecommerceu.data.dao.CustomerDao
import com.example.ecommerceu.data.dao.ProductDao
import com.example.ecommerceu.data.entities.Customer
import com.example.ecommerceu.data.entities.Product

@Database(entities = [Customer::class,Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao;
    abstract fun productDao(): ProductDao;
}