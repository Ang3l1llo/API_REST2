package com.example.API_REST2.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("Task")
data class Task (
    @BsonId
    val _id : String?,
    val title: String,
    val description: String,
    val state: Boolean,
    val userId: String,
    val completed: LocalDate
){

}