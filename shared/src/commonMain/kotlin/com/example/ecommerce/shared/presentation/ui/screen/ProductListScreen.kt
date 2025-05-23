package com.example.ecommerce.shared.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.model.Rating
import com.example.ecommerce.shared.presentation.ui.component.ProductItem
import com.example.ecommerce.shared.presentation.viewmodel.ProductListViewModel
import com.example.ecommerce.shared.presentation.viewmodel.ProductListViewModelFactory
// import org.jetbrains.compose.ui.tooling.preview.Preview // Not available in commonMain for Material 3 yet

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = ProductListViewModelFactory.create(),
    onProductClick: (productId: Int) -> Unit // Added for navigation
) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("E-Commerce Products") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                products.isEmpty() && !isLoading -> {
                     Text(
                        text = "No products found. Try refreshing.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            ProductItem(
                                product = product,
                                onClick = { onProductClick(product.id) } // Call onProductClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// Preview for ProductListScreen
// @Preview
// @Composable
// fun ProductListScreenPreview_Loading() {
//     val mockViewModel = object : ProductListViewModel(ProductRepository(ApiService())) { // Simplified mock
//         override val isLoading = MutableStateFlow(true)
//         override val products = MutableStateFlow<List<Product>>(emptyList())
//         override val error = MutableStateFlow<String?>(null)
//     }
//    // MaterialTheme {
//         ProductListScreen(viewModel = mockViewModel, onProductClick = {})
//         ProductListScreen(viewModel = mockViewModel, onProductClick = {})
//         ProductListScreen(viewModel = mockViewModel, onProductClick = {})
//    // }
// }

// @Preview
// @Composable
// fun ProductListScreenPreview_Error() {
//      val mockViewModel = object : ProductListViewModel(ProductRepository(ApiService())) { // Simplified mock
//         override val isLoading = MutableStateFlow(false)
//         override val products = MutableStateFlow<List<Product>>(emptyList())
//         override val error = MutableStateFlow("Network connection failed. Please try again.")
//     }
//    // MaterialTheme {
//         ProductListScreen(viewModel = mockViewModel)
//    // }
// }

// @Preview
// @Composable
// fun ProductListScreenPreview_WithData() {
//     val sampleProducts = listOf(
//         Product(1, "Laptop Pro", 1299.99, "High-performance laptop", "Electronics", "url_to_image1", Rating(4.7, 250)),
//         Product(2, "Wireless Headphones", 199.99, "Noise-cancelling headphones", "Audio", "url_to_image2", Rating(4.5, 500)),
//         Product(3, "Smart Watch", 249.50, "Feature-rich smart watch", "Wearables", "url_to_image3", Rating(4.3, 150))
//     )
//      val mockViewModel = object : ProductListViewModel(ProductRepository(ApiService())) { // Simplified mock
//         override val isLoading = MutableStateFlow(false)
//         override val products = MutableStateFlow(sampleProducts)
//         override val error = MutableStateFlow<String?>(null)
//     }
//    // MaterialTheme {
//         ProductListScreen(viewModel = mockViewModel)
//    // }
// }
