package com.example.ecommerce.shared.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.shared.data.model.Product
// import org.jetbrains.compose.ui.tooling.preview.Preview // Not available in commonMain for Material 3 yet
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
// If Kamel or Coil is not yet set up, a simple AsyncImage or a placeholder can be used.
// For now, we'll use a placeholder.
// To use a real image loading library, you'd add its dependency and use its Composable.

@Composable
@OptIn(ExperimentalMaterial3Api::class) // Needed for Card onClick
fun ProductItem(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick, // Make the Card clickable
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Product Image
            KamelImage(
                resource = asyncPainterResource(data = product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Consistent height for items
                    .background(Color.LightGray, RoundedCornerShape(4.dp)), // Background for loading/error
                contentScale = ContentScale.Crop, // Or ContentScale.Fit, depending on preference
                onLoading = {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.size(30.dp))
                    }
                },
                onFailure = {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Failed to load image", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Product Title
            Text(
                text = product.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Product Price
            Text(
                text = "$${product.price}", // Simple currency formatting
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Product Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107), // Yellow star color
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating.rate} (${product.rating.count})",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
             Spacer(modifier = Modifier.height(8.dp))

            // Category (Optional, but good for context like Amazon)
            Text(
                text = "Category: ${product.category}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Preview for ProductItem (Requires a way to run previews in commonMain or platform-specific source sets)
// @Preview // This annotation might not work directly in commonMain with Material 3 yet.
// @Composable
// fun ProductItemPreview() {
//     val sampleProduct = Product(
//         id = 1,
//         title = "Sample Product Title - Very Long Title That Overflows to Test Ellipsis Feature",
//         price = 29.99,
//         description = "This is a sample product description.",
//         category = "Electronics",
//         image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg", // Example image
//         rating = Rating(rate = 4.5, count = 120)
//     )
//     // MaterialTheme { // Ensure a MaterialTheme is applied for previews
//         ProductItem(product = sampleProduct, onClick = {})
//     // }
// }
