package com.example.lct2023.view.user.consulting

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lct2023.gate.model.user.ConsultTheme
import com.example.lct2023.gate.model.user.ControlType
import com.example.lct2023.gate.model.user.Kno
import com.example.lct2023.ui.theme.ptSerifFontFamily
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView
import com.example.lct2023.view.util.SearchBoxView
import com.example.lct2023.view.util.SlotCalendarView
import com.example.lct2023.view.util.Time
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConsultingView(vm: ConsultingViewModel) {

    val viewState by vm.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val widthItem = (screenWidth.toDouble() / 3.15).dp
    val format = SimpleDateFormat("dd MMMM в EEE", Locale.getDefault())
    val showOk by vm.showOk.collectAsState()



    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {
                var selectedKno: Kno? by remember {
                    mutableStateOf(vm.kno)
                }


                var showDialog by remember {
                    mutableStateOf(false)
                }

                var selectedControl: ControlType? by remember {
                    mutableStateOf(vm.controlType)
                }

                var selectedTheme: ConsultTheme? by remember {
                    mutableStateOf(vm.consult)
                }

                LaunchedEffect(selectedTheme){
                    if(selectedKno != null && selectedControl != null && selectedTheme != null && vm.slots.isEmpty()){
                        vm.loadFreeSlots(selectedKno!!.id)
                    }
                }

                var selectedMonthIdx by remember {
                    mutableStateOf(0)
                }

                var meetDate: Date by remember {
                    mutableStateOf(Date())
                }
                var time: Time by remember {
                    mutableStateOf(Time(-1, ""))
                }

//                        Box(modifier = Modifier.fillMaxWidth()) {
//                            if (selectedMonthIdx > 0) {
//                                IconButton(onClick = { selectedMonthIdx -= 1 }) {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowLeft,
//                                        contentDescription = "",
//                                        modifier = Modifier.align(BiasAlignment(0.8f, 0f))
//                                    )
//                                }
//                            }

                if (showDialog){
                    AlertDialog(onDismissRequest = { showDialog = false }, title = {
                        Text("Внимание")
                    }, text = { Text(text = "Вы увереы что хотите записаться на видеовстречу в КНО ${selectedKno?.name ?: ""}\n" +
                            "Вид контроля: ${selectedControl?.name ?: ""} на тему ${selectedTheme?.name ?: ""}\n" +
                            "${format.format(meetDate) ?: ""} в ${time.name}")
                    }, confirmButton = {
                        Button(onClick = { showDialog = false
                            vm.reserveMeet(time.id, selectedTheme!!.id)
                        }) {
                            Text(text = "Отправить")
                        }
                    }, dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "Отмена")
                        }
                    }
                    )
                }
                
                if(showOk){
                    AlertDialog(onDismissRequest = {vm.showOk.update { false }}, text = { Text(text = "Вы успешно записались")}, confirmButton = {
                        Button(onClick = {vm.showOk.update { false }}) {
                            Text(text = "Отлично")
                        }
                    })
                        

                }


                LazyColumn(
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 70.dp),
                    modifier = Modifier.animateContentSize()
                ) {

                    item {
                        Text(
                            text = "Запись на видео консультацию",
                            fontFamily = ptSerifFontFamily,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )

                        Spacer(Modifier.padding(8.dp))
                    }

                    item {
//                        Text(
//                            text = "Контрольно надзорный орган",
//                            fontFamily = ptSerifFontFamily,
//                            fontSize = 14.sp,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 2.dp)
//                        )
                        SearchBoxView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            list = s.data.first,
                            labelText = "Выбрать КНО",
                            defaultText = selectedKno?.name ?: "",
                            onSelect = {
                                selectedKno = null
                                selectedKno = it as Kno
                                vm.kno = selectedKno
                                selectedControl = null
                                selectedTheme = null
                                vm.controlType = null
                                vm.slots = mapOf()
                            }, onClearClick = {
                                selectedKno = null
                                vm.kno = null
                                selectedTheme = null
                                selectedControl = null
                                vm.controlType = null
                                vm.slots = mapOf()
                            })
                    }




                    item {
                        AnimatedVisibility(visible = selectedKno != null) {

//                            Text(
//                                text = "Тип контроля",
//                                fontFamily = ptSerifFontFamily,
//                                fontSize = 14.sp,
//
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(bottom = 2.dp)
//                            )
                            SearchBoxView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                labelText = "Тип контроля",
                                list = selectedKno?.visions ?: listOf(),
                                defaultText = selectedControl?.name ?: "",
                                onSelect = {
                                    selectedControl = null
                                    selectedControl = it as ControlType
                                    vm.controlType = selectedControl
                                    vm.slots = mapOf()
//                                    selectedTheme = null
////                                selectedTheme = null
//                                selectedKno?.name
////                                selectedTheme = null
                                }, onClearClick = {
                                    selectedControl = null
                                    vm.controlType = null
                                    vm.slots = mapOf()
                                }
                            )

                        }

                    }



                    item {
                        AnimatedVisibility(visible = selectedKno != null && selectedControl != null) {


//                            Text(
//                                text = "Тема консультации",
//                                fontFamily = ptSerifFontFamily,
//                                fontSize = 14.sp,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(bottom = 2.dp)
//                            )
                            SearchBoxView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                labelText = "Тема консультации",
                                defaultText = selectedTheme?.name ?: "",
                                list = s.data.second,
                                onClearClick = {
                                    vm.consult = null
                                    selectedTheme = null

                                }
                            ) {
                                selectedTheme = it as ConsultTheme
                                vm.consult = selectedTheme

                            }

                        }
                    }

//                    if (selectedKno != null && selectedControl != null && selectedTheme != null) {
                    item {
                        if (vm.slots.isNotEmpty() && selectedTheme != null) {
//                        if (selectedKno != null && selectedControl != null && selectedTheme != null) {
                            val name =  vm.slotsOrder[selectedMonthIdx].second.drop(3)
                            Spacer(Modifier.padding(8.dp))
                            Text(
                                text = "Свободные слоты",
                                fontFamily = ptSerifFontFamily,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )

                            Spacer(Modifier.padding(4.dp))


                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                value = name.uppercase(
                                    Locale.getDefault()
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                onValueChange = {},
                                enabled = false,
                                textStyle = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colors.onBackground,
                                    textAlign = TextAlign.Center
                                ),
                                leadingIcon =
                                {
                                    if (selectedMonthIdx > 0) {
                                        IconButton(onClick = { selectedMonthIdx -= 1 }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowLeft,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(48.dp),
                                                tint = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
                                            )
                                        }
                                    }
                                },
                                trailingIcon =
                                {
                                    if (selectedMonthIdx < vm.slotsOrder.size - 1) {
                                        IconButton(onClick = { selectedMonthIdx += 1 }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowRight,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(48.dp),
                                                tint = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                                            )
                                        }

                                    }
                                }

                            )
                        }
//
                        AnimatedVisibility(visible = vm.slots.isNotEmpty() && selectedTheme != null) {


                            SlotCalendarView(vm.slots[vm.slotsOrder[selectedMonthIdx].second]!!){ d, t ->
                                meetDate = d
                                time = t
                                showDialog = true
                                "Время"
                            }
                        }
                    }
                }


            }
            is ViewStateClass.Error ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = s.e.message ?: "Неизвестная ошибка")
                }
            ViewStateClass.Loading -> LoadingView()
        }
    }


    BackHandler(viewState is ViewStateClass.Error) {
        vm.consult = null
        vm.loadList()
    }
}


