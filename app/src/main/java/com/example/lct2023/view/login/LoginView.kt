package com.example.lct2023.view.login

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lct2023.R
import com.example.lct2023.gate.model.user.Kno
import com.example.lct2023.gate.model.user.KnoResponse
import com.example.lct2023.ui.theme.interFontFamily
import com.example.lct2023.util.toUserType
import com.example.lct2023.view.ViewStateClass
import com.example.lct2023.view.util.CustomListDropDownEntity
import com.example.lct2023.view.util.DropdownListView
import com.example.lct2023.view.util.LoadingView

@Composable
fun LoginView(vm: LoginViewModel, pd: PaddingValues) {

    val state by vm.state.collectAsState()
    var login by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current


    Crossfade(targetState = state) { st ->
        when (st) {
            is ViewStateClass.Loading -> {
                LoadingView()
            }
            is ViewStateClass.Data -> {
                login = st.data.name
                pass = st.data.pass
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            .background(Color.White)
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {


                        Text(
                            text = "Авторизация",
                            style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                            fontSize = 30.sp
                        )
                        Spacer(modifier = Modifier.padding(20.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            OutlinedTextField(
                                value = login,
                                onValueChange = { login = it },
                                label = { Text(text = "Логин") },
                                placeholder = { Text(text = "Логин") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next,
                                ),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                })
                            )
                            OutlinedTextField(
                                value = pass,
                                onValueChange = {
                                    pass = it
                                },
                                label = { Text(text = "Пароль") },
                                placeholder = { Text(text = "Пароль") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Password
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    vm.doLogin(login, pass)
                                })
                            )
                            Spacer(Modifier.padding(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(0.8f),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(onClick = {
                                    vm.doLogin(login, pass)
                                }, shape = RoundedCornerShape(6.dp)) {
                                    Text(text = "Войти")
                                }
                                Spacer(modifier = Modifier.padding(4.dp))
                                Button(onClick = {
                                    vm.goToRegister()
                                }, shape = RoundedCornerShape(6.dp)) {
                                    Text(text = "Регистрация")
                                }

                            }
                        }

                    }

                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.dark),
                        contentDescription = "",
                        modifier = Modifier
                            .width(maxWidth / 2)
                            .height(maxWidth / 6)
                            .align(
                                Alignment.TopStart
                            )
                            .padding(top = 16.dp, start = 16.dp)
                    )


                    Image(
                        ImageVector.vectorResource(id = R.drawable.gerb), contentDescription = "",
                        Modifier
                            .size(180.dp)
                            .align(BiasAlignment(1.22f, -1.09f))
                            .background(Color(0xFFED573F), CircleShape)
                            .padding(26.dp)

                    )

                    Text(
                        text = "18\nведомств",
                        fontFamily = interFontFamily,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(BiasAlignment(0.20f, -0.7f))
                            .background(Color(0xEBF8E0DA), CircleShape)
                            .padding(horizontal = 16.dp, vertical = 28.dp)
                    )





                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(id = R.drawable.mos), contentDescription = "",
                            Modifier
                                .padding(end = 8.dp)
                                .size(48.dp)
                        )
                        Text(
                            text = "При поддержке\nПравительства Москвы",
                            fontFamily = interFontFamily,
                            fontSize = 14.sp
                        )
                    }

                }
            }
            is ViewStateClass.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
        }

    }
    BackHandler(state is ViewStateClass.Error) {
        vm.afterExceptionOnLogin()
    }


}

