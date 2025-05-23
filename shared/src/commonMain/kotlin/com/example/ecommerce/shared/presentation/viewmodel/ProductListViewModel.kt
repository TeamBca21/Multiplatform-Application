package com.example.ecommerce.shared.presentation.viewmodel

import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.remote.ApiService // Added for ApiService instantiation
import com.example.ecommerce.shared.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel // For viewModelScope, if available, or create a custom scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

// A simple ViewModel base class if androidx.lifecycle.ViewModel is not available
open class BaseViewModel {
    private val viewModelJob = SupervisorJob()
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    protected open fun onCleared() {
        viewModelJob.cancel()
    }
}


class ProductListViewModel(private val productRepository: ProductRepository) : BaseViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = productRepository.getProducts()
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
                // Log the full error for debugging
                println("Error in ProductListViewModel: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

// Factory for creating ProductListViewModel (useful for DI or manual instantiation)
// This is a common pattern, especially if you're not using a DI framework
object ProductListViewModelFactory {
    fun create(): ProductListViewModel {
        // In a real app, ApiService might be a singleton or provided by DI
        val apiService = ApiService()
        val productRepository = ProductRepository(apiService)
        return ProductListViewModel(productRepository)
    }
}
