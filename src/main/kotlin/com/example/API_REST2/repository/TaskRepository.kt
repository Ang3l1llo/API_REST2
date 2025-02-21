package com.example.API_REST2.repository

import com.example.API_REST2.model.Task
import java.util.*

interface TaskRepository {

    fun findByTitle(title: String): Optional<Task>
}