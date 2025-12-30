package com.iamkurtgoz.feature.home.coachList

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
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListScreenRoute
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelClub
import com.iamkurtgoz.domain.model.response.GetClubsAndDetailsDomainModelMock

@Composable
internal fun CoachListScreenContent(
    state: CoachListScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CoachListScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            all = 16.dp,
        ),
    ) {
        item {
            Text(
                text = "Kulüp Seç",
                style = AppTheme.typography.subtitleLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        state.response?.clubs?.let { clubs ->
            items(
                items = clubs.filterNotNull(),
                key = { it.id ?: "" },
                itemContent = { club ->
                    ClubItem(
                        club = club,
                        onClick = {
                            setEvent.invoke(CoachListScreenContract.Event.NavigateToTrainingGroups(club.id))
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                },
            )
        }
    }
}

@Composable
fun ClubItem(club: GetClubsAndDetailsDomainModelClub, onClick: () -> Unit) {
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
                .clickable { onClick() }
                .padding(12.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(club.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = club.name ?: "-",
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.subtitleLarge,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go",
                tint = Color.Gray,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CoachListScreenContent(
                state = CoachListScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeCoachListScreenRoute.Route,
                    response = GetClubsAndDetailsDomainModelMock.mock(),
                ),
                setEvent = { },
            )
        }
    }
}
