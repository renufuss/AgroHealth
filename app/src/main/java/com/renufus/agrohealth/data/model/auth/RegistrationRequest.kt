package com.renufus.agrohealth.data.model.auth

data class RegistrationRequest(
    val email: String,
    val username: String,
    val password: String
)
