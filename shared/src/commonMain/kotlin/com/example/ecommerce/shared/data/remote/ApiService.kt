package com.example.ecommerce.shared.data.remote

import com.example.ecommerce.shared.data.model.Product
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiService {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Important for APIs that might add new fields
            })
        }
    }

    suspend fun getProducts(): List<Product> {
        return try {
            httpClient.get("https://fakestoreapi.com/products").body()
        } catch (e: Exception) {
            // Log the exception or handle it more gracefully
            println("Error fetching products: ${e.message}")
            emptyList()
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return try {
            httpClient.get("https://fakestoreapi.com/products/$id").body()
        } catch (e: Exception) {
            println("Error fetching product by ID $id: ${e.message}")
            null
        }
    }
}
