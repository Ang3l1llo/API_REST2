package com.example.API_REST2.model

data class Address(
    val street: String,
    val number: String,
    val municipio: String,
    val provincia: String,
    val cp: String,
    val city: String
)