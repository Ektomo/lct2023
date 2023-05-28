package com.example.lct2023.view.inspector.waiting_list

import androidx.lifecycle.ViewModel
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaitListViewModel @Inject constructor(
    private val gate: LctGate
): ViewModel() {
}

