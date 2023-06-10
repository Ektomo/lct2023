package com.example.lct2023.view.inspector.waiting_list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView

@Composable
fun AppointListView(vm: AppointListViewModel) {

    val viewState by vm.state.collectAsState()



    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 90.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                ) {
                    for (i in 1..4) {
                        item {
                            AppointListView()
                        }
                    }

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
fun AppointListView(isApproved: Boolean = true) {
//    val controlType = ControlType(1, "dfgsdfg")
//    val topic = ConsultTheme(1, "sdfgsdfg")
//    val format = SimpleDateFormat("dd EEE", Locale.getDefault())
//    val slot = Slot(Date(), listOf(Time(1, "10:20")))
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val annotatedString = buildAnnotatedString {
            val text =
                "Видеовстреча 2 июня на тему региональные споры соседей в тропиках с пользователем Алексей Степанцев"
            val status = if (isApproved) "\nПодтверждена" else "\nОжидает подтверждения"
            append(text)
            append(status)
            addStyle(
                style = SpanStyle(color = if (isApproved) Color(0xFF34CA86) else Color(0xFFCA7922)),
                start = text.length, end = text.length + status.length
            )
            if (isApproved) {
                val url =
                    "\nСсылка на встречу: https://us05web.zoom.us/j/85979946849?pwd=Z2ZMaENHRlE0UDRRZWsxRk5ZYXFUdz09"
                append(url)
                addStyle(
                    style = SpanStyle(
                        color = Color(0xff64B5F6),
                        textDecoration = TextDecoration.Underline
                    ),
                    start = text.length + status.length + 20,
                    end = text.length + status.length + url.length
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://us05web.zoom.us/j/85979946849?pwd=Z2ZMaENHRlE0UDRRZWsxRk5ZYXFUdz09",
                    start = text.length + status.length + 20,
                    end = text.length + status.length + url.length
                )
            }
        }

        Text(
            text = annotatedString
        )
    }
}
