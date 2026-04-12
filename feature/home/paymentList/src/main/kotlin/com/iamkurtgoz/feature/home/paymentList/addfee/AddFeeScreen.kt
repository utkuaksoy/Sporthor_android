package com.iamkurtgoz.feature.home.paymentList.addfee

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddFeeScreen(
    trainingGroupId: String,
    navigateUp: () -> Unit,
    viewModel: AddFeeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var paymentDate by remember { mutableStateOf(LocalDate.now()) }
    var showPaymentDatePicker by remember { mutableStateOf(false) }
    val paymentDateUiFormatter = remember {
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
    }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isSinglePayment by remember { mutableStateOf(true) }
    var isRepeatCountExpanded by remember { mutableStateOf(false) }
    var selectedRepeatCount by remember { mutableStateOf(3) }
    var amountText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    val paymentDateValue = paymentDate.toString()
    val canSubmit =
        !state.isLoading &&
            (state.selectedUsers.isNotEmpty() || state.selectedGroups.isNotEmpty()) &&
            amountText.isNotBlank() &&
            descriptionText.isNotBlank() &&
            paymentDateValue.isNotBlank() &&
            (isSinglePayment || selectedRepeatCount > 0)

    LaunchedEffect(trainingGroupId) {
        viewModel.setEvent(AddFeeViewModel.Event.Initialize(trainingGroupId))
    }

    viewModel.sideEffect.observeSideEffect { effect ->
        when (effect) {
            AddFeeViewModel.SideEffect.NavigateUp -> navigateUp()
        }
    }

    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(onClick = navigateUp)
                },
                centerContent = {
                    AppToolbarFields.Title(text = stringResource(resourcesR.string.addfeescreen_title))
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            AddFeeSelectedItemsSection(
                selectedUsers = state.selectedUsers,
                selectedGroups = state.selectedGroups,
                onAddClick = {
                    viewModel.setEvent(AddFeeViewModel.Event.SetPersonOrGroupPickerVisible(true))
                },
                onRemoveUser = { user ->
                    viewModel.setEvent(AddFeeViewModel.Event.ToggleUserSelection(user))
                },
                onRemoveGroup = { groupId ->
                    viewModel.setEvent(AddFeeViewModel.Event.ToggleTrainingGroupSelection(groupId))
                },
            )

            Text(
                text = stringResource(resourcesR.string.addfeescreen_category),
                style = AppTheme.typography.subtitleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            ExposedDropdownMenuBox(
                expanded = isCategoryExpanded,
                onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
            ) {
                OutlinedTextField(
                    value = state.categories
                        .firstOrNull { it.id == state.selectedCategoryId }
                        ?.name
                        .orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                    shape = RoundedCornerShape(12.dp),
                )

                ExposedDropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false },
                ) {
                    state.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name.orEmpty()) },
                            onClick = {
                                viewModel.setEvent(AddFeeViewModel.Event.SelectCategory(category.id))
                                isCategoryExpanded = false
                            },
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(999.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FeeTypeButton(
                    text = stringResource(resourcesR.string.addfeescreen_single_payment),
                    isSelected = isSinglePayment,
                    onClick = { isSinglePayment = true },
                    modifier = Modifier.weight(1f),
                )
                FeeTypeButton(
                    text = stringResource(resourcesR.string.addfeescreen_recurring_payment),
                    isSelected = !isSinglePayment,
                    onClick = { isSinglePayment = false },
                    modifier = Modifier.weight(1f),
                )
            }

            if (isSinglePayment) {
                AddFeeFieldLabel(
                    text = stringResource(resourcesR.string.addfeescreen_payment_date),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPaymentDatePicker = true },
                ) {
                    AddFeeInputField(
                        value = paymentDate.format(paymentDateUiFormatter),
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AddFeeFieldLabel(
                            text = stringResource(resourcesR.string.addfeescreen_first_payment_date),
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPaymentDatePicker = true },
                        ) {
                            AddFeeInputField(
                                value = paymentDate.format(paymentDateUiFormatter),
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(0.42f)) {
                        AddFeeFieldLabel(
                            text = stringResource(resourcesR.string.addfeescreen_repeat_count),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = isRepeatCountExpanded,
                            onExpandedChange = { isRepeatCountExpanded = !isRepeatCountExpanded },
                        ) {
                            AddFeeInputField(
                                value = selectedRepeatCount.toString(),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRepeatCountExpanded)
                                },
                            )

                            ExposedDropdownMenu(
                                expanded = isRepeatCountExpanded,
                                onDismissRequest = { isRepeatCountExpanded = false },
                            ) {
                                repeat(12) { index ->
                                    val count = index + 1
                                    DropdownMenuItem(
                                        text = { Text(count.toString()) },
                                        onClick = {
                                            selectedRepeatCount = count
                                            isRepeatCountExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AddFeeFieldLabel(
                text = stringResource(resourcesR.string.addfeescreen_amount),
            )
            AddFeeInputField(
                value = amountText,
                onValueChange = {
                    amountText = it.filter { char ->
                        char.isDigit() || char == ',' || char == '.'
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = {
                    Text(text = stringResource(resourcesR.string.addfeescreen_amount_placeholder))
                },
            )

            AddFeeFieldLabel(
                text = stringResource(resourcesR.string.addfeescreen_description),
            )
            AddFeeInputField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = stringResource(resourcesR.string.addfeescreen_description_placeholder))
                },
            )

            Button(
                onClick = {
                    viewModel.setEvent(
                        AddFeeViewModel.Event.CreateFee(
                            isSinglePayment = isSinglePayment,
                            paymentDate = paymentDateValue,
                            repeatCount = selectedRepeatCount,
                            amountText = amountText,
                            description = descriptionText,
                        ),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA7F45C),
                    contentColor = Color(0xFF101010),
                ),
                enabled = canSubmit,
            ) {
                Text(
                    text = stringResource(resourcesR.string.addfeescreen_create_payment),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        if (state.showPersonOrGroupPicker) {
            AddFeeUserOrGroupPickerSheet(
                state = state,
                setEvent = viewModel::setEvent,
                onDismissRequest = {
                    viewModel.setEvent(AddFeeViewModel.Event.SetPersonOrGroupPickerVisible(false))
                },
            )
        }

        if (state.isLoading) {
            AppLoadingDialog()
        }

        if (showPaymentDatePicker) {
            FeeDatePickerDialog(
                initialDate = paymentDate,
                onDismissRequest = { showPaymentDatePicker = false },
                onDateSelected = { selectedDate ->
                    paymentDate = selectedDate
                    showPaymentDatePicker = false
                },
            )
        }
    }
}

@Composable
private fun AddFeeFieldLabel(
    text: String,
) {
    Text(
        text = text,
        style = AppTheme.typography.subtitleLarge,
        fontWeight = FontWeight.SemiBold,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddFeeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent { focusState ->
                if (!readOnly && focusState.isFocused) {
                    coroutineScope.launch {
                        delay(250)
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        trailingIcon = trailingIcon,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedTextColor = AppTheme.colors.generalColors.textPrimary,
            unfocusedTextColor = AppTheme.colors.generalColors.textPrimary,
            disabledTextColor = AppTheme.colors.generalColors.textPrimary,
            focusedPlaceholderColor = Color(0xFF6A6A6A),
            unfocusedPlaceholderColor = Color(0xFF6A6A6A),
            disabledPlaceholderColor = Color(0xFF6A6A6A),
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeeDatePickerDialog(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val zoneId = remember { ZoneId.systemDefault() }
    val initialSelectedDateMillis = remember(initialDate, zoneId) {
        initialDate
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
    )
    val confirmEnabled by remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                        val selectedDate = Instant
                            .ofEpochMilli(selectedDateMillis)
                            .atZone(zoneId)
                            .toLocalDate()
                        onDateSelected(selectedDate)
                    }
                },
                enabled = confirmEnabled,
            ) {
                Text(text = "Tamam")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = AppTheme.colors.generalColors.textPrimary,
                ),
            ) {
                Text(text = "İptal")
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}

@Composable
private fun AddFeeSelectedItemsSection(
    selectedUsers: List<AddFeeSelectableUserUi>,
    selectedGroups: List<AddFeeSelectableGroupUi>,
    onAddClick: () -> Unit,
    onRemoveUser: (AddFeeSelectableUserUi) -> Unit,
    onRemoveGroup: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(12.dp),
    ) {
        Text(
            text = stringResource(resourcesR.string.addfeescreen_person_or_group_section),
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        selectedUsers.forEachIndexed { index, user ->
            AddFeeSelectedItemRow(
                title = user.name,
                subtitle = null,
                imageUrl = user.imageUrl,
                fallbackText = user.name.take(2),
                onRemove = { onRemoveUser(user) },
            )

            if (index != selectedUsers.lastIndex || selectedGroups.isNotEmpty()) {
                HorizontalDivider(color = Color(0xFFF1F1F1))
            }
        }

        selectedGroups.forEachIndexed { index, group ->
            AddFeeSelectedItemRow(
                title = group.name,
                subtitle = group.subtitle.ifBlank { null },
                imageUrl = group.imageUrl,
                fallbackText = group.name.take(1),
                onRemove = { onRemoveGroup(group.id) },
            )

            if (index != selectedGroups.lastIndex) {
                HorizontalDivider(color = Color(0xFFF1F1F1))
            }
        }

        if (selectedUsers.isNotEmpty() || selectedGroups.isNotEmpty()) {
            HorizontalDivider(color = Color(0xFFF1F1F1))
        }

        Spacer(modifier = Modifier.height(4.dp))

        AddFeePersonPickerRow(
            onClick = onAddClick,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun AddFeeSelectedItemRow(
    title: String,
    subtitle: String?,
    imageUrl: String?,
    fallbackText: String,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddFeeSelectedAvatar(
            imageUrl = imageUrl,
            fallbackText = fallbackText,
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.subtitleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodyMedium,
                    color = Color(0xFF6A6A6A),
                )
            }
        }

        IconButton(
            onClick = onRemove,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color(0xFF4A4A4A),
            )
        }
    }
}

@Composable
private fun AddFeeSelectedAvatar(
    imageUrl: String?,
    fallbackText: String,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(100.dp),
            ),
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AppAsyncImageLoader.Load(
                data = imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = fallbackText.ifBlank { "?" },
                    style = AppTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A5A5A),
                )
            }
        }
    }
}

@Composable
private fun FeeTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.Transparent,
            contentColor = if (isSelected) Color(0xFF101010) else Color(0xFF707070),
        ),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
