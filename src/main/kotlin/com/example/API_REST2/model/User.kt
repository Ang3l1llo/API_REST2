package com.example.API_REST2.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("User")
data class User(
    @BsonId
    val _id : String?,
    @Indexed(unique = true)
    val username: String,
    val password: String,
    @Indexed(unique = true)
    val email: String,
    val role: String = "USER",
    val address: Address

) {



}