package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*


@Composable
fun TaskScreen(
    token: String,
    isAdmin: Boolean,
    onLogout: () -> Unit,
    onNavigateToCreateTask: () -> Unit,
    onNavigateToUpdateTask: (taskId: String) -> Unit
) {
    var tasksJson by remember { mutableStateOf<JsonArray?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    // Cargar tareas (el backend filtra según el usuario autenticado)
    LaunchedEffect(token) {
        coroutineScope.launch {
            try {
                val response: HttpResponse = client.get("https://api-rest2.onrender.com/tasks/getAll") {
                    headers { append(HttpHeaders.Authorization, "Bearer $token") }
                }
                if (response.status == HttpStatusCode.OK) {
                    val bodyText = response.bodyAsText()
                    tasksJson = Json.parseToJsonElement(bodyText).jsonArray
                } else {
                    errorMessage = "Error al cargar tareas: ${response.status}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Tareas ${if (isAdmin) "(Admin)" else "(Usuario)"}", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }
        tasksJson?.let { tasks ->
            // Depurar el primer elemento para ver su estructura
            if (tasks.isNotEmpty()) {
                val firstTask = tasks[0].jsonObject
                println("Estructura de la tarea: ${firstTask.keys}")
                firstTask.entries.forEach { entry ->
                    println("Campo: ${entry.key}, Valor: ${entry.value}")
                }
            }
            LazyColumn {
                items(tasks) { taskElement ->
                    val taskObj = taskElement.jsonObject
                    val id = taskObj["_id"]?.jsonPrimitive?.content ?: ""
                    val title = taskObj["title"]?.jsonPrimitive?.content ?: "Sin título"
                    val description = taskObj["description"]?.jsonPrimitive?.content ?: "Sin descripción"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, style = MaterialTheme.typography.subtitle1)
                                Text(description, style = MaterialTheme.typography.body2)
                            }
                            IconButton(onClick = {
                                println("Task ID seleccionado: $id") // Verificar el ID en la consola
                                if (id.isNotEmpty()) {
                                    onNavigateToUpdateTask(id)
                                } else {
                                    errorMessage = "Error: ID de tarea inválido"
                                }
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar tarea")
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    try {
                                        val deleteResponse: HttpResponse = client.delete("https://api-rest2.onrender.com/tasks/$id") {
                                            headers { append(HttpHeaders.Authorization, "Bearer $token") }
                                        }
                                        if (deleteResponse.status == HttpStatusCode.NoContent) {
                                            // Recargar tareas
                                            val updatedResponse: HttpResponse = client.get("https://api-rest2.onrender.com/tasks/getAll") {
                                                headers { append(HttpHeaders.Authorization, "Bearer $token") }
                                            }
                                            if (updatedResponse.status == HttpStatusCode.OK) {
                                                val bodyText = updatedResponse.bodyAsText()
                                                tasksJson = Json.parseToJsonElement(bodyText).jsonArray
                                            }
                                        } else {
                                            errorMessage = "Error al eliminar tarea: ${deleteResponse.status}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error de conexión: ${e.message}"
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateToCreateTask) {
                Text("Crear Tarea")
            }
            Button(onClick = onLogout) {
                Text("Cerrar Sesión")
            }
        }
    }
}

