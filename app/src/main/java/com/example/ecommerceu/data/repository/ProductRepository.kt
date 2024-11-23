package com.example.ecommerceu.data.repository

import com.example.ecommerceu.data.dao.ProductDao
import com.example.ecommerceu.data.entities.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao : ProductDao) {

    suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    fun getProductById(productId: Int): Flow<Product?> {
        return productDao.getProductById(productId)
    }
}
