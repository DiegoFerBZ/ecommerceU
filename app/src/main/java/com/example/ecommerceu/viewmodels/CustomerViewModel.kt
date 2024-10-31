package com.example.ecommerceu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceu.data.entities.Customer
import com.example.ecommerceu.data.repository.CustomerRepository
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    // Function to register a new customer and return a boolean value
    suspend fun registerNewCustomer(customer: Customer): Boolean {
        return try {
            repository.registerCustomer(customer)
            true // Registration successful
        } catch (e: Exception) {
            // Log the error if necessary
            false // Registration failed
        }
    }

    // Call this function from your UI to perform the registration
    fun registerCustomer(customer: Customer, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = registerNewCustomer(customer)
            onResult(result) // Pass the result to the callback
        }
    }

    fun getCustomer(email: String): LiveData<Customer?> {
        val customerData = MutableLiveData<Customer?>()
        viewModelScope.launch {
            customerData.postValue(repository.getCustomerByEmail(email))
        }
        return customerData
    }
}