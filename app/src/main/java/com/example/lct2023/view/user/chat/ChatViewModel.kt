package com.example.lct2023.view.user.chat

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import com.example.lct2023.util.VoiceRecognizer
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

enum class MsgAuthor{
    Bot, User
}

data class ChatMsgItem(
    val author: MsgAuthor,
    val msg: String,
    val date: Date = Date()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    val gate: LctGate,
    val recognizer: VoiceRecognizer
): ViewModel() {

    private val _state: MutableStateFlow<ViewStateClass<List<ChatMsgItem>>> = MutableStateFlow(ViewStateClass.Loading)
    val state = _state.asStateFlow()

    private var msgs = mutableListOf<ChatMsgItem>()


    init {
        loadList()
    }

    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _state.value = ViewStateClass.Data(msgs)
                msgs = (_state.value as ViewStateClass.Data<List<ChatMsgItem>>).data as MutableList<ChatMsgItem>
            }catch (e: Exception){
                _state.value = ViewStateClass.Error(e)
            }
        }
    }
    fun sendMsg(msg: ChatMsgItem){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                if (_state.value is ViewStateClass.Data<*>){
                    _state.value = ViewStateClass.Loading
                    delay(1000)
                    val answer = ChatMsgItem(MsgAuthor.Bot, "${msg.msg}\n${msg.msg}\n${msg.msg}", Date())
                    msgs.add(msg)
                    msgs.add(answer)
                    recognizer.afterSaveText()
                    _state.value = ViewStateClass.Data(msgs)
                }else{
                    throw IllegalStateException("Попытка добавить ообщение из состояния ${_state.value}")
                }
            }catch(e: Exception){
                _state.value = ViewStateClass.Error(e)
            }
        }
    }

}