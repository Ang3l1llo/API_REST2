package com.example.API_REST2.DTO

import org.springframework.data.mongodb.core.index.Indexed
import java.time.LocalDate

data class TaskInsertDTO (
    val title: String,
    val description: String,
    val state: Boolean,
    val userId: String,
    val completed: LocalDate
)