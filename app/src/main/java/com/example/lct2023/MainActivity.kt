package com.example.lct2023

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.lct2023.ui.theme.Lct2023Theme
import com.example.lct2023.view.inspector.InspectorBottomNavigation
import com.example.lct2023.view.inspector.InspectorNavigationGraph
import com.example.lct2023.view.login.LoginView
import com.example.lct2023.view.login.LoginViewModel
import com.example.lct2023.view.login.RegisterView
import com.example.lct2023.view.user.UserBottomNavigation
import com.example.lct2023.view.user.UserNavigationGraph
import com.example.lct2023.view.util.LoadingView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

enum class StartPosition {
    NotLogin, LoginInspector, LoginUser, Register, Loading
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {

            val recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->

                }
            )

            LaunchedEffect(key1 = recordAudioLauncher){
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            val vm = hiltViewModel<LoginViewModel>()

            val currentAppState = vm.loginState.collectAsState()


            val navController = rememberNavController()

            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            Lct2023Theme {
                BackPressSample()
                Crossfade(targetState = currentAppState) { st ->
                    when(st.value){
                        StartPosition.NotLogin -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),

                            ) { pd ->
                                LoginView(vm = vm, pd)
                            }

                        }
                        StartPosition.LoginInspector -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { InspectorBottomNavigation(navController = navController) }
                            ) { pd ->
                                InspectorNavigationGraph(navController = navController, pd)
                            }
                        }
                        StartPosition.LoginUser -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { UserBottomNavigation(navController = navController) }
                            ) { pd ->
                                UserNavigationGraph(navController = navController, pd)
                            }
                        }
                        StartPosition.Register -> {
                            RegisterView(loginViewModel = vm)
                        }
                        StartPosition.Loading -> LoadingView()
                    }
                }
                // A surface container using the 'background' color from the theme
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    bottomBar = { UserBottomNavigation(navController = navController) }
//                ) { pd ->
//                    UserNavigationGraph(navController = navController, pd)
//                }
            }

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
        }
    }
}


sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}

@Composable
private fun BackPressSample() {
    var showToast by remember { mutableStateOf(false) }

    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    if(showToast){
        Toast.makeText(context, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show()
        showToast= false
    }


    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lct2023Theme {
        Greeting("Android")
    }
}