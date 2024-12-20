package com.example.ecommerceu.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.ecommerceu.data.entities.Product

class CartViewModel : ViewModel() {
    private val _cartItems: SnapshotStateList<CartItem> = mutableStateListOf()
    val cartItems: List<CartItem> get() = _cartItems

    fun addToCart(product: Product) {
        var existingItem = _cartItems.find { it.product == product }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        _cartItems.remove(cartItem)
    }

    fun clearCart() {
        _cartItems.clear()
    }
}

data class CartItem(val product: Product, var quantity: Int)

