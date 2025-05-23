package com.example.ecommerce.shared.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ecommerce.shared.presentation.ui.screen.ProductListScreen
import com.example.ecommerce.shared.presentation.viewmodel.ProductListViewModel
import com.example.ecommerce.shared.presentation.viewmodel.ProductListViewModelFactory

object ProductListScreenVoyager : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProductListViewModel = remember { ProductListViewModelFactory.create() }
        val navigator = LocalNavigator.currentOrThrow

        ProductListScreen(
            viewModel = viewModel,
            onProductClick = { productId ->
                navigator.push(ProductDetailsScreenVoyager(productId))
            }
        )
    }
}
