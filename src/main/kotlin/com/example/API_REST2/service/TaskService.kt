package com.example.API_REST2.service

import com.example.API_REST2.DTO.TaskDTO
import com.example.API_REST2.DTO.TaskInsertDTO
import com.example.API_REST2.exception.*
import com.example.API_REST2.model.Task
import com.example.API_REST2.repository.TaskRepository
import com.example.API_REST2.util.DTOMapper
import com.example.API_REST2.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TaskService {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    fun createTask(taskInsertDTO: TaskInsertDTO): TaskDTO {

        if (!SecurityUtils.isAdmin() && taskInsertDTO.userId != SecurityUtils.getAuthenticatedUser()) {
            throw ForbiddenException("No tienes permiso crear esta tarea")
        }

        if (taskRepository.findByTitle(taskInsertDTO.title).isPresent) {
            throw ConflictException("La tarea ${taskInsertDTO.title} ya existe")
        }

        if (taskInsertDTO.title.isBlank()
            || taskInsertDTO.description.isBlank()
            || taskInsertDTO.userId.isBlank()){

            throw BadRequestException("Uno o más campos vacios")
        }

        val task = Task(
            null,
            title = taskInsertDTO.title,
            description = taskInsertDTO.description,
            state = taskInsertDTO.state,
            userId = taskInsertDTO.userId,
            completed = taskInsertDTO.completed
        )

        taskRepository.save(task)

        return DTOMapper.entityToTaskDTO(task)
    }

    fun loadAllTasks(): List<TaskDTO> {

        return if (SecurityUtils.isAdmin()) {
            taskRepository.findAll().map { DTOMapper.entityToTaskDTO(it) }
        } else {
            taskRepository.findByUserId(SecurityUtils.getAuthenticatedUser()).map { DTOMapper.entityToTaskDTO(it) }
        }
    }

    fun loadTaskById(id: String): TaskDTO {

        val task = taskRepository.findById(id).orElseThrow{NotFoundException("Tarea con ID ${id} no encontrada")}

        if (!SecurityUtils.isAdmin() && task.userId != SecurityUtils.getAuthenticatedUser()) {
            throw ForbiddenException("No tienes permiso para ver esta tarea")
        }

        return DTOMapper.entityToTaskDTO(task)

    }

    fun updateTask(id: String, taskDTO: TaskInsertDTO): TaskDTO {

        val task = taskRepository.findById(id).orElseThrow { NotFoundException("Tarea con ID $id no encontrada") }

        if (!SecurityUtils.isAdmin()) {
            if (task.userId != SecurityUtils.getAuthenticatedUser()) {
                throw ForbiddenException("No tienes permiso para modificar esta tarea")
            }
        }

        val updatedTask = task.copy(
            _id = task._id,
            title = taskDTO.title,
            description = taskDTO.description,
            state = taskDTO.state,
            completed = taskDTO.completed
        )
        taskRepository.save(updatedTask)

        return DTOMapper.entityToTaskDTO(updatedTask)
    }

    fun deleteTask(id: String) {

        val taskToDelete = taskRepository.findById(id).orElseThrow { NotFoundException("Tarea no encontrada") }

        if (!taskRepository.existsById(id)) {
            throw NotFoundException("Tarea con ID ${id} no existente")
        }

        if (!SecurityUtils.isAdmin() && taskToDelete.userId != SecurityUtils.getAuthenticatedUser()) {
            throw ForbiddenException("No tienes permiso para eliminar esta tarea")
        }

        taskRepository.deleteById(id)

    }
}