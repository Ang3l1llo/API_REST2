import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import navegacion.NavController

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Interfaz") {
        NavController()
    }
}


