package com.example.lct2023.view.inspector.approved_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.LctDataStore
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import com.example.lct2023.gate.model.inspector.InspectorWaitListResponse
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.user.chat.ChatMsgItem
import com.example.lct2023.view.user.chat.MsgAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ApprovedListViewModel@Inject constructor(
    private val dataStore: LctDataStore,
    private val gate: LctGate
): ViewModel() {


    private val _state: MutableStateFlow<ViewStateClass<List<InspectorWaitListResponse>>> = MutableStateFlow(
        ViewStateClass.Loading)
    val state = _state.asStateFlow()

init {
    loadList()
}

    sealed class State {
        object Loading : State()
        data class Error(val e: Exception) : State()
        data class Data(val data: List<InspectorWaitListResponse>) : State()
    }



    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            try{

                _state.value = ViewStateClass.Loading
                gate.getApprovedList()
//                    _state.value = ViewStateClass.Data()

            }catch(e: Exception){
                _state.value = ViewStateClass.Error(e)
            }
        }

    }

}