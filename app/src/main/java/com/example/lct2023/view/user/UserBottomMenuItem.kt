package com.example.lct2023.view.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lct2023.R
import com.example.lct2023.view.inspector.InspectorBottomMenuItem
import com.example.lct2023.view.inspector.approved_list.ApprovedListView
import com.example.lct2023.view.inspector.approved_list.ApprovedListViewModel
import com.example.lct2023.view.inspector.waiting_list.WaitListView
import com.example.lct2023.view.inspector.waiting_list.WaitListViewModel
import com.example.lct2023.view.user.chat.ChatView
import com.example.lct2023.view.user.chat.ChatViewModel
import com.example.lct2023.view.user.consulting.ConsultingView
import com.example.lct2023.view.user.consulting.ConsultingViewModel
import com.example.lct2023.view.user.profile.ProfileView
import com.example.lct2023.view.user.profile.ProfileViewModel

sealed class UserBottomMenuItem(var title: String, var icon: Int,var screen_route: String){

    object Profile:
        UserBottomMenuItem("Профиль", R.drawable.baseline_lc_24, "profile")
    object ChatBot :
        UserBottomMenuItem("Чат бот", R.drawable.baseline_chat_24, "chat_bot")
    object Consulting :
        UserBottomMenuItem("Консультация", R.drawable.baseline_video_chat_24, "consulting")



}

@Composable
fun UserNavigationGraph(navController: NavHostController, paddingValues: PaddingValues){
    NavHost(navController, startDestination = UserBottomMenuItem.Profile.screen_route){

        composable(UserBottomMenuItem.Profile.screen_route){
            val vm = hiltViewModel<ProfileViewModel>()
            ProfileView(vm = vm)
        }

        composable(UserBottomMenuItem.ChatBot.screen_route){
            val vm = hiltViewModel<ChatViewModel>()
            ChatView(vm = vm, paddingValues)
        }

        composable(UserBottomMenuItem.Consulting.screen_route){
            val vm = hiltViewModel<ConsultingViewModel>()
            ConsultingView(vm = vm)
        }

    }
}

@Composable
fun UserBottomNavigation(navController: NavController){
    val items = listOf(
        UserBottomMenuItem.Profile,
        UserBottomMenuItem.ChatBot,
        UserBottomMenuItem.Consulting
    )

    BottomNavigation(
//        contentColor = Color.White, backgroundColor = MaterialTheme.colors.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(ImageVector.vectorResource(id = item.icon), contentDescription = item.title) },
                selected = currentRoute == item.screen_route,
                label = {
                    Text(text = item.title, fontSize = 9.sp)
                },
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.screen_route){
                        navController.graph.startDestinationRoute?.let{screenRoute ->
                            popUpTo(screenRoute){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}