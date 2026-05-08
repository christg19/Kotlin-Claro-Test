package com.chris.contactos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chris.contactos.ui.contactlist.ContactListScreen
import com.chris.contactos.ui.createcontact.CreateContactScreen

sealed class Screen(val route: String) {
    data object ContactList : Screen("contact_list")
    data object CreateContact : Screen("create_contact")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ContactList.route
    ) {
        composable(Screen.ContactList.route) {
            ContactListScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateContact.route)
                }
            )
        }
        composable(Screen.CreateContact.route) {
            CreateContactScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}