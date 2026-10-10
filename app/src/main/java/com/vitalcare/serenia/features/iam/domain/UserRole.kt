package com.vitalcare.serenia.features.iam.domain

/** Who is creating the account. It decides which experience the user gets after signing up. */
enum class UserRole {
    FAMILY,
    OLDER_ADULT
}
