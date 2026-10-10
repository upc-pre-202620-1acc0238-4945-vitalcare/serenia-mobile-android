package com.vitalcare.serenia.features.iam.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CredentialsValidatorTest {

    @Test
    fun `accepts a valid email`() {
        assertTrue(CredentialsValidator.isValidIdentifier("lucia@ejemplo.com"))
    }

    @Test
    fun `accepts a peruvian mobile number`() {
        assertTrue(CredentialsValidator.isValidIdentifier("987 654 321"))
    }

    @Test
    fun `rejects text that is neither an email nor a mobile number`() {
        assertFalse(CredentialsValidator.isValidIdentifier("lucia"))
        assertFalse(CredentialsValidator.isValidIdentifier("12345"))
        assertFalse(CredentialsValidator.isValidIdentifier(""))
    }

    @Test
    fun `password needs at least 8 characters`() {
        assertFalse(CredentialsValidator.isValidPassword("1234567"))
        assertTrue(CredentialsValidator.isValidPassword("12345678"))
    }

    @Test
    fun `name must not be blank`() {
        assertFalse(CredentialsValidator.isValidName("  "))
        assertTrue(CredentialsValidator.isValidName("Lucía Mendoza"))
    }
}
