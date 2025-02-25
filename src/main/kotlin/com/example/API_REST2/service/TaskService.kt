package com.example.API_REST2.service

import com.example.API_REST2.DTO.TaskDTO
import com.example.API_REST2.DTO.TaskInsertDTO
import com.example.API_REST2.exception.BadRequestException
import com.example.API_REST2.exception.ConflictException
import com.example.API_REST2.exception.NotFoundException
import com.example.API_REST2.model.Task
import com.example.API_REST2.repository.TaskRepository
import com.example.API_REST2.util.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TaskService {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    fun createTask(taskInsertDTO: TaskInsertDTO): TaskDTO {

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

        return taskRepository.findAll().map { DTOMapper.entityToTaskDTO(it) }

    }

    fun loadTaskById(id: String): TaskDTO {

        val task = taskRepository.findById(id).orElseThrow{NotFoundException("Tarea con ID ${id} no encontrada")}

        return DTOMapper.entityToTaskDTO(task)

    }

    fun updateTask(id: String, taskDTO: TaskInsertDTO): TaskDTO {

        val existingTask = taskRepository.findById(id).orElseThrow{NotFoundException("Tarea con ID ${id} no encontrada")}

        val updatedTask = existingTask.copy(
            title = taskDTO.title,
            description = taskDTO.description,
            state = taskDTO.state,
            userId = taskDTO.userId,
            completed = taskDTO.completed
        )

        taskRepository.save(updatedTask)

        return DTOMapper.entityToTaskDTO(updatedTask)


    }

    fun deleteTask(id: String) {

        if (!taskRepository.existsById(id)) {
            throw NotFoundException("Tarea con ID ${id} no existente")
        }
        taskRepository.deleteById(id)

    }
}