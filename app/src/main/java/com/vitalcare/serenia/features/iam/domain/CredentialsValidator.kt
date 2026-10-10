package com.vitalcare.serenia.features.iam.domain

/** Basic form rules shared by the sign up and sign in screens. */
object CredentialsValidator {

    const val MIN_PASSWORD_LENGTH = 8

    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Peruvian mobile numbers have 9 digits and start with 9
    private val phoneRegex = Regex("^9\\d{8}$")

    fun isValidName(name: String): Boolean = name.trim().length >= 2

    /** Accepts an email or a mobile number, as the field says "Correo o celular". */
    fun isValidIdentifier(identifier: String): Boolean {
        val value = identifier.trim()
        return emailRegex.matches(value) || phoneRegex.matches(value.replace(" ", ""))
    }

    fun isValidPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
}
