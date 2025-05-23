import com.example.ecommerce.shared.presentation.navigation.AppNavigation
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        // MaterialTheme { // Optional: Wrap with MaterialTheme if you have one for web
            AppNavigation()
        // }
    }
}
