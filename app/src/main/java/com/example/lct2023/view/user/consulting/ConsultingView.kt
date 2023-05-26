package com.example.lct2023.view.user.consulting

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lct2023.ui.theme.ptSerifFontFamily
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.DropdownListView
import com.example.lct2023.view.util.LoadingView
import com.example.lct2023.view.util.SlotCalendarView
import java.util.*

@Composable
fun ConsultingView(vm: ConsultingViewModel) {

    val viewState by vm.state.collectAsState()

    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {
                var selectedKno: Kno? by remember {
                    mutableStateOf(null)
                }


                var selectedControl: ControlType? by remember {
                    mutableStateOf(null)
                }

                var selectedTheme: ConsultTheme? by remember {
                    mutableStateOf(null)
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
                        Text(
                            text = "Контрольно надзорный орган",
                            fontFamily = ptSerifFontFamily,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                        )
                        DropdownListView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            itemModifier = Modifier
                                .fillMaxWidth(),
                            items = s.data,
                            defaultText = "Выберете КНО",
                            onSelect = {
                                selectedKno = it as Kno
                                selectedControl = null
                                selectedKno?.name
//                                selectedTheme = null
                            })
                    }

                    item {
                        Text(
                            text = "Тип контроля",
                            fontFamily = ptSerifFontFamily,
                            fontSize = 14.sp,

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                        )
                        DropdownListView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            itemModifier = Modifier
                                .fillMaxWidth(),
                            items = selectedKno?.controlList ?: listOf(),
                            defaultText = "Необходимо выбрать КНО"
                        ) {
                            selectedControl = it as ControlType
                            selectedControl?.name
//                            selectedTheme = null
                        }


                    }

                    item {
                        Text(
                            text = "Тема консультации",
                            fontFamily = ptSerifFontFamily,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                        )
                        DropdownListView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            itemModifier = Modifier
                                .fillMaxWidth(),
                            items = selectedControl?.themeList ?: listOf(),
                            defaultText = "Необходимо выбрать тип контроля"
                        ) {
                            selectedTheme = it as ConsultTheme
                            selectedControl?.name
                        }

                    }


//                    if (selectedKno != null && selectedControl != null && selectedTheme != null) {
                        item {


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


                            var selectedMonthIdx by remember {
                                mutableStateOf(0)
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
                            val name = vm.slotsOrder[selectedMonthIdx].second.drop(3)

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


                            SlotCalendarView(vm.slots[vm.slotsOrder[selectedMonthIdx].second]!!)
                        }

//                            Text(
//                                text = name, modifier = Modifier.align(BiasAlignment(0f, 0f))
//                            )

//                            if (selectedMonthIdx < vm.slotsOrder.size - 1) {
//                                IconButton(onClick = { selectedMonthIdx += 1 }) {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowRight,
//                                        contentDescription = "",
//                                        modifier = Modifier.align(BiasAlignment(1.2f, 0f))
//                                    )
//                                }
//                            }
//                        }


//
//                    }
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
        vm.loadList()
    }
}


