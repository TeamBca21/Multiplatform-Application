package com.example.ecommerce.shared.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
fun AppNavigation() {
    Navigator(screen = ProductListScreenVoyager) { navigator ->
        SlideTransition(navigator) // Or use other transitions like FadeTransition, ScaleTransition
    }
}
