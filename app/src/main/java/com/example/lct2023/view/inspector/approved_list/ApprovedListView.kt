package com.example.lct2023.view.inspector.approved_list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lct2023.gate.model.user.ConsultTheme
import com.example.lct2023.gate.model.user.ControlType
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView
import com.example.lct2023.view.util.Slot
import com.example.lct2023.view.util.Time
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ApprovedListView(vm: ApprovedListViewModel) {

    val viewState by vm.state.collectAsState()


    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                }

            }
            is ViewStateClass.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = s.e.message ?: "Неизвестная ошибка")
                }
            }
            ViewStateClass.Loading -> {
                LoadingView()
            }
        }
    }


    BackHandler(viewState is ViewStateClass.Error) {
        vm.loadList()
    }

}


@Composable
fun ApprovedListViewItem() {
    val controlType = ControlType(1, "dfgsdfg")
    val topic = ConsultTheme(1, "sdfgsdfg")
    val format = SimpleDateFormat("dd EEE", Locale.getDefault())
    val slot = Slot(Date(), listOf(Time(1, "10:20")))
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Видеовстреча ${format.format(slot.date)} в ${slot.times.first().name} на тему ${topic.name} в ${controlType.name}")
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { }, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "Отклонить")
            }
            Button(
                onClick = { }, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "Принять")
            }
        }
    }
}