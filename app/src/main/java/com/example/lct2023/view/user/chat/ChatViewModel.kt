package com.example.lct2023.view.user.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.gate.LctGate
import com.example.lct2023.util.VoiceRecognizer
import com.example.lct2023.view.ViewStateClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class MsgAuthor {
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
) : ViewModel() {

    private val _state: MutableStateFlow<ViewStateClass<List<ChatMsgItem>>> =
        MutableStateFlow(ViewStateClass.Loading)
    val state = _state.asStateFlow()

    private var msgs = mutableListOf(
        ChatMsgItem(
            MsgAuthor.Bot,
            "Задайте свой вопрос нажимая на микрофон или напишите его в поле внизу"
        )
    )


    init {
        loadList()
    }

    fun loadList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = ViewStateClass.Data(msgs)
                msgs =
                    (_state.value as ViewStateClass.Data<List<ChatMsgItem>>).data as MutableList<ChatMsgItem>
            } catch (e: Exception) {
                _state.value = ViewStateClass.Error(e)
            }
        }
    }

    fun mockFirstQuestion(msg: ChatMsgItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_state.value is ViewStateClass.Data<*>) {
                    _state.value = ViewStateClass.Loading
                    delay(1000)


                    val answer1 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "1) Прием на работу молодежи в размере 2 процентов от среднесписочной численности работников.",
                        Date()
                    )
                    val answer2 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "2) Ежеквартальное представление информации о выполнении квоты для приема на работу молодежи посредством Интерактивного портала Центра занятости населения города Москвы.",
                        Date()
                    )
                    val answer3 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "Не найдено нужного ответа давайте запишемся на консультацию. Нажмите сюда для записи.",
                        Date()
                    )

                    msgs.add(msg)
                    msgs.add(answer1)
                    msgs.add(answer2)
                    msgs.add(answer3)
                    recognizer.afterSaveText()
                    _state.value = ViewStateClass.Data(msgs)
                } else {
                    throw IllegalStateException("Попытка добавить ообщение из состояния ${_state.value}")
                }
            } catch (e: Exception) {
                _state.value = ViewStateClass.Error(e)
            }
        }
    }


    fun mockSecondQuestion(msg: ChatMsgItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_state.value is ViewStateClass.Data<*>) {
                    _state.value = ViewStateClass.Loading
                    delay(1000)

                    val answer1 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "1) п. 1, 3 ст. 2 Закона города Москвы от 22.12.2004 № 90 \"О квотировании рабочих мест\": \"1. Квотирование рабочих мест осуществляется для инвалидов, признанных таковыми федеральными учреждениями медико-социальной экспертизы, в порядке и на условиях, установленных Правительством Российской Федерации, и молодежи следующих категорий: несовершеннолетние в возрасте от 14 до 18 лет; лица из числа детей-сирот и детей, оставшихся без попечения родителей, в возрасте до 23 лет; выпускники учреждений начального и среднего профессионального образования в возрасте от 18 до 24 лет, высшего профессионального образования в возрасте от 21 года до 26 лет, ищущие работу впервые. 3. Выполнением квоты для приема на работу (далее - квота) считается: 1) в отношении инвалидов ...; 2) в отношении категорий молодежи, указанных в части 1 настоящей статьи, - трудоустройство работодателем молодежи, подтвержденное заключением трудового договора, действие которого в текущем месяце составило не менее 15 дней, либо уплата ежемесячно в бюджет города Москвы компенсационной стоимости квотируемого рабочего места в размере прожиточного минимума для трудоспособного населения, определенного в городе Москве на день ее уплаты в порядке, установленном нормативными правовыми актами города Москвы\".\n.",
                        Date()
                    )
                    val answer2 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "2) п. 1, 3 ст. 3 Закона города Москвы от 22.12.2004 № 90 \"О квотировании рабочих мест\": \"1. Работодателям, осуществляющим деятельность на территории города Москвы, у которых среднесписочная численность работников составляет более 100 человек, устанавливается квота в размере 4 процентов от среднесписочной численности работников: 2 процента - для трудоустройства инвалидов и 2 процента - для трудоустройства категорий молодежи, указанных в части 1 статьи 2 настоящего Закона. 3. В случае если количество инвалидов, принятых на квотируемые рабочие места, составляет более 2 процентов от среднесписочной численности работников, количество квотируемых рабочих мест в отношении категорий молодежи, указанных в части 1 статьи 2 настоящего Закона, уменьшается на соответствующую величину\".\n",
                        Date()
                    )
                    val answer3 = ChatMsgItem(
                        MsgAuthor.Bot,
                        "Не найдено нужного ответа давайте запишемся на консультацию. Нажмите сюда для записи.",
                        Date()
                    )

                    msgs.add(answer1)
                    msgs.add(answer2)
                    msgs.add(answer3)
                    recognizer.afterSaveText()
                    _state.value = ViewStateClass.Data(msgs)
                } else {
                    throw IllegalStateException("Попытка добавить ообщение из состояния ${_state.value}")
                }
            } catch (e: Exception) {
                _state.value = ViewStateClass.Error(e)
            }
        }
    }

}