package com.example.API_REST2.util

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    fun getAuthenticatedUser(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.name // Devuelve el userId del token JWT
    }

    fun isAdmin(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.authorities.any { it.authority == "ROLE_ADMIN" }
    }
}