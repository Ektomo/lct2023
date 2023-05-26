package com.example.lct2023.view.user.consulting

import androidx.lifecycle.ViewModel
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsultingViewModel @Inject constructor(
    val gate: LctGate
): ViewModel() {
}