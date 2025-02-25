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

        val task = taskService.loadTaskById(id)

        return ResponseEntity.ok(task)

    }

    @GetMapping("/getAll")
    fun readAll(httpRequest: HttpServletRequest): ResponseEntity<List<TaskDTO>>{

        val tasks = taskService.loadAllTasks()

        return ResponseEntity.ok(tasks)

    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody taskDTO: TaskInsertDTO, httpRequest: HttpServletRequest): ResponseEntity<TaskDTO> {

        val updateTask = taskService.updateTask(id, taskDTO)

        return ResponseEntity.ok(updateTask)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String, httpRequest: HttpServletRequest): ResponseEntity<Void> {

        taskService.deleteTask(id)

        return ResponseEntity.noContent().build()
    }
}