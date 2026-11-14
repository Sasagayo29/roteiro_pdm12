package com.example.roteiro_pdm12.ui.navigation

sealed class Screen(val route: String) {
    data object PartnerList : Screen("partner_list")
    data object PartnerTrash : Screen("partner_trash")
    data object PartnerForm : Screen("partner_form?partnerId={partnerId}") {
        fun createRoute(partnerId: Long? = null): String {
            return if (partnerId != null && partnerId > 0L) {
                "partner_form?partnerId=$partnerId"
            } else {
                "partner_form?partnerId=-1"
            }
        }
    }
}