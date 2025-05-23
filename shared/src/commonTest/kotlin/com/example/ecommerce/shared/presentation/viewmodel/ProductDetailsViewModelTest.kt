package com.example.ecommerce.shared.presentation.viewmodel

import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.model.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var fakeRepository: FakeProductRepository // Reusing FakeProductRepository from ProductListViewModelTest
    private val testDispatcher = UnconfinedTestDispatcher()

    private val sampleProduct = Product(1, "Product 1", 10.0, "Desc 1", "Cat 1", "img1", Rating(1.0, 10))
    private val sampleProduct2 = Product(2, "Product 2", 20.0, "Desc 2", "Cat 2", "img2", Rating(2.0, 20))


    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProductRepository() // Assuming FakeProductRepository is in the same package or imported
        viewModel = ProductDetailsViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProductDetails updates product StateFlow on successful fetch`() = runTest {
        fakeRepository.setProducts(listOf(sampleProduct, sampleProduct2)) // Add products to fake repo

        val targetProductId = sampleProduct.id
        viewModel.loadProductDetails(targetProductId)
        advanceUntilIdle()

        assertNotNull(viewModel.product.value)
        assertEquals(sampleProduct, viewModel.product.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadProductDetails sets isLoading correctly during fetch`() = runTest {
        fakeRepository.setProducts(listOf(sampleProduct))
        fakeRepository.setShouldSimulateDelay(true)

        val targetProductId = sampleProduct.id
        viewModel.loadProductDetails(targetProductId) // This starts the loading

        assertTrue(viewModel.isLoading.value, "ViewModel should be loading during fetch")

        advanceUntilIdle() // Let the coroutine complete

        assertFalse(viewModel.isLoading.value, "ViewModel should not be loading after fetch completes")
    }
    
    @Test
    fun `loadProductDetails sets error StateFlow when product not found`() = runTest {
        fakeRepository.setProducts(emptyList()) // No products in the repo

        val nonExistentProductId = 999
        viewModel.loadProductDetails(nonExistentProductId)
        advanceUntilIdle()

        assertNull(viewModel.product.value, "Product should be null if not found")
        assertNotNull(viewModel.error.value, "Error should be set if product not found")
        assertTrue(viewModel.error.value!!.contains("Product not found"), "Error message mismatch")
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadProductDetails sets error StateFlow on repository exception`() = runTest {
        val errorMessage = "Network error for specific product"
        fakeRepository.setShouldThrowError(true, errorMessage)

        val targetProductId = 1 // ID doesn't matter as much as the error throwing
        viewModel.loadProductDetails(targetProductId)
        advanceUntilIdle()

        assertNull(viewModel.product.value)
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains(errorMessage))
        assertFalse(viewModel.isLoading.value)
    }
}
