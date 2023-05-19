package com.example.lct2023.view.inspector

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
import com.example.lct2023.view.inspector.approved_list.ApprovedListView
import com.example.lct2023.view.inspector.approved_list.ApprovedListViewModel
import com.example.lct2023.view.inspector.waiting_list.WaitListView
import com.example.lct2023.view.inspector.waiting_list.WaitListViewModel


sealed class InspectorBottomMenuItem(var title: String, var icon: Int,var screen_route: String){

    object WaitList:
        InspectorBottomMenuItem("Подтвердить", R.drawable.wait_list_24, "wait_list")
    object ApprovedList :
        InspectorBottomMenuItem("Подтвержденные", R.drawable.list_approved_24, "approved_list")



}


@Composable
fun InspectorNavigationGraph(navController: NavHostController, paddingValues: PaddingValues){
    NavHost(navController, startDestination = InspectorBottomMenuItem.ApprovedList.screen_route){

        composable(InspectorBottomMenuItem.WaitList.screen_route){
            val vm = hiltViewModel<WaitListViewModel>()
            WaitListView(vm = vm)
        }

        composable(InspectorBottomMenuItem.ApprovedList.screen_route){
            val vm = hiltViewModel<ApprovedListViewModel>()
            ApprovedListView(vm = vm)
        }

    }
}

@Composable
fun InspectorBottomNavigation(navController: NavController){
    val items = listOf(
        InspectorBottomMenuItem.WaitList,
        InspectorBottomMenuItem.ApprovedList
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


