package com.example.lct2023.view.inspector.approved_list

import androidx.lifecycle.ViewModel
import com.example.lct2023.LctDataStore
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import com.example.lct2023.gate.model.inspector.InspectorWaitListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApprovedListViewModel@Inject constructor(
    private val dataStore: LctDataStore,
    private val gate: LctGate
): ViewModel() {




    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<InspectorWaitListResponse>) : State()
    }

    fun loadList(){

    }

}