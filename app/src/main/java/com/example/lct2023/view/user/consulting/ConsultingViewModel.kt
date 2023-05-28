package com.example.lct2023.view.user.consulting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.gate.LctGate
import com.example.lct2023.gate.model.user.ConsultTheme
import com.example.lct2023.gate.model.user.ControlType
import com.example.lct2023.gate.model.user.Kno
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject





@HiltViewModel
class ConsultingViewModel @Inject constructor(
    val gate: LctGate
) : ViewModel() {



    private val _state: MutableStateFlow<ViewStateClass<Pair<List<Kno>, List<ConsultTheme>>>> = MutableStateFlow(
        ViewStateClass.Loading)
    val state = _state.asStateFlow()
    var slots: Map<String, List<List<Slot>>> = mapOf()
    var slotsOrder: List<Pair<Int, String>> = listOf()

    var kno: Kno? = null
    var controlType: ControlType? = null
    var consult: ConsultTheme? = null


    init {
        loadList()

    }

    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                ViewStateClass.Loading
            }
            try{
//                delay(1000)
//                slots = mockSlots()
//                slotsOrder = slots.keys.map { it.take(2).toInt() to it }.sortedBy { it.first }

                val knoList = gate.getSupervisors()
                val themeList = gate.getTopicList()

                _state.update {
                    ViewStateClass.Data(knoList to themeList)
                }
                //Записать данные на случай ошибки в переменную массива
            }catch (e: Exception){
                _state.update {
                    ViewStateClass.Error(e)
                }
            }

        }
    }

    fun loadFreeSlots(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val tempState = _state.value
            _state.update {
                ViewStateClass.Loading
            }
            try{

//                delay(1000)
                val serverSlots = gate.getSlots(id)

                val format = SimpleDateFormat("MM,LLLL", Locale.getDefault())
                slots = serverSlots.groupBy { format.format(it.date)}.mapValues { it.value.chunked(3) }

                slotsOrder = slots.keys.map { it.take(2).toInt() to it }.sortedBy { it.first }


                _state.update {
                    tempState
                }
                //Записать данные на случай ошибки в переменную массива
            }catch (e: Exception){
                _state.update {
                    ViewStateClass.Error(e)
                }
            }

        }
    }

    fun reserveMeet(idSlot: Int, idTopic: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val tempState = _state.value
            _state.update {
                ViewStateClass.Loading
            }
            try{

                gate.reserveMeet(slotId = "$idSlot", topicId = "$idTopic")


                _state.update {
                    tempState
                }
                //Записать данные на случай ошибки в переменную массива
            }catch (e: Exception){
                _state.update {
                    ViewStateClass.Error(e)
                }
            }

        }
    }



}


//fun mockSlots(): Map<String, List<List<Slot>>> {
//    val calendar = Calendar.getInstance()
//    calendar.time = Date()
//    var curMonth = 0
//    calendar.run {
//        set(Calendar.DAY_OF_MONTH, 1)
//        curMonth = calendar.get(Calendar.MONTH)
//    }
//
//    val slotsTime = listOf(
//        Time("8:00-9:00"),
//        Time("9:00-10:00"),
//        Time("10:00-11:00"),
//        Time("11:00-12:00"),
//        Time("12:00-13:00"),
//        Time("13:00-14:00"),
//        Time("14:00-15:00"),
//        Time("15:00-16:00"),
//        Time("16:00-17:00"),
//        Time("17:00-18:00"),
//        Time("18:00-19:00"),
//        Time("19:00-20:00")
//    )
//
//    val slots: MutableList<Slot> = mutableListOf()
//
//    while (curMonth == calendar.get(Calendar.MONTH)){
//        slots.add(Slot(calendar.time, slotsTime))
//        calendar.add(Calendar.DAY_OF_MONTH, 1)
//    }
//    curMonth += 1
//
//    while (curMonth == calendar.get(Calendar.MONTH)){
//        slots.add(Slot(calendar.time, slotsTime))
//        calendar.add(Calendar.DAY_OF_MONTH, 1)
//    }
//
//    curMonth += 1
//
//    while (curMonth == calendar.get(Calendar.MONTH)){
//        slots.add(Slot(calendar.time, slotsTime))
//        calendar.add(Calendar.DAY_OF_MONTH, 1)
//    }
//
//    val format = SimpleDateFormat("MM,LLLL", Locale.getDefault())
//    val answer = slots.groupBy { format.format(it.date)}.mapValues { it.value.chunked(3) }
//
//
//    return answer
//
//}



//var knoList = listOf(
//    Kno(
//        "МОСКОВСКАЯ АДМИНИСТРАТИВНАЯ ДОРОЖНАЯ ИНСПЕКЦИЯ",
//        listOf(
//            ControlType(
//                "Региональный государственный контроль (надзор) в сфере перевозок пассажиров и багажа легковым такси на территории города Москвы\n",
////                listOf(
////                    ConsultTheme("Выполнение предписания, выданного по итогам контрольного (надзорного) мероприятия\n"),
////                    ConsultTheme("Наличие и (или) содержание обязательных требований\n"),
////                )
//            ),
//            ControlType(
//                "Муниципальный кoнтроль на автомобильном транспорте, городском наземном электрическом транспорте и в дорожном хозяйстве на территории города Москвы\n",
////                listOf(
////                    ConsultTheme("Выполнение предписания, выданного по итогам контрольного (надзорного) мероприятия\n"),
////                    ConsultTheme("Наличие и (или) содержание обязательных требований\n"),
////                )
//            ),
//            ControlType(
//                "Региональный государственный контроль (надзор) в сфере перевозок пассажиров и багажа легковым такси на территории города Москвы\n",
////                listOf(
////                    ConsultTheme("Выполнение предписания, выданного по итогам контрольного (надзорного) мероприятия\n"),
////                    ConsultTheme("Наличие и (или) содержание обязательных требований\n"),
////                )
//            ),
//            ControlType(
//                "Муниципальный кoнтроль на автомобильном транспорте, городском наземном электрическом транспорте и в дорожном хозяйстве на территории города Москвы\n",
////                listOf(
////                    ConsultTheme("Выполнение предписания, выданного по итогам контрольного (надзорного) мероприятия\n"),
////                    ConsultTheme("Наличие и (или) содержание обязательных требований\n"),
////                )
//            ),
//
//        )
//    ),
//    Kno(
//        "ДЕПАРТАМЕНТ ЗДРАВООХРАНЕНИЯ ГОРОДА МОСКВЫ",
//        listOf(
//            ControlType(
//                "Региональный государственный контроль за применением цен на лекарственные препараты, включенные в перечень жизненно необходимых и важнейших лекарственных препаратов\n",
////                listOf(
////                    ConsultTheme("Гарантии и защита прав контролируемых лиц\n"),
////                    ConsultTheme("Иные вопросы, связанные с осуществлением регионального государственного контроля (надзора)\n"),
////                )
//            )
//        )
//    )
//)