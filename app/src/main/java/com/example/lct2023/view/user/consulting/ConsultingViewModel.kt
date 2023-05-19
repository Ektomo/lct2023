package com.example.lct2023.view.user.consulting

import com.example.lct2023.gate.Gate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsultingViewModel @Inject constructor(
    val gate: Gate
) {
}