package com.vitalcare.serenia.features.iam.domain

interface ProfilePhotoReader {

    suspend fun read(photoUri: String): Result<ByteArray>
}
