package com.example.ecommerce.shared.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ecommerce.shared.presentation.ui.screen.ProductDetailsScreen
import com.example.ecommerce.shared.presentation.viewmodel.ProductDetailsViewModel
import com.example.ecommerce.shared.presentation.viewmodel.ProductDetailsViewModelFactory

data class ProductDetailsScreenVoyager(val productId: Int) : Screen {
    @Composable
    override fun Content() {
        // ViewModel is remembered; if productId could change for the *same* screen instance,
        // viewModel should be keyed with productId: remember(productId) { ... }
        // However, Voyager typically creates new Screen instances, so this simple remember is usually fine.
        val viewModel: ProductDetailsViewModel = remember { ProductDetailsViewModelFactory.create() }
        val navigator = LocalNavigator.currentOrThrow

        ProductDetailsScreen(
            viewModel = viewModel,
            productId = productId,
            onBackClick = {
                navigator.pop()
            }
        )
    }
}
