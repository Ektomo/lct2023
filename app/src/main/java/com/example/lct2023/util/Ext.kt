package com.example.lct2023.util

import androidx.compose.ui.text.toLowerCase
import com.example.lct2023.view.login.UserType
import java.util.*

fun String.toUserType(): UserType? {
    return when(this.lowercase(Locale.ROOT)){
        "клиент" -> UserType.client
        "инспектор" -> UserType.inspector
        else -> null
    }
}