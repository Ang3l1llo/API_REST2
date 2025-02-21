package navegacion

import androidx.compose.runtime.*
import screens.LoginScreen
import screens.RegisterScreen

@Composable
fun NavController() {
    var currentScreen by remember { mutableStateOf("login") }

    when (currentScreen) {
        "login" -> LoginScreen(onNavigateToRegister = { currentScreen = "register" })
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
    }
}