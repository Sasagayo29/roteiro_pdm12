package com.example.roteiro_pdm12.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.roteiro_pdm12.ui.screens.form.PartnerFormScreen
import com.example.roteiro_pdm12.ui.screens.list.PartnerListScreen
import com.example.roteiro_pdm12.ui.screens.trash.PartnerTrashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PartnerList.route
    ) {
        composable(route = Screen.PartnerList.route) {
            PartnerListScreen(
                onAddPartner = {
                    navController.navigate(Screen.PartnerForm.createRoute(null))
                },
                onEditPartner = { id ->
                    navController.navigate(Screen.PartnerForm.createRoute(id))
                },
                onOpenTrash = {
                    navController.navigate(Screen.PartnerTrash.route)
                }
            )
        }

        composable(
            route = Screen.PartnerForm.route,
            arguments = listOf(
                navArgument("partnerId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            PartnerFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.PartnerTrash.route) {
            PartnerTrashScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}