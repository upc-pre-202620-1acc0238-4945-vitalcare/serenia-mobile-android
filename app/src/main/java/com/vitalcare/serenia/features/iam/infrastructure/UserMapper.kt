package com.vitalcare.serenia.features.iam.infrastructure

import com.vitalcare.serenia.BuildConfig
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.User
import com.vitalcare.serenia.features.iam.domain.UserRole
import java.util.Locale

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        role = UserRole.valueOf(this.role),
        fullName = this.fullName,
        phoneNumber = this.phoneNumber,
        birthDate = this.birthDate?.toBirthDate(),
        photoUrl = this.photoUrl?.toAbsoluteUrl(),
        locale = this.locale,
        timeZone = this.timeZone
    )
}

fun BirthDate.toRequestValue(): String {
    return String.format(Locale.ROOT, "%04d-%02d-%02d", year, month, day)
}

private fun String.toAbsoluteUrl(): String {
    return if (startsWith("/")) BuildConfig.API_BASE_URL.substringBefore(API_PATH) + this else this
}

private const val API_PATH = "/api/"

private fun String.toBirthDate(): BirthDate {
    val parts = split("-")
    return BirthDate(year = parts[0].toInt(), month = parts[1].toInt(), day = parts[2].toInt())
}
