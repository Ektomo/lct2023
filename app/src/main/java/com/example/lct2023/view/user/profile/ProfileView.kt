package com.example.lct2023.view.user.profile

import android.widget.Space
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lct2023.ui.theme.ptSerifFontFamily

@Composable
fun ProfileView(vm: ProfileViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical, enabled = true)
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


        LCRow(header = "Фамилия:", value =  "Портупеев")
        LCRow(header = "Имя:", value =  "Семен")
        LCRow(header = "Отчество:", value =  "Жадикович")
//        LCRow(header = "Лицо:", value =  "Индивидуальный предприниматель")
//        LCRow(header = "Наименование:", value =  "ИП \"Все для сталелитейного цеха\"")
        LCRow(header = "Тип:", value =  "Юридическое лицо")
        LCRow(header = "Наименование:", value =  "ООО \"Мечта Максима\"")
        LCRow(header = "Должность:", value =  "Исполняющий директор")
        LCRow(header = "Инн:", value =  "572 123115 124")
        LCRow(header = "Основной ОКВЭД:", value =  "12.123(Вратарь)")
        LCRow(header = "Дополнительные ОКВЭД:", value =  "23.12312(Пошив ткани), 123.1267(Аренда хлебобулочных изделий), 234.1233(Ремонт)")

        Button(onClick = {

        },
            modifier = Modifier.align(Alignment.End).padding(top = 16.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Редактировать")
        }

    }

}

@Composable
fun LCRow(header: String, value: String){
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
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