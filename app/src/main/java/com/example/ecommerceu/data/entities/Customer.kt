package com.example.ecommerceu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name:String,
    val email:String,
    val password:String
)
