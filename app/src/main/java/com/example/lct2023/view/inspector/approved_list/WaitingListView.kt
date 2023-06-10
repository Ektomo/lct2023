package com.example.lct2023.view.inspector.approved_list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WaitingListView(vm: WaitingViewModel) {

    val viewState by vm.state.collectAsState()


    val mockList = remember(vm.mockStateList) {
        vm.mockStateList
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {
                if (showDialog) {
                    Dialog(
                        onDismissRequest = { showDialog = false },
                        DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .padding(8.dp)) {
                            var commentTex by remember {
                                mutableStateOf("")
                            }
                            Text(text = "Укажите комментарий для отказа", fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.padding(2.dp))
                            TextField(
                                value = commentTex,
                                onValueChange = { commentTex = it },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    showDialog = false
                                })
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Button(onClick = { showDialog = false }) {
                                Text("Отправить")
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 90.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                ) {
                    itemsIndexed(mockList) { idx, i ->
                        ApprovedListViewItem(
                            modifier = Modifier.animateItemPlacement(),
                            "$i, $idx",
                            onOk = {
//                                mockList.remove(i)
                            },
                            onCancel = {
                                showDialog = true
                            })
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ApprovedListViewItem(
    modifier: Modifier = Modifier,
    name: String,
    onOk: () -> Unit,
    onCancel: () -> Unit
) {

    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible
    ) {

        Column(
            modifier = modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = name)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        visible = false
                        onCancel()
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Отклонить")
                }
                Button(
                    onClick = {
                        visible = false
                        onOk()
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Принять")
                }
            }
        }
    }
}