package com.example.API_REST2.exception

class ConflictException (message: String): Exception("Conflict Exception (409). $message") {
}