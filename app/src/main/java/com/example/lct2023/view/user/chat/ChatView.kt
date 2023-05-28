package com.example.lct2023.view.user.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.LoadingView
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun ChatView(vm: ChatViewModel, paddingValues: PaddingValues) {

    val viewState by vm.state.collectAsState()

    val voiceRecState by vm.recognizer.state.collectAsState()


    if (voiceRecState.isSpeaking) {
        Dialog(onDismissRequest = { vm.recognizer.stopListening() }) {
            Text(text = "Скажите ваш вопрос и нажмите нажмите на экран", color = Color.White)
        }
    }

    if (voiceRecState.isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            CircularProgressIndicator()
        }
    }

    Crossfade(targetState = viewState) { s ->
        when (s) {
            is ViewStateClass.Data -> {

                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.BottomCenter),
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 140.dp),
                        state = listState
                    ) {

                        s.data.forEach { msg ->
                            item {
                                MsgItemView(msg = msg, width = maxWidth / 3 * 2)
                            }
                        }

                        coroutineScope.launch {
                            if (s.data.isNotEmpty()) {
                                listState.scrollToItem(s.data.count() - 1)
                            }
                        }

                    }

                    Row(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth()
                            .shadow(2.dp)
                            .background(Color(MaterialTheme.colors.primary.value))

                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        var text by remember(voiceRecState.spokenText) {
                            mutableStateOf(voiceRecState.spokenText)
                        }
                        var isErrorTF by remember {
                            mutableStateOf(false)
                        }

                        Column {
                            OutlinedTextField(
                                value = text,
                                onValueChange = {
                                    text = it
                                    if (it.isNotEmpty()) {
                                        isErrorTF = false
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 8.dp)
                                    .fillMaxWidth(0.7f),
                                shape = RoundedCornerShape(24.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    backgroundColor = Color.White
                                ),
                                placeholder = {
                                    Text("Введите ваш вопрос")
                                },
                                trailingIcon = {
                                    if (isErrorTF) {
                                        Icon(
                                            imageVector = Icons.Filled.Error,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                isError = isErrorTF
                            )
                            if (isErrorTF) {
                                Text(
                                    text = "Введите вопрос перед отправкой",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }


                        IconButton(onClick = {
                            if (voiceRecState.isSpeaking) {
                                vm.recognizer.stopListening()
                            } else {
                                vm.recognizer.startListening(Locale.getDefault().language)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "",
                                modifier = Modifier.padding(2.dp)
                            )
                        }

                        IconButton(onClick = {
                            if (text.isNotEmpty()) {
                                vm.sendMsg(
                                    ChatMsgItem(
                                        MsgAuthor.User,
                                        text
                                    )
                                )
                            } else {
                                isErrorTF = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "",
                                modifier = Modifier.padding(2.dp)
                            )
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
fun MsgItemView(msg: ChatMsgItem, width: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (msg.author == MsgAuthor.Bot) Arrangement.Start else Arrangement.End
    ) {
        if (msg.author == MsgAuthor.Bot) {
            Icon(
                imageVector = Icons.Filled.SmartToy,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = msg.msg,
            modifier = Modifier
                .width(width)

                .background(
                    if (msg.author == MsgAuthor.Bot) {
                        Color(0xFFD9F5F5)
                    } else {
                        Color(0xFFE4E6E6)
                    },
                    if (msg.author == MsgAuthor.Bot) {
                        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
                    } else {
                        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
                    }
                )
                .padding(8.dp)
        )
    }

}

@Preview
@Composable
fun Preview_Chat() {

    val msgs = remember {
        mutableStateListOf(
            ChatMsgItem(MsgAuthor.Bot, "Hello", Date()),
            ChatMsgItem(MsgAuthor.Bot, "World", Date()),
            ChatMsgItem(MsgAuthor.User, "Hi", Date()),
            ChatMsgItem(MsgAuthor.Bot, "Wow", Date()),
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp)
                .align(Alignment.BottomCenter), contentPadding = PaddingValues(horizontal = 8.dp)
        ) {

            msgs.forEach { msg ->
                item {
                    MsgItemView(msg = msg, width = maxWidth / 3 * 2)
                }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(MaterialTheme.colors.primary.value))
                .shadow(2.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            var text by remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White
                ),
                placeholder = {
                    Text("Введите ваш вопрос")
                }
            )
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "",
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}


