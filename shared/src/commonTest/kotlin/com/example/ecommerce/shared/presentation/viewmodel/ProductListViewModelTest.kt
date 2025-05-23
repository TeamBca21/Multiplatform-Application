package com.example.ecommerce.shared.presentation.viewmodel

import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.model.Rating
import com.example.ecommerce.shared.data.repository.ProductRepository
import com.example.ecommerce.shared.data.remote.ApiService // Required for FakeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private lateinit var viewModel: ProductListViewModel
    private lateinit var fakeRepository: FakeProductRepository
    private val testDispatcher = UnconfinedTestDispatcher() // StandardTestDispatcher can also be used

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher for ViewModel's scope
        fakeRepository = FakeProductRepository()
        viewModel = ProductListViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher
    }

    @Test
    fun `loadProducts updates products StateFlow on successful fetch`() = runTest {
        val mockProducts = listOf(
            Product(1, "Product 1", 10.0, "Desc 1", "Cat 1", "img1", Rating(1.0, 10)),
            Product(2, "Product 2", 20.0, "Desc 2", "Cat 2", "img2", Rating(2.0, 20))
        )
        fakeRepository.setProducts(mockProducts)

        viewModel.loadProducts() // ViewModel loads products on init, but we can call it again to be explicit

        // Advance time if using StandardTestDispatcher: testScheduler.advanceUntilIdle()
        
        assertEquals(mockProducts, viewModel.products.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadProducts sets isLoading correctly during fetch`() = runTest {
        fakeRepository.setShouldSimulateDelay(true) // Simulate network delay

        // Check initial state (isLoading might be true due to init{} block)
        // To test this part accurately, we might need to control init{} or use a fresh ViewModel
        // For simplicity, let's assume init has finished or we are testing a subsequent load.
        
        // Re-create ViewModel or ensure init call is handled if testing initial load precisely
        viewModel = ProductListViewModel(fakeRepository) // Re-init to test its initial loadProducts
        
        assertTrue(viewModel.isLoading.value, "ViewModel should be loading after init")

        advanceUntilIdle() // Let the coroutine in loadProducts complete

        assertFalse(viewModel.isLoading.value, "ViewModel should not be loading after fetch completes")
    }
    
    @Test
    fun `loadProducts sets isLoading to false even if products are empty`() = runTest {
        fakeRepository.setProducts(emptyList()) // Repository returns empty list
        
        viewModel.loadProducts() 
        advanceUntilIdle()
        
        assertFalse(viewModel.isLoading.value, "ViewModel should not be loading if empty list is fetched")
        assertTrue(viewModel.products.value.isEmpty(), "Products list should be empty")
    }

    @Test
    fun `loadProducts sets error StateFlow on repository exception`() = runTest {
        val errorMessage = "Network error"
        fakeRepository.setShouldThrowError(true, errorMessage)

        viewModel.loadProducts()
        advanceUntilIdle()

        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains(errorMessage))
        assertTrue(viewModel.products.value.isEmpty()) // Products should be empty on error
        assertFalse(viewModel.isLoading.value)
    }
}

// Fake implementation of ProductRepository for testing
class FakeProductRepository : ProductRepository(ApiService()) { // ApiService won't be used by the fake
    private var productsToReturn: List<Product> = emptyList()
    private var shouldThrowError: Boolean = false
    private var errorMessage: String = "Test Error"
    private var simulateDelay: Boolean = false

    fun setProducts(products: List<Product>) {
        this.productsToReturn = products
        this.shouldThrowError = false
    }

    fun setShouldThrowError(shouldThrow: Boolean, message: String = "Test Error") {
        this.shouldThrowError = shouldThrow
        this.errorMessage = message
    }
    
    fun setShouldSimulateDelay(delay: Boolean) {
        this.simulateDelay = delay
    }

    override suspend fun getProducts(): List<Product> {
        if (simulateDelay) {
            kotlinx.coroutines.delay(100) // Simulate network delay
        }
        if (shouldThrowError) {
            throw RuntimeException(errorMessage)
        }
        return productsToReturn
    }

    override suspend fun getProductById(id: Int): Product? {
        if (simulateDelay) {
            kotlinx.coroutines.delay(50)
        }
        if (shouldThrowError) {
            throw RuntimeException(errorMessage)
        }
        return productsToReturn.find { it.id == id }
    }
}
