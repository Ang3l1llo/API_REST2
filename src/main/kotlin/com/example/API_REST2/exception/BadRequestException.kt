package com.example.API_REST2.exception

class BadRequestException (message: String): Exception("Bad Request Exception (400). $message") {
}