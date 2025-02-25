package com.example.API_REST2.repository

import com.example.API_REST2.model.Task
import com.example.API_REST2.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository: MongoRepository<Task, String> {

    fun findByTitle(title: String): Optional<Task>
}