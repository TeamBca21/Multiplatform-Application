package com.example.kmmproject.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme // Or your custom theme
import com.example.ecommerce.shared.presentation.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Assuming you have a MaterialTheme or a custom theme defined in shared code
            // If not, you might need to define one here or in the shared code.
            // For simplicity, let's assume MaterialTheme is available or you'll adapt.
            // MaterialTheme { // This might need to be a custom theme if MaterialTheme is not set up in common
                AppNavigation()
            // }
        }
    }
}
