package com.example.lct2023.view.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.lct2023.ui.theme.ptSerifFontFamily
import com.example.lct2023.view.inspector.waiting_list.AppointListView

@Composable
fun ProfileView(vm: ProfileViewModel) {

    var showDialog by remember {
        mutableStateOf(false)
    }


    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                for (i in 1..2){
                        AppointListView()
                }
                for (i in 1..2){
                    AppointListView(false)
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Цифровой профиль",
            fontFamily = ptSerifFontFamily,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(Modifier.padding(8.dp))


        LCRow(header = "Фамилия:", value = "Портупеев")
        LCRow(header = "Имя:", value = "Семен")
        LCRow(header = "Отчество:", value = "Жадикович")
//        LCRow(header = "Лицо:", value =  "Индивидуальный предприниматель")
//        LCRow(header = "Наименование:", value =  "ИП \"Все для сталелитейного цеха\"")
        LCRow(header = "Тип:", value = "Юридическое лицо")
        LCRow(header = "Наименование:", value = "ООО \"Мечта Максима\"")
        LCRow(header = "Должность:", value = "Исполняющий директор")
        LCRow(header = "Инн:", value = "572 123115 124")
        LCRow(header = "Основной ОКВЭД:", value = "12.123(Вратарь)")
        LCRow(
            header = "Дополнительные ОКВЭД:",
            value = "23.12312(Пошив ткани), 123.1267(Аренда хлебобулочных изделий), 234.1233(Ремонт)"
        )

        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Редактировать профиль")
        }

        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Запланированные встречи")
        }

    }

}

@Composable
fun LCRow(header: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
        Text(
            text = header,
            fontFamily = ptSerifFontFamily,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(0.8f)
        )
        Text(
            text = value,
            fontFamily = ptSerifFontFamily,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
        )
    }
}