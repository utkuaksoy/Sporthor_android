package com.iamkurtgoz.feature.home.coachList.trainingGroups

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsScreenRoute
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelMock
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelTrainingGroup

@Composable
internal fun TrainingGroupsScreenContent(
    state: TrainingGroupsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (TrainingGroupsScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            all = 16.dp,
        ),
    ) {
        item {
            Text(
                text = "Antrenman Grupları",
                style = AppTheme.typography.subtitleLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        state.response?.clubs?.firstOrNull { it?.id == state.route.clubId }?.trainingGroups.let { trainingGroups ->
            items(
                items = trainingGroups?.filterNotNull() ?: listOf(),
                key = { it.id ?: "" },
                itemContent = { trainingGroup ->
                    ClubTrainingGroupItem(
                        trainingGroup = trainingGroup,
                        isDeleteMode = state.isDeleteMode,
                        onClick = {
                            setEvent(TrainingGroupsScreenContract.Event.NavigateToUpdateCoachScreen(state.route.clubId, trainingGroup.id))
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                },
            )
        }

        state.response?.clubs?.firstOrNull { it?.id == state.route.clubId }?.coaches?.let { coaches ->
            if (coaches.isNotEmpty()) {
                item {
                    Text(
                        text = "Antrenman Grupları",
                        style = AppTheme.typography.subtitleLarge,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }

            items(
                items = coaches.filterNotNull(),
                key = { it.id ?: "" },
                itemContent = { trainingGroup ->
                    UserRow(
                        userHeaderData = trainingGroup.imageUrl,
                        isHeaderUser = true,
                        title = trainingGroup.name,
                        subTitle = arrayOf(trainingGroup.username ?: ""),
                        trailingContent = {
                            if (state.isDeleteMode) {
                                AppRadioButton.Secondary(
                                    selected = state.selectedUserList.any { it.id == trainingGroup.id },
                                    onClick = {
                                        setEvent(TrainingGroupsScreenContract.Event.SetSelectedUserList(trainingGroup))
                                    },
                                )
                            }
                        },
                        onClickAction = {
                            if (state.isDeleteMode) {
                                setEvent(TrainingGroupsScreenContract.Event.SetSelectedUserList(trainingGroup))
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                },
            )
        }
    }
}

@Composable
fun ClubTrainingGroupItem(
    trainingGroup: GetClubsAndDetailsDomainModelTrainingGroup,
    isDeleteMode: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.generalColors.foregroundDisabled,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(enabled = !isDeleteMode) { onClick() }
                .padding(12.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(resourcesR.drawable.img_error_user_image),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = trainingGroup.name ?: "-",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.subtitleLarge,
            )

            if (!isDeleteMode) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go",
                    tint = Color.Gray,
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            TrainingGroupsScreenContent(
                state = TrainingGroupsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListTrainingGroupsScreenRoute.Route(
                        clubId = "687997d99f0b5e1be60d78c0",
                    ),
                    response = GetClubsAndDetailsDomainModelMock.mock(),
                ),
                setEvent = { },
            )
        }
    }
}
