package com.example.API_REST2.DTO

data class UserRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val role: String?,
    val street: String,
    val num: String,
    val municipio: String,
    val provincia: String,
    val cp: String,
)