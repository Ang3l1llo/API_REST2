package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (token: String, isAdmin: Boolean, username: String) -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val client = remember { HttpClient(CIO) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.h5, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Usuario") },
                modifier = Modifier.background(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.background(Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    val response: HttpResponse = client.post("https://api-rest2.onrender.com/users/login") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"username": "$nickname", "password": "$password"}""")
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                            val token = json["token"]?.jsonPrimitive?.content ?: ""
                            val isAdmin = json["isAdmin"]?.jsonPrimitive?.booleanOrNull ?: false
                            // Si el backend no retorna el username, usamos el nickname
                            val username = json["username"]?.jsonPrimitive?.content ?: nickname
                            onLoginSuccess(token, isAdmin, username)
                        }
                        HttpStatusCode.Unauthorized -> {
                            errorMessage = "Credenciales incorrectas, verifica tus datos"
                        }
                        else -> {
                            errorMessage = "Error: ${response.status}"
                        }
                    }
                }
            }) {
                Text("Ingresar")
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate", color = Color.White)
            }
        }
    }
}




