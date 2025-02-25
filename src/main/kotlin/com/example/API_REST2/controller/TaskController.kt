package com.example.API_REST2.controller

import com.example.API_REST2.DTO.TaskDTO
import com.example.API_REST2.DTO.TaskInsertDTO
import com.example.API_REST2.service.TaskService
import com.example.API_REST2.service.TokenService
import com.example.API_REST2.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    private lateinit var taskService: TaskService

    @PostMapping("/create")
    fun create(
        httpRequest: HttpServletRequest,
        @RequestBody createTaskDTO: TaskInsertDTO
    ): ResponseEntity<TaskDTO>? {

        val task = taskService.createTask(createTaskDTO)

        return ResponseEntity(task, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: String, httpRequest: HttpServletRequest): ResponseEntity<TaskDTO> {


    }

    @GetMapping("/getAll")
    fun readAll(httpRequest: HttpServletRequest): ResponseEntity<List<TaskDTO>>{

    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody taskDTO: TaskInsertDTO, httpRequest: HttpServletRequest): ResponseEntity<TaskDTO> {

    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, httpRequest: HttpServletRequest): ResponseEntity<Void> {

    }
}