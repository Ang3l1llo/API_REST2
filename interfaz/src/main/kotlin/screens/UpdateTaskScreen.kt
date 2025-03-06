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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun UpdateTaskScreen(
    token: String,
    taskId: String,
    username: String,
    onBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(username) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }

    // Depuración del ID recibido
    LaunchedEffect(Unit) {
        println("UpdateTaskScreen inicializada con taskId: '$taskId'")
    }

    // Cargar datos de la tarea a actualizar
    LaunchedEffect(taskId) {
        if (taskId.isEmpty()) {
            errorMessage = "Error: No se recibió un ID de tarea válido"
            isLoading = false
            return@LaunchedEffect
        }

        try {
            println("Intentando cargar tarea con ID: $taskId")
            val response: HttpResponse = client.get("https://api-rest2.onrender.com/tasks/$taskId") {
                headers { append(HttpHeaders.Authorization, "Bearer $token") }
            }

            println("Respuesta recibida: ${response.status}")

            if (response.status == HttpStatusCode.OK) {
                val bodyText = response.bodyAsText()
                println("Cuerpo de respuesta: $bodyText")

                val taskJson = Json.parseToJsonElement(bodyText).jsonObject
                titulo = taskJson["title"]?.jsonPrimitive?.content ?: ""
                descripcion = taskJson["description"]?.jsonPrimitive?.content ?: ""
                userId = taskJson["userId"]?.jsonPrimitive?.content ?: username

                println("Tarea cargada exitosamente: título=$titulo, descripción=$descripcion, userId=$userId")
            } else {
                errorMessage = "Error al cargar la tarea: ${response.status}"
                println("Error en respuesta: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            errorMessage = "Error de conexión: ${e.message}"
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Actualizar Tarea", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (titulo.isEmpty() || descripcion.isEmpty()) {
                        errorMessage = "Todos los campos son obligatorios"
                    } else {
                        scope.launch {
                            try {
                                println("Actualizando tarea con ID: $taskId")
                                val requestUrl = "https://api-rest2.onrender.com/tasks/$taskId"
                                println("URL de solicitud: $requestUrl")

                                val requestBody = """
                                    {
                                        "title": "$titulo",
                                        "description": "$descripcion",
                                        "state": false,
                                        "userId": "$userId",
                                        "completed": "2025-03-05"
                                    }
                                """.trimIndent()
                                println("Cuerpo de solicitud: $requestBody")

                                val response: HttpResponse = client.put(requestUrl) {
                                    contentType(ContentType.Application.Json)
                                    headers { append(HttpHeaders.Authorization, "Bearer $token") }
                                    setBody(requestBody)
                                }

                                println("Código de respuesta: ${response.status}")

                                if (response.status == HttpStatusCode.OK) {
                                    println("Actualización exitosa")
                                    onBack()
                                } else {
                                    val responseBody = response.bodyAsText()
                                    errorMessage = "Error al actualizar tarea: ${response.status}"
                                    println("Error en respuesta: $responseBody")
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error de conexión: ${e.message}"
                                e.printStackTrace()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar Tarea")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colors.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}


