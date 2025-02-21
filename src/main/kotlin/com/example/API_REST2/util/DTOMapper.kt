package com.example.API_REST2.util

import com.example.API_REST2.DTO.UserDTO
import com.example.API_REST2.DTO.UserRegisterDTO
import com.example.API_REST2.model.Address
import com.example.API_REST2.model.User
import org.springframework.security.crypto.password.PasswordEncoder

object DTOMapper {

    fun usuarioRegisteredDTOToEntity(usuarioInsertDTO: UserRegisterDTO, passwordEncoder: PasswordEncoder) : User {
        return User(
            _id = null,
            username = usuarioInsertDTO.username,
            password = passwordEncoder.encode(usuarioInsertDTO.password),
            email = usuarioInsertDTO.email,
            rol = usuarioInsertDTO.rol ?: "",
            address = Address(
                street = usuarioInsertDTO.street,
                number = usuarioInsertDTO.num,
                municipio = usuarioInsertDTO.municipio,
                provincia = usuarioInsertDTO.provincia,
                cp = usuarioInsertDTO.cp

            ),
        )
    }

    fun entityToUsuarioRegisteredDTO(user: User) : UserRegisterDTO {

        return UserRegisterDTO(
            username = user.username,
            email = user.email,
            password = user.password,
            passwordRepeat = user.password,
            rol = user.rol,
            street = user.address.street,
            num = user.address.number,
            municipio = user.address.municipio,
            provincia = user.address.provincia,
            cp = user.address.cp,
        )
    }

    fun usuarioEntityToUsuarioDTO(usuario: User) : UserDTO {
        return UserDTO(
            username = usuario.username,
            email = usuario.email,
            rol = usuario.rol
        )
    }
}