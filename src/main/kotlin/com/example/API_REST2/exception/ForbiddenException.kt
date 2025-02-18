package com.example.API_REST2.exception

class ForbiddenException (message: String): Exception("Forbidden Exception (403). $message") {
}