package com.example.lct2023.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*


data class Slots(
    val slotList: List<Slot>
)

data class Slot(
    val date: Date,
    val times: List<Time>
)

data class Time(
    override val name: String
) : DropDownItem


@Composable
fun SlotCalendarView(slots: List<Slot>) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val widthItem = (screenWidth.toDouble()/3.15).dp

    val chSlots = slots.chunked(3)

    Column(modifier = Modifier.fillMaxSize()) {
        chSlots.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach {
                    Item(slot = it, width = widthItem)
                }
            }

        }
    }
}


//@Preview
//@Composable
//fun SlotCalendarItem() {
//
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//
//    val slots = mockSlots().chunked(3)
//
//    Column(modifier = Modifier.fillMaxSize().scrollable(rememberScrollState(), Orientation.Vertical)) {
//        slots.forEach { row ->
//            Row(modifier = Modifier.fillMaxWidth()) {
//                row.forEach {
//                    Item(slot = it, width = screenWidth/3)
//                }
//            }
//
//        }
//    }
//
//
//
//
//}



//LLLL - месяц в именительном
@Composable
fun Item(slot: Slot, width: Dp) {
    val format = SimpleDateFormat("dd EEE", Locale.getDefault())
//    val slot = Slot(Date(), listOf(Time("9:00-10:00"), Time("10:00-11:00")))

    Column(
        Modifier
            .size(width = width, height = width)
            .padding(2.dp)
            .background(
                Color(0xFFFEF4F1), RoundedCornerShape(2.dp)
            )
            .border(1.dp, color = Color.LightGray)
    ) {
        Text(
            text = format.format(slot.date),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        DropdownListView(
            modifier = Modifier.padding(4.dp),
            itemModifier = Modifier.wrapContentWidth(),
            items = slot.times,
            defaultText = "Время"
        ) {

            it.name.replace("-", "\n")
        }
    }


}
