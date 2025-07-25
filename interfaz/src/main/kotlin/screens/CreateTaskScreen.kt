package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch

@Composable
fun CreateTaskScreen(token: String, onBack: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var assignedUsername by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Tarea", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = assignedUsername,
                onValueChange = { assignedUsername = it },
                label = { Text("Username a asignar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response: HttpResponse = client.post("https://api-rest2.onrender.com/tasks/create") {
                                contentType(ContentType.Application.Json)
                                headers {
                                    append(HttpHeaders.Authorization, "Bearer $token")
                                }
                                setBody(
                                    """{
                                        "title": "$titulo",
                                        "description": "$descripcion",
                                        "state": false,
                                        "userId": "$assignedUsername",
                                        "completed": "2025-03-05"
                                    }"""
                                )
                            }
                            if (response.status == HttpStatusCode.Created) {
                                onBack()  // Vuelve a la pantalla de tareas
                            } else {
                                errorMessage = "Error al crear tarea: ${response.status}"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error de conexión: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Tarea")
            }
            errorMessage?.let { Text(it, color = MaterialTheme.colors.error) }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}