@Composable
fun RegisterView(loginViewModel: LoginViewModel) {

    var user by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }

    var role by remember {
        mutableStateOf("")
    }

    var kno: Kno? by remember {
        mutableStateOf(null)
    }




    val focusManager = LocalFocusManager.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            Text(
                text = "Регистрация",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DropdownListView(
                    modifier = Modifier.fillMaxWidth(0.825f),
                    itemModifier = Modifier,
                    items = roleList,
                    defaultText = "Выберите роль",
                ) {
                    if (it.name.toUserType() != null){
                        role = it.name.toUserType()!!.name
                    }
                    it.name
                }
                AnimatedVisibility(visible = role == UserType.inspector.name) {

                    DropdownListView(
                        modifier = Modifier.fillMaxWidth(0.825f).padding(top = 8.dp),
                        itemModifier = Modifier,
                        items = loginViewModel.knoList,
                        defaultText = "Выберите КНО",
                    ) {
                        kno = it as Kno
                        it.name
                    }
                }
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text(text = "Логин") },
                    placeholder = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(text = "Имя") },
                    placeholder = { Text(text = "Имя") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(text = "Фамилия") },
                    placeholder = { Text(text = "Фамилия") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Пароль") },
                    placeholder = { Text(text = "Пароль") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.doRegister(
                            userName = user,
                            pass = password,
                            firstName = firstName,
                            lastName = lastName,
                            role = role,
                            kno = kno?.id
                        )
                    })
                )
                Spacer(Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            loginViewModel.doRegister(
                                userName = user,
                                pass = password,
                                firstName = firstName,
                                lastName = lastName,
                                role = role,
                                kno = kno?.id
                            )
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = "Регистрация")
                    }

                }


//                Image(
//                    painter = painterResource(id = R.drawable.logo),
//                    contentDescription = "logo",
//                    alignment = BiasAlignment(0f, -1f),
//                )
            }

        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.mos), contentDescription = "",
                Modifier
                    .padding(end = 8.dp)
                    .size(48.dp)
            )
            Text(
                text = "При поддержке\nПравительства Москвы",
                fontFamily = interFontFamily,
                fontSize = 14.sp
            )
        }

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.dark),
            contentDescription = "",
            modifier = Modifier
                .width(maxWidth / 2)
                .height(maxWidth / 6)
                .align(
                    Alignment.TopStart
                )
                .padding(top = 16.dp, start = 16.dp)
        )
    }

    BackHandler() {
        loginViewModel.goToLogin()
    }
}

data class UserRole(
    override val name: String
): CustomListDropDownEntity

val roleList = listOf(
    UserRole(UserType.client.value),
    UserRole(UserType.inspector.value)
)

@Preview
@Composable
fun ForTest() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        ) {


            Text(
                text = "Авторизация",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text(text = "Логин") },
                    placeholder = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                    )
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {

                    },
                    label = { Text(text = "Пароль") },
                    placeholder = { Text(text = "Пароль") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                    })
                )
                Spacer(Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {

                    }) {
                        Text(text = "Войти")
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(onClick = {

                    }) {
                        Text(text = "Регистрация")
                    }

                }
            }

        }

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.dark),
            contentDescription = "",
            modifier = Modifier
                .width(maxWidth / 2)
                .height(maxWidth / 6)
                .align(
                    Alignment.TopStart
                )
                .padding(top = 16.dp, start = 16.dp)
        )


        Image(
            ImageVector.vectorResource(id = R.drawable.gerb), contentDescription = "",
            Modifier
                .size(180.dp)
                .align(BiasAlignment(1.22f, -1.09f))
                .background(Color(0xFFED573F), CircleShape)
                .padding(26.dp)

        )

        Text(
            text = "18\nведомств",
            fontFamily = interFontFamily,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(BiasAlignment(0.20f, -0.7f))
                .background(Color(0xEBF8E0DA), CircleShape)
                .padding(horizontal = 16.dp, vertical = 28.dp)
        )





        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.mos), contentDescription = "",
                Modifier
                    .padding(end = 8.dp)
                    .size(48.dp)
            )
            Text(
                text = "При поддержке\nПравительства Москвы",
                fontFamily = interFontFamily,
                fontSize = 14.sp
            )
        }

    }


}

enum class UserType(val value: String){
    client("Клиент"), inspector("Инспектор")
}



