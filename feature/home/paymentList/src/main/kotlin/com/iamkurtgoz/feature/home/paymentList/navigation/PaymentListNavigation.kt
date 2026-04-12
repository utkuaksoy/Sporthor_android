package com.iamkurtgoz.feature.home.paymentList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.feature.home.paymentList.PaymentListScreen
import com.iamkurtgoz.feature.home.paymentList.addfee.AddFeeScreen
import com.iamkurtgoz.feature.home.paymentList.detail.FeeDetailScreen


private const val HOME_PAYMENT_LIST_ROUTE = "home_payment_list_route"
private const val HOME_PAYMENT_ADD_FEE_ROUTE = "home_payment_add_fee_route"
private const val HOME_PAYMENT_DETAIL_ROUTE = "home_payment_detail_route"
private const val TRAINING_GROUP_ID_ARG = "trainingGroupId"
private const val USER_ID_ARG = "userId"

fun NavGraphBuilder.paymentListScreenNavigation(
    navigateUp: () -> Unit,
    navigateToAddFeeScreen: (trainingGroupId: String) -> Unit,
    navigateToFeeDetailScreen: (userId: String, trainingGroupId: String) -> Unit,
) {
    composable(route = HOME_PAYMENT_LIST_ROUTE) {
        PaymentListScreen(
            navigateUp = navigateUp,
            navigateToAddFee = { trainingGroupId ->
                navigateToAddFeeScreen(trainingGroupId)
            },
            navigateToFeeDetail = { userId, trainingGroupId ->
                navigateToFeeDetailScreen(userId, trainingGroupId)
            },
        )
    }
}
fun NavGraphBuilder.addFeeScreenNavigation(
    navigateUp: () -> Unit,
) {
    composable(
        route = "$HOME_PAYMENT_ADD_FEE_ROUTE/{$TRAINING_GROUP_ID_ARG}",
    ) { backStackEntry ->
        val trainingGroupId = backStackEntry.arguments
            ?.getString(TRAINING_GROUP_ID_ARG)
            .orEmpty()

        AddFeeScreen(
            trainingGroupId = trainingGroupId,
            navigateUp = navigateUp,
        )
    }
}

fun NavGraphBuilder.feeDetailScreenNavigation(
    navigateUp: () -> Unit,
    navigateToAddFeeScreen: (trainingGroupId: String) -> Unit,
) {
    composable(
        route = "$HOME_PAYMENT_DETAIL_ROUTE/{$USER_ID_ARG}/{$TRAINING_GROUP_ID_ARG}",
    ) { backStackEntry ->
        val userId = backStackEntry.arguments
            ?.getString(USER_ID_ARG)
            .orEmpty()
        val trainingGroupId = backStackEntry.arguments
            ?.getString(TRAINING_GROUP_ID_ARG)
            .orEmpty()

        FeeDetailScreen(
            userId = userId,
            trainingGroupId = trainingGroupId,
            navigateUp = navigateUp,
            navigateToAddFee = navigateToAddFeeScreen,
        )
    }
}

fun NavController.navigateToPaymentListScreen(navOptions: NavOptions? = null) {
    this.navigate(HOME_PAYMENT_LIST_ROUTE, navOptions)
}

fun NavController.navigateToAddFeeScreen(
    trainingGroupId: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("$HOME_PAYMENT_ADD_FEE_ROUTE/$trainingGroupId", navOptions)
}

fun NavController.navigateToFeeDetailScreen(
    userId: String,
    trainingGroupId: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("$HOME_PAYMENT_DETAIL_ROUTE/$userId/$trainingGroupId", navOptions)
}
