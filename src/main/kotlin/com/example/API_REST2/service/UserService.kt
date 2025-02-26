package com.example.API_REST2.service


import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import com.example.API_REST2.DTO.UserDTO
import com.example.API_REST2.DTO.UserRegisterDTO
import com.example.API_REST2.exception.BadRequestException
import com.example.API_REST2.exception.ConflictException
import com.example.API_REST2.exception.NotFoundException
import com.example.API_REST2.exception.UnauthorizedException
import com.example.API_REST2.model.User
import com.example.API_REST2.repository.UserRepository
import com.example.API_REST2.util.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var apiService: ExternalApiService


    override fun loadUserByUsername(username: String?): UserDetails {
        var user: User = userRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.role)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UserRegisterDTO) : UserDTO? {

        if(usuarioInsertadoDTO.username.isBlank()
            || usuarioInsertadoDTO.email.isBlank()
            || usuarioInsertadoDTO.password.isBlank()
            || usuarioInsertadoDTO.passwordRepeat.isBlank()){

            throw BadRequestException("Uno o más campos vacios")
        }

        if(userRepository.findByUsername(usuarioInsertadoDTO.username).isPresent){
            throw ConflictException("Usuario ${usuarioInsertadoDTO.username} ya está registrado")
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        if (!emailRegex.matches(usuarioInsertadoDTO.email)) {
            throw BadRequestException("El email no es válido")
        }

        if (usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("Las contraseñas no coinciden")
        }


        val usuario = DTOMapper.usuarioRegisteredDTOToEntity(usuarioInsertadoDTO, passwordEncoder)
        val datosProvincias = apiService.obtenerDatosDesdeApi()

        var cpro = ""

        if(datosProvincias?.data != null) {
            datosProvincias.data.stream().filter {
                if (it.PRO == usuario.address.provincia.uppercase()) {
                    cpro = it.CPRO
                }
                it.PRO == usuario.address.provincia.uppercase()
            }.findFirst().orElseThrow {
                NotFoundException("Provincia ${usuario.address.provincia.uppercase()} no válida.")
            }
        }

        val datosMunicipios = apiService.obtenerMunicipios(cpro)

        if(datosMunicipios?.data != null) {
            datosMunicipios.data.stream().filter {
                it.DMUN50 == usuario.address.municipio.uppercase()
            }.findFirst().orElseThrow {
                NotFoundException("Municipio ${usuario.address.municipio.uppercase()} no válido.")
            }
        }

        userRepository.insert(usuario)

        return DTOMapper.usuarioEntityToUsuarioDTO(usuario)
    }
}