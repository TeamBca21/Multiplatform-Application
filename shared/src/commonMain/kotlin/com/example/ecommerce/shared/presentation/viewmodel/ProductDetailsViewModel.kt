package com.example.ecommerce.shared.presentation.viewmodel

import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.remote.ApiService // Added for ApiService instantiation
import com.example.ecommerce.shared.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Reusing BaseViewModel from ProductListViewModel file
// If it were in a separate file, it would be imported.

class ProductDetailsViewModel(private val productRepository: ProductRepository) : BaseViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _product.value = productRepository.getProductById(productId)
                if (_product.value == null && _error.value == null) { // If product is null but no exception was caught
                    _error.value = "Product not found."
                }
            } catch (e: Exception) {
                _error.value = "Failed to load product details: ${e.message}"
                // Log the full error for debugging
                println("Error in ProductDetailsViewModel: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

// Factory for creating ProductDetailsViewModel
object ProductDetailsViewModelFactory {
    fun create(): ProductDetailsViewModel {
        // In a real app, ApiService might be a singleton or provided by DI
        val apiService = ApiService()
        val productRepository = ProductRepository(apiService)
        return ProductDetailsViewModel(productRepository)
    }
}
