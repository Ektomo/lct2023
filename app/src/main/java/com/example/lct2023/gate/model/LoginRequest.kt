package com.example.lct2023.gate.model

@kotlinx.serialization.Serializable
data class RegisterRequestBody(
    val username: String,
//    val first_name: String,
//    val last_name: String,
    val password: String,
    val role: String,
    val supervisor_id: Int = 0
)

@kotlinx.serialization.Serializable
data class UserResponseBody(
//    val id: Long,
    val username: String,
//    val id: Int,
    val role: String = "client"
//    val first_name: String,
//    val last_name: String,
//    val is_active: Boolean,
//    val is_approved: Boolean,
//    val created_at: String,
//    val updated_at: String,
//    val full_name: String
)

@kotlinx.serialization.Serializable
data class UserRequestBody(
    val username: String,
    val password: String
//    val is_active: Boolean,
//    val is_approved: Boolean,
//    val created_at: String,
//    val updated_at: String,
//    val full_name: String
)

@kotlinx.serialization.Serializable
data class UserLoginResponseBody(
    val username: String,
    val id: Int,
//    val first_name: String,
//    val last_name: String,
//    val is_active: Boolean,
//    val is_approved: Boolean,
//    val created_at: String,
//    val updated_at: String,
//    val full_name: String,
    val role: String = "user"
)

@kotlinx.serialization.Serializable
data class SlotListResponseBody(
    val username: String,
    val id: Int,
//    val first_name: String,
//    val last_name: String,
//    val is_active: Boolean,
//    val is_approved: Boolean,
//    val created_at: String,
//    val updated_at: String,
//    val full_name: String,
    val role: String = "user"
)