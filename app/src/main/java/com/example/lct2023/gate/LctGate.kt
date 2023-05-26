package com.example.lct2023.gate

import com.example.lct2023.gate.model.RegisterRequestBody
import com.example.lct2023.gate.model.UserLoginResponseBody
import com.example.lct2023.gate.model.UserResponseBody
import com.example.lct2023.util.VoiceRecognizer
import kotlinx.serialization.decodeFromString
import javax.inject.Inject

class LctGate: Gate() {

    fun sendMsg(){
        makePostRequest("sendMsg", "")
    }

    fun registerUser(body: RegisterRequestBody): UserResponseBody {
        val result = makePostRequest(
            "register",
            body
        )
        return format.decodeFromString(result)
    }

    fun login(name: String): UserLoginResponseBody {
        val reslt = makeGetRequest("login/$name")
        return format.decodeFromString(reslt)
    }


}