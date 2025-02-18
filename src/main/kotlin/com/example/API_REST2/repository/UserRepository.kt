package com.example.API_REST2.repository

import com.example.API_REST2.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, String> {

    fun findByUsername(username: String) : Optional<User>
}