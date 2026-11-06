package com.vitalcare.serenia.features.iam.domain

object PhoneNumber {

    const val COUNTRY_CODE = "+51"
    const val LOCAL_LENGTH = 9

    fun isValidLocal(digits: String): Boolean =
        digits.length == LOCAL_LENGTH && digits.startsWith("9") && digits.all { it.isDigit() }

    fun fromLocal(digits: String): String = COUNTRY_CODE + digits

    fun toLocal(phoneNumber: String): String = phoneNumber.removePrefix(COUNTRY_CODE)
}
