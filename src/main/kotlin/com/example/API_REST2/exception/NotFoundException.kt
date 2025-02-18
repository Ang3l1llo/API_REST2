package com.example.API_REST2.exception

class NotFoundException (message: String): Exception("Not Found Exception (404). $message") {
}