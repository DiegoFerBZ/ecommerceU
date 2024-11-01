package com.example.ecommerceu.data.repository

import com.example.ecommerceu.data.dao.CustomerDao
import com.example.ecommerceu.data.entities.Customer

class CustomerRepository(private val customerDao: CustomerDao) {

    suspend fun registerCustomer(customer: Customer) :Boolean{
        val emailCount = customerDao.emailExists(customer.email)
        return if (emailCount > 0) {
            false
        } else {
            customerDao.registerCustomer(customer)
            true
        }
    }

    suspend fun getCustomerByEmail(email: String): Customer? {
        return customerDao.getCustomerByEmail(email)
    }

    suspend fun validateCredentials(email: String, password: String): Boolean {
        val customer = customerDao.getCustomerByEmail(email)
        return customer?.password == password
    }

}