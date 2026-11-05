package com.vitalcare.serenia.features.iam.infrastructure

import com.vitalcare.serenia.features.iam.domain.User
import com.vitalcare.serenia.features.iam.domain.UserRole

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        role = UserRole.valueOf(this.role),
        fullName = this.fullName,
        phoneNumber = this.phoneNumber,
        photoUrl = this.photoUrl,
        locale = this.locale,
        timeZone = this.timeZone
    )
}
