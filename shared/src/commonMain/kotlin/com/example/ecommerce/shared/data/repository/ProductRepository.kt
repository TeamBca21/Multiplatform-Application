package com.example.ecommerce.shared.data.repository

import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.remote.ApiService

class ProductRepository(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product> {
        return try {
            apiService.getProducts()
        } catch (e: Exception) {
            // Log the exception or handle it more gracefully
            println("Error in ProductRepository: ${e.message}")
            emptyList()
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return try {
            apiService.getProductById(id)
        } catch (e: Exception) {
            println("Error in ProductRepository fetching product by ID $id: ${e.message}")
            null
        }
    }
}
