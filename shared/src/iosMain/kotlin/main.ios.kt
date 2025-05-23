package com.example.ecommerce.shared // Package should match your project structure

import androidx.compose.ui.window.ComposeUIViewController
import com.example.ecommerce.shared.presentation.navigation.AppNavigation
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    // MaterialTheme { // Optional: If you have a MaterialTheme defined for iOS
        AppNavigation()
    // }
}
