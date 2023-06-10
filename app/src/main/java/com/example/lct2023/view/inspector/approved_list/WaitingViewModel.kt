package com.example.lct2023.view.inspector.approved_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.LctDataStore
import com.example.lct2023.gate.LctGate
import com.example.lct2023.gate.model.user.OpenSlotResponse
import com.example.lct2023.view.ViewStateClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(
    private val dataStore: LctDataStore,
    private val gate: LctGate
) : ViewModel() {


    private val _state: MutableStateFlow<ViewStateClass<List<OpenSlotResponse>>> = MutableStateFlow(
        ViewStateClass.Loading
    )
    val state = _state.asStateFlow()

    val mockCollection = MutableList(10){"Видеовстреча 2 июня на тему региональные споры соседей в тропиках"}
    var mockStateList = mutableStateListOf<String>()

    init {
        loadList()
        mockStateList.addAll(mockCollection)
    }

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<OpenSlotResponse>) : State()
    }


    fun loadList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                _state.value = ViewStateClass.Loading
//                _state.update {
//                    ViewStateClass.Data(gate.getApprovedList())
//                }
                _state.update {
                    ViewStateClass.Data(listOf())
                }
//                    _state.value = ViewStateClass.Data()

            } catch (e: Exception) {
                _state.value = ViewStateClass.Error(e)
            }
        }

    }

}