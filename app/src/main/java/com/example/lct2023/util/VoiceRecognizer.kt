package com.example.lct2023.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class VoiceRecognizer @Inject constructor(private val app: Context): RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextState())
    val state = _state.asStateFlow()

    val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

    fun startListening(lg: String){
        _state.update { VoiceToTextState() }

        if (!SpeechRecognizer.isRecognitionAvailable(app)){
            _state.update {
                it.copy(
                    error = "Не доступен сервис распознания речи"
                )
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, lg)
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }
    fun stopListening(){
        _state.update {
            it.copy(
                isSpeaking = false,
                isLoading = true
            )
        }
        recognizer.stopListening()
    }

    fun afterSaveText(){
        _state.update {
            it.copy(
                spokenText = ""
            )
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _state.update { it.copy(isSpeaking = false, isLoading = false) }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT){
            return
        }
        _state.update { it.copy(error = "Error code: $error") }
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.getOrNull(0)?.let {result ->
            _state.update {
                it.copy(
                    spokenText = result
                )
            }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}

data class VoiceToTextState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)