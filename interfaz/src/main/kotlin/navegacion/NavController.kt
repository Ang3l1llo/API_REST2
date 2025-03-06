package navegacion

import androidx.compose.runtime.*
import screens.*

@Composable
fun NavController() {
    var currentScreen by remember { mutableStateOf("login") }
    var token by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") } // Guarda el username
    var selectedTaskId by remember { mutableStateOf("") }

    when (currentScreen) {
        "login" -> LoginScreen(
            onNavigateToRegister = { currentScreen = "register" },
            onLoginSuccess = { receivedToken, admin, user ->
                token = receivedToken
                isAdmin = admin
                username = user ?: ""
                currentScreen = "tasks"
            }
        )
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
        "tasks" -> TaskScreen(
            token = token,
            isAdmin = isAdmin,
            onLogout = {
                token = ""
                currentScreen = "login"
            },
            onNavigateToCreateTask = { currentScreen = "createTask" },
            onNavigateToUpdateTask = { taskId ->
                selectedTaskId = taskId
                currentScreen = "updateTask"
            }
        )
        "createTask" -> CreateTaskScreen(token = token, onBack = { currentScreen = "tasks" })
        "updateTask" -> UpdateTaskScreen(
            token = token,
            taskId = selectedTaskId,
            username = username,
            onBack = { currentScreen = "tasks" }
        )
    }
}

