package com.example.API_REST2.exception

class UnauthorizedException(message: String) : Exception("Not authorized exception (401). $message") {
}