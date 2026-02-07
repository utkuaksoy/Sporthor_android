package com.iamkurtgoz.feature.home.paymentList.addfee

import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.model.request.AddFeeUserWithTrainingGroupRequest
import com.iamkurtgoz.domain.model.request.AddFeeUsersOrTrainingGroupsRequest
import com.iamkurtgoz.domain.model.response.FeeCategoryItemDomainModel
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModelGroup
import com.iamkurtgoz.domain.model.response.GetTrainingGroupUserDomainModelUser
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.AddFeeUsersOrTrainingGroupsUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeeCategoryUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetTrainingGroupUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AddFeeViewModel @Inject constructor(
    private val addFeeUsersOrTrainingGroupsUseCase: AddFeeUsersOrTrainingGroupsUseCase,
    private val getFeeCategoryUseCase: GetFeeCategoryUseCase,
    private val getTrainingGroupUserUseCase: GetTrainingGroupUserUseCase,
) : CoreViewModel<AddFeeViewModel.State, AddFeeViewModel.SideEffect, AddFeeViewModel.Event>(
    initialState = State(),
) {

    data class State(
        override val isLoading: Boolean = false,
        val categories: List<FeeCategoryItemDomainModel> = emptyList(),
        val selectedCategoryId: String? = null,
        val contextTrainingGroupId: String? = null,
        val showPersonOrGroupPicker: Boolean = false,
        val searchText: String = "",
        val groups: List<AddFeeSelectableGroupUi> = emptyList(),
        val users: List<AddFeeSelectableUserUi> = emptyList(),
        val selectedTrainingGroupIds: Set<String> = emptySet(),
        val selectedUserSelectionKeys: Set<String> = emptySet(),
    ) : com.iamkurtgoz.domain.core.CoreState.ViewState {
        val selectedGroups: List<AddFeeSelectableGroupUi>
            get() = groups.filter { selectedTrainingGroupIds.contains(it.id) }

        val selectedUsers: List<AddFeeSelectableUserUi>
            get() = users.filter { selectedUserSelectionKeys.contains(it.selectionKey) }
    }

    sealed class Event : com.iamkurtgoz.domain.core.CoreState.Event {
        data class Initialize(val trainingGroupId: String) : Event()
        data class SelectCategory(val categoryId: String?) : Event()
        data class SetPersonOrGroupPickerVisible(val isVisible: Boolean) : Event()
        data class SetSearchText(val value: String) : Event()
        data class ToggleTrainingGroupSelection(val groupId: String) : Event()
        data class ToggleUserSelection(val user: AddFeeSelectableUserUi) : Event()
        data class CreateFee(
            val isSinglePayment: Boolean,
            val paymentDate: String,
            val repeatCount: Int,
            val amountText: String,
            val description: String,
        ) : Event()
    }

    sealed class SideEffect : com.iamkurtgoz.domain.core.CoreState.SideEffect {
        data object NavigateUp : SideEffect()
    }

    override fun setEvent(event: Event) {
        when (event) {
            is Event.Initialize -> {
                val contextTrainingGroupId = event.trainingGroupId.ifBlank { null }
                updateState {
                    it.copy(
                        contextTrainingGroupId = contextTrainingGroupId,
                    )
                }
                getFeeCategories()
                getTrainingGroupUsers(contextTrainingGroupId)
            }

            is Event.SelectCategory -> {
                updateState { it.copy(selectedCategoryId = event.categoryId) }
            }

            is Event.SetPersonOrGroupPickerVisible -> {
                updateState {
                    it.copy(
                        showPersonOrGroupPicker = event.isVisible,
                        searchText = if (event.isVisible) it.searchText else "",
                    )
                }
            }

            is Event.SetSearchText -> {
                updateState { it.copy(searchText = event.value) }
            }

            is Event.ToggleTrainingGroupSelection -> {
                updateState { state ->
                    val selectedIds = state.selectedTrainingGroupIds.toMutableSet()
                    if (selectedIds.contains(event.groupId)) {
                        selectedIds.remove(event.groupId)
                    } else {
                        selectedIds.add(event.groupId)
                    }

                    state.copy(
                        selectedTrainingGroupIds = selectedIds,
                    )
                }
            }

            is Event.ToggleUserSelection -> {
                updateState { state ->
                    val selectedKeys = state.selectedUserSelectionKeys.toMutableSet()

                    if (selectedKeys.contains(event.user.selectionKey)) {
                        selectedKeys.remove(event.user.selectionKey)
                    } else {
                        selectedKeys.add(event.user.selectionKey)
                    }

                    state.copy(
                        selectedUserSelectionKeys = selectedKeys,
                    )
                }
            }
            is Event.CreateFee -> createFee(event)
        }
    }

    private fun getFeeCategories() {
        getFeeCategoryUseCase.invoke()
            .requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess { response ->
                val categories = response.categories.orEmpty()
                updateState {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        selectedCategoryId = it.selectedCategoryId
                            ?: categories.firstOrNull { category -> !category.id.isNullOrBlank() }?.id,
                    )
                }
            }
    }

    private fun getTrainingGroupUsers(contextTrainingGroupId: String?) {
        getTrainingGroupUserUseCase.invoke()
            .requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess { response ->
                val filteredGroups = response.groups.filterByContext(contextTrainingGroupId)
                val selectableGroups = filteredGroups.map { it.toSelectableGroupUi() }
                val selectableUsers = filteredGroups.flatMap { it.toSelectableUsers() }
                val preselectedGroupIds = if (contextTrainingGroupId.isNullOrBlank()) {
                    emptySet()
                } else {
                    selectableGroups
                        .map { it.id }
                        .filter { it == contextTrainingGroupId }
                        .toSet()
                }
                val preselectedUserSelectionKeys = if (contextTrainingGroupId.isNullOrBlank()) {
                    emptySet()
                } else {
                    selectableUsers
                        .filter { it.groupId == contextTrainingGroupId }
                        .map { it.selectionKey }
                        .toSet()
                }

                updateState { state ->
                    state.copy(
                        isLoading = false,
                        groups = selectableGroups,
                        users = selectableUsers,
                        selectedTrainingGroupIds = state.selectedTrainingGroupIds.ifEmpty { preselectedGroupIds },
                        selectedUserSelectionKeys = state.selectedUserSelectionKeys.ifEmpty { preselectedUserSelectionKeys },
                    )
                }
            }
    }

    private fun createFee(event: Event.CreateFee) {
        val state = viewState
        if (state.selectedGroups.isEmpty() && state.selectedUsers.isEmpty()) return
        val categoryId = state.selectedCategoryId
            ?: state.categories.firstOrNull { !it.id.isNullOrBlank() }?.id
        if (categoryId.isNullOrBlank()) return
        if (event.description.isBlank()) return

        val amount = event.amountText.toAmountOrNull() ?: return
        val selectedUsers = state.selectedUsers.filter { it.userId.isNotBlank() && it.groupId.isNotBlank() }
        val selectedGroupIds = state.selectedGroups.map { it.id }.filter { it.isNotBlank() }

        if (selectedUsers.isEmpty() && selectedGroupIds.isEmpty()) return

        val request = AddFeeUsersOrTrainingGroupsRequest(
            usersWithTrainingGroups = selectedUsers.map { user ->
                AddFeeUserWithTrainingGroupRequest(
                    userId = user.userId,
                    trainingGroupId = user.groupId,
                )
            },
            trainingGroupIds = selectedGroupIds,
            categoryId = categoryId,
            paymentTypeId = if (event.isSinglePayment) SINGLE_PAYMENT_TYPE_ID else RECURRING_PAYMENT_TYPE_ID,
            paymentDate = event.paymentDate,
            paymentAgainCount = if (event.isSinglePayment) 0 else event.repeatCount,
            amount = amount,
            description = event.description.ifBlank { null },
        )

        addFeeUsersOrTrainingGroupsUseCase.invoke(request)
            .requester
            .onLoading {
                updateState { it.copy(isLoading = true) }
            }
            .onError {
                updateState { it.copy(isLoading = false) }
            }
            .callWithSuccess {
                updateState { current ->
                    current.copy(isLoading = false)
                }
                setSideEffect(SideEffect.NavigateUp)
            }
    }
}

private const val SINGLE_PAYMENT_TYPE_ID = "1"
private const val RECURRING_PAYMENT_TYPE_ID = "2"

internal data class AddFeeSelectableGroupUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val imageUrl: String?,
)

internal data class AddFeeSelectableUserUi(
    val selectionKey: String,
    val userId: String,
    val groupId: String,
    val name: String,
    val username: String?,
    val imageUrl: String?,
) {
    fun isMatch(query: String): Boolean {
        return name.contains(query, ignoreCase = true) || username?.contains(query, ignoreCase = true) == true
    }
}

private fun List<GetTrainingGroupUserDomainModelGroup?>?.filterByContext(
    contextTrainingGroupId: String?,
): List<GetTrainingGroupUserDomainModelGroup> {
    val groups = this.orEmpty().filterNotNull()
    if (contextTrainingGroupId.isNullOrBlank()) {
        return groups
    }

    return groups.filter { it.groupId == contextTrainingGroupId }
        .ifEmpty { groups }
}

private fun GetTrainingGroupUserDomainModelGroup.toSelectableGroupUi(): AddFeeSelectableGroupUi {
    return AddFeeSelectableGroupUi(
        id = groupId.orEmpty(),
        name = groupName.orEmpty(),
        subtitle = listOfNotNull(season, team?.name)
            .filter { it.isNotBlank() }
            .joinToString(" - "),
        imageUrl = groupImage,
    )
}

private fun GetTrainingGroupUserDomainModelGroup.toSelectableUsers(): List<AddFeeSelectableUserUi> {
    val resolvedGroupId = groupId.orEmpty()

    return users.orEmpty()
        .filterNotNull()
        .map { user ->
            user.toSelectableUserUi(groupId = resolvedGroupId)
        }
        .distinctBy { it.selectionKey }
}

private fun GetTrainingGroupUserDomainModelUser.toSelectableUserUi(
    groupId: String,
): AddFeeSelectableUserUi {
    return AddFeeSelectableUserUi(
        selectionKey = "$groupId:${id.orEmpty()}",
        userId = id.orEmpty(),
        groupId = groupId,
        name = name.orEmpty(),
        username = username,
        imageUrl = imageUrl,
    )
}

private fun String.toAmountOrNull(): Double? {
    val normalized = trim()
        .replace("TL", "", ignoreCase = true)
        .replace(",", ".")
        .filter { it.isDigit() || it == '.' }

    return normalized.toDoubleOrNull()
}
