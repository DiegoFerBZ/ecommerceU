package com.example.ecommerceu.data.repository

import com.example.ecommerceu.data.dao.CustomerDao
import com.example.ecommerceu.data.entities.Customer

class CustomerRepository(private val customerDao: CustomerDao) {
    suspend fun registerCustomer(customer: Customer) {
        customerDao.registerCustomer(customer)
    }

    suspend fun getCustomerByEmail(email: String): Customer? {
        return customerDao.getCustomerByEmail(email)
    }

}