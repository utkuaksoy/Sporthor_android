/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.feature.home.profile.profileEdit

import androidx.compose.ui.util.fastFirstOrNull
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.impl.ProfileEditEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.request.UpdateProfileImageRequest
import com.iamkurtgoz.domain.repository.LocationRepository
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchInfoRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchesAttributeItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.types.toInfoRowType
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.useCase.GetProfileSummaryUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.useCase.ImageUploadUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.useCase.ImageUploadUseCaseParams
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.useCase.UpdateProfileImageUseCase
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.useCase.UpdateProfileSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class ProfileEditViewModel @Inject constructor(
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val getProfileSummaryUseCase: GetProfileSummaryUseCase,
    private val updateProfileSummaryUseCase: UpdateProfileSummaryUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val locationRepository: LocationRepository,
) : CoreViewModel<ProfileEditScreenContract.State, ProfileEditScreenContract.SideEffect, ProfileEditScreenContract.Event>(
    initialState = ProfileEditScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    override fun setEvent(event: ProfileEditScreenContract.Event) {
        when (event) {
            is ProfileEditScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ProfileEditScreenContract.Event.NavigateUp -> setSideEffect(ProfileEditScreenContract.SideEffect.NavigateUp)
            is ProfileEditScreenContract.Event.PopBackStack -> setSideEffect(ProfileEditScreenContract.SideEffect.PopBackStack)
            is ProfileEditScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ProfileEditScreenContract.Event.SetSelectedBranchId -> setSelectedBranchId(event.branchId)
            is ProfileEditScreenContract.Event.SetDynamicTextFieldValue -> setDynamicTextFieldValue(event.appTextFieldValue)
            is ProfileEditScreenContract.Event.UpdateProfileSummary -> updateProfileSummary()
            is ProfileEditScreenContract.Event.OnClickAddBranch -> onClickAddBranch()
            is ProfileEditScreenContract.Event.SetShowStatePhotoPicker -> setShowStatePhotoPicker(event.isShow)
            is ProfileEditScreenContract.Event.SetSelectedImage -> setSelectedImage(event.imagePath)
            is ProfileEditScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(status = event.eventBusState)
            is ProfileEditScreenContract.Event.GetLocation -> getLocations()
            is ProfileEditScreenContract.Event.NavigateToSelectUserRole -> setSideEffect(ProfileEditScreenContract.SideEffect.NavigateToSelectUserRole)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        getProfileSummary()
    }

    private fun dismissDialogs() {
        updateState { state ->
            state.copy(
                alertDialogModel = null,
                showPhotoPicker = false,
            )
        }
    }

    private fun getProfileSummary() = viewModelScope.launch {
        getProfileSummaryUseCase.invoke()
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                val dynamicTextFieldValues: MutableList<AppTextFieldValue> = mutableListOf()
                it.profileInfo?.row?.let { rowList ->
                    dynamicTextFieldValues.addAll(
                        rowList.map { row ->
                            AppTextFieldValue(
                                id = row.parameterName ?: "",
                                value = row.text ?: "",
                            )
                        },
                    )
                }
                it.highlights?.branchesAttributes?.let { rowList ->
                    rowList.map { row ->
                        val branchId = row.branchId
                        dynamicTextFieldValues.addAll(
                            row.branchInfoRow?.map { branchInfoRow ->
                                AppTextFieldValue(
                                    id = branchId.plus(branchInfoRow.parameterName ?: ""),
                                    value = branchInfoRow.text ?: "",
                                )
                            } ?: emptyList(),
                        )
                    }
                }

                val selectedBranch = it.highlights?.branches?.fastFirstOrNull { it.isSelected == true } ?: it.highlights?.branches?.firstOrNull()
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        profileSummaryModel = it,
                        selectedBranchId = selectedBranch?.branchId,
                        dynamicTextFieldValues = dynamicTextFieldValues,
                    )
                }
            }
    }

    @Suppress("NestedBlockDepth")
    private fun updateProfileSummary() {
        val content = mutableMapOf<String, JsonElement>()
        viewState.profileSummaryModel?.profileInfo?.row?.forEach { row ->
            val parameterName = row.parameterName ?: return@forEach
            val value = viewState.dynamicTextFieldValues.firstOrNull { it.id == parameterName }?.value

            val formattedValue = if (parameterName == "birthday") {
                value?.takeIf { it.length == AppDefaults.EIGHT }?.let {
                    "${it.substring(0, 2)}.${it.substring(AppDefaults.TWO, AppDefaults.FOUR)}.${it.substring(AppDefaults.FOUR, AppDefaults.EIGHT)}"
                } ?: (value ?: "")
            } else {
                value ?: ""
            }

            content[parameterName] = JsonPrimitive(formattedValue)
        }

        val attributes: MutableList<JsonElement> = mutableListOf()
        viewState.profileSummaryModel?.highlights?.branchesAttributes?.forEach { branchesAttributes ->
            branchesAttributes.branchId?.let { branchId ->
                branchesAttributes.branchInfoRow?.forEach { row ->
                    row.parameterName?.let { parameterName ->
                        val key = branchId + parameterName
                        attributes.add(
                            buildJsonObject {
                                put("branchId", JsonPrimitive(branchId))
                                put("parameterName", JsonPrimitive(parameterName))
                                put("value", JsonPrimitive(viewState.dynamicTextFieldValues.firstOrNull { it.id == key }?.value))
                            },
                        )
                    }
                }
            }
        }
        content["attributes"] = JsonArray(attributes)

        updateProfileSummaryUseCase.invoke(JsonObject(content))
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }

    private fun setSelectedBranchId(branchId: String?) {
        updateState { state ->
            state.copy(
                selectedBranchId = branchId,
            )
        }
    }

    private fun setDynamicTextFieldValue(appTextFieldValue: AppTextFieldValue) {
        val dynamicTextFieldValues = state.value.dynamicTextFieldValues.map {
            if (it.id == appTextFieldValue.id) {
                appTextFieldValue
            } else {
                it
            }
        }
        updateState { state ->
            state.copy(
                dynamicTextFieldValues = dynamicTextFieldValues,
            )
        }
    }

    private fun onClickAddBranch() {
        setSideEffect(ProfileEditScreenContract.SideEffect.NavigateToProfileEditSelectBranch)
    }

    private fun updateEventBusStatus(status: ProfileEditEventBus.Event) {
        when (status) {
            is ProfileEditEventBus.Event.UpdateSelectedBranch -> {
                val newBranchUIModel = BranchItemUIModel(
                    branchImage = status.branchImage,
                    branchTitle = status.branchTitle,
                    branchId = status.branchId,
                    isSelected = false,
                )
                val newBranchAttributeUIModel = BranchesAttributeItemUIModel(
                    branchId = status.branchId,
                    branchInfoRow = status.branchAttribute.branchInfoRow?.map {
                        BranchInfoRowUIModel(
                            title = it.title,
                            placeholder = it.placeholder,
                            text = it.text,
                            parameterName = it.parameterName,
                            isRequired = it.isRequired,
                            type = it.type.toInfoRowType(),
                        )
                    },
                )

                val updateProfileSummaryModel = viewState.profileSummaryModel?.copy(
                    highlights = viewState.profileSummaryModel?.highlights?.copy(
                        branches = viewState.profileSummaryModel?.highlights?.branches?.toMutableList()?.apply {
                            add(newBranchUIModel)
                        },
                        branchesAttributes = viewState.profileSummaryModel?.highlights?.branchesAttributes?.toMutableList()?.apply {
                            add(newBranchAttributeUIModel)
                        },
                    ),
                )

                updateState { state ->
                    state.copy(
                        profileSummaryModel = updateProfileSummaryModel,
                    )
                }
            }
        }
    }

    private fun setSelectedImage(imagePath: String?) {
        imagePath?.let {
            updateState { state ->
                state.copy(
                    selectedImage = File(imagePath),
                )
            }
        }
        requestImageUpload()
    }

    private fun requestImageUpload() {
        val mediaFileDataList: MutableList<File> = mutableListOf()
        viewState.selectedImage?.let {
            if (it.isFile && it.exists()) {
                mediaFileDataList.add(it)
            }
        }
        val onUploadProgress: (Int) -> Unit = {
            updateState { state ->
                state.copy(
                    onUploadProgress = it,
                )
            }
        }
        val body = ImageUploadUseCaseParams(
            files = mediaFileDataList,
            onUploadProgress = onUploadProgress,
        )

        imageUploadUseCase.invoke(body)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        onUploadProgress = null,
                        isLoading = true,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isLoading = false,
                        alertDialogModel = error.toAlertDialog,
                        onUploadProgress = null,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        onUploadProgress = null,
                    )
                }
                updateProfileImage(imageUrl = it.firstOrNull())
            }
    }

    private fun updateProfileImage(imageUrl: String?) {
        val params = UpdateProfileImageRequest(
            imageUrl = imageUrl,
        )

        updateProfileImageUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isLoading = true,
                    )
                }
            }
            .onError {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        alertDialogModel = it.toAlertDialog,
                    )
                }
            }
            .callWithSuccess {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
    }

    private fun setShowStatePhotoPicker(isShow: Boolean) {
        updateState { state ->
            state.copy(
                showPhotoPicker = isShow,
            )
        }
    }

    private fun getLocations() {
        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()
            location?.let {
                val address = locationRepository.getAddressFromLocation(it)
                updateState { state ->
                    state.copy(
                        dynamicTextFieldValues = viewState.dynamicTextFieldValues.map { textField ->
                            if (textField.id == "city") {
                                textField.copy(
                                    value = "${address.city}, ${address.district}, ${address.country}",
                                )
                            } else {
                                textField
                            }
                        },
                    )
                }
            }
        }
    }
}
