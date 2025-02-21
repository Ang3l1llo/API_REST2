
import com.example.API_REST2.DTO.LoginUserDTO
import com.example.API_REST2.DTO.UserDTO
import com.example.API_REST2.DTO.UserRegisterDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private const val BASE_URL = "http://localhost:8080/users"

    suspend fun registerUser(registerDTO: UserRegisterDTO): UserDTO {
        return client.post("$BASE_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }.body()
    }

    suspend fun loginUser(loginDTO: LoginUserDTO): String {
        return client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }.body()
    }
}