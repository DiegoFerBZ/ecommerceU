package com.example.ecommerceu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceu.data.entities.Customer

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerCustomer(customer:Customer)

    @Query("SELECT * FROM customer WHERE email = :email LIMIT 1")
    suspend fun getCustomerByEmail(email:String): Customer?

    @Query("SELECT COUNT(*) FROM customer WHERE email = :email")
    suspend fun emailExists(email: String): Int
}