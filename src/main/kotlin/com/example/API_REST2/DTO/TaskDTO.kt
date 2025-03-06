package com.example.API_REST2.DTO

import java.time.LocalDate

data class TaskDTO (
    val _id : String?,
    val title: String,
    val description: String,
    val state: Boolean,
    val userId: String,
    val completed: LocalDate
)