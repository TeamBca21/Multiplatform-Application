package com.example.ecommerce.shared.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.shared.data.model.Product
import com.example.ecommerce.shared.data.model.Rating
import com.example.ecommerce.shared.presentation.viewmodel.ProductDetailsViewModel
import com.example.ecommerce.shared.presentation.viewmodel.ProductDetailsViewModelFactory
// import org.jetbrains.compose.ui.tooling.preview.Preview // Not available in commonMain for Material 3 yet
// For image loading:
// import org.jetbrains.compose.resources.painterResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = ProductDetailsViewModelFactory.create(),
    productId: Int,
    onBackClick: () -> Unit // For navigation
) {
    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
    }

    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.title ?: "Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add to Cart") },
                icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Add to Cart") },
                onClick = { /* TODO: Implement Add to Cart */ },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 72.dp) // Space for the FAB
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
                product == null && !isLoading -> {
                     Text(
                        text = "Product details not available.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                product != null -> {
                    ProductDetailsContent(product!!)
                }
            }
        }
    }
}

@Composable
fun ProductDetailsContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Product Image
        KamelImage(
            resource = asyncPainterResource(data = product.image),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Adjust as needed
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit, // Or ContentScale.Crop
            onLoading = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            },
            onFailure = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Failed to load image", fontSize = 16.sp, color = Color.Gray)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Product Title
        Text(
            text = product.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product Category
        Text(
            text = "Category: ${product.category}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Product Price
        Text(
            text = "$${product.price}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Product Rating
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = if (index < product.rating.rate.toInt()) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${product.rating.rate} (${product.rating.count} ratings)",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Product Description
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.description,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reviews Section (Placeholder)
        Text(
            text = "Reviews",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No reviews yet. (Placeholder)",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // In a real app, this would be a list of review items.

        Spacer(modifier = Modifier.height(80.dp)) // Extra space at the bottom before FAB
    }
}

// Preview for ProductDetailsScreen
// @Preview
// @Composable
// fun ProductDetailsScreenPreview_Loading() {
//     val mockViewModel = ProductDetailsViewModelFactory.create() // Simplified
//     // Simulate loading state in ViewModel for preview if needed
//     // MaterialTheme {
//         ProductDetailsScreen(viewModel = mockViewModel, productId = 1, onBackClick = {})
//     // }
// }

// @Preview
// @Composable
// fun ProductDetailsScreenPreview_Error() {
//      val mockViewModel = ProductDetailsViewModelFactory.create() // Simplified
//     // Simulate error state in ViewModel for preview
//     // MaterialTheme {
//         ProductDetailsScreen(viewModel = mockViewModel, productId = 1, onBackClick = {})
//     // }
// }

// @Preview
// @Composable
// fun ProductDetailsScreenPreview_WithData() {
//     val sampleProduct = Product(
//         id = 1,
//         title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
//         price = 109.95,
//         description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday.",
//         category = "men's clothing",
//         image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
//         rating = Rating(rate = 3.9, count = 120)
//     )
//     val mockViewModel = object : ProductDetailsViewModel(ProductRepository(ApiService())) { // More direct mock
//         override val product = MutableStateFlow(sampleProduct)
//         override val isLoading = MutableStateFlow(false)
//         override val error = MutableStateFlow<String?>(null)
//         // Override loadProductDetails to do nothing for this preview
//         override fun loadProductDetails(productId: Int) {}
//     }
//    // MaterialTheme {
//         ProductDetailsScreen(viewModel = mockViewModel, productId = 1, onBackClick = {})
//    // }
// }
