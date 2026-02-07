package com.iamkurtgoz.feature.home.inviteGroupMembers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.commonui.component.user.UserRowFields
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.component.CoachRoleSelectionBottomSheet
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.CoachRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.InviteGroupMembersTab
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.UserRelationUIItemModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.iamkurtgoz.core.resources.R as resourcesR

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
internal fun InviteGroupMembersScreenContent(
    state: InviteGroupMembersScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            Card(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .clickable { setEvent(InviteGroupMembersScreenContract.Event.NavigateToSelectGroup) },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF212A1D),
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.spacing.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppAsyncImageLoader.Load(
                        data = state.route.model.clubLogo ?: "",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = state.route.model.groupName ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.colors.generalColors.textWhite,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        painter = painterResource(id = resourcesR.drawable.img_arrow_right),
                        contentDescription = null,
                        tint = AppTheme.colors.generalColors.textWhite,
                    )
                }
            }
        }

        if (!state.route.model.isEdit) {
            item { Spacer(modifier = Modifier.height(AppTheme.spacing.spacingHuge)) }

            item {
                StaffActionCard(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    title = "Antrenör Ekibini Ekle",
                    subtitle = "Antrenör ekibini ekle antrenman grubunu birlikte yönet",
                    buttonText = "Antrenör Ekle",
                    onClick = {
                        setEvent(
                            InviteGroupMembersScreenContract.Event.OpenCreateFlowSheet(
                                InviteGroupMembersTab.STAFF,
                            ),
                        )
                    },
                )
            }

            item { Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium)) }

            item {
                StaffActionCard(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    title = "Sporcu Ekle",
                    subtitle = "Antrenman grubuna sporcuları ekle",
                    buttonText = "Sporcu Ekle",
                    onClick = {
                        setEvent(
                            InviteGroupMembersScreenContract.Event.OpenCreateFlowSheet(
                                InviteGroupMembersTab.PLAYERS,
                            ),
                        )
                    },
                )
            }

            item { Spacer(modifier = Modifier.height(AppTheme.spacing.spacingHuge)) }
        } else {
            item {
                InviteGroupMembersTabBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.spacing.spacingHuge,
                            vertical = AppTheme.spacing.spacingMedium,
                        ),
                    selectedTab = state.selectedTab,
                    onTabSelected = {
                        setEvent(
                            InviteGroupMembersScreenContract.Event.OnChangeTab(it),
                        )
                    },
                )
            }

            val relation = state.followingList

            if (relation != null) {


                when (state.selectedTab) {
                    InviteGroupMembersTab.PLAYERS -> {
                        val baseUsers = relation.users?.toPersistentList() ?: persistentListOf()

                        val extraUsersFromSelected = state.selectedUserList
                            .filterNotNull()
                            .filter { ui ->
                                ui.role == InviteGroupMembersTab.PLAYERS &&
                                    ui.id != null &&
                                    baseUsers.none { it.id == ui.id }
                            }

                        val displayPlayers: List<Any> = baseUsers + extraUsersFromSelected
                        if (displayPlayers.isEmpty()) {
                            item {
                                EmptyPlayerInfoMessage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = AppTheme.spacing.spacingHuge,
                                            vertical = AppTheme.spacing.spacingHuge,
                                        ),
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = displayPlayers,
                                key = { index, item ->
                                    when (item) {
                                        is UserRelationUIItemModel -> "player-${item.id}-$index"
                                        is SocialSearchUIItemModel -> "player-extra-${item.id}-$index"
                                        else -> "player-unknown-$index"
                                    }
                                },
                            ) { index, item ->
                                when (item) {
                                    is UserRelationUIItemModel -> {
                                        SwipeToDeleteRow(
                                            modifier = Modifier.padding(
                                                top = if (index == AppDefaults.ZERO)
                                                    AppTheme.spacing.spacingMedium
                                                else
                                                    AppTheme.spacing.spacingNone,
                                            ),
                                            onDelete = {
                                                setEvent(
                                                    InviteGroupMembersScreenContract.Event.RemoveGroupMember(
                                                        item = item,
                                                        tab = InviteGroupMembersTab.PLAYERS,
                                                    ),
                                                )
                                            },
                                        ) { contentModifier ->
                                            UserRow(
                                                modifier = contentModifier,
                                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                                isHeaderUser = true,
                                                title = item.name,
                                                subTitle = arrayOf("Sporcu"),
                                                onClickAction = {
                                                    setEvent(
                                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                            item = item,
                                                            tab = state.selectedTab,
                                                        ),
                                                    )
                                                },
                                            )
                                        }
                                    }

                                    is SocialSearchUIItemModel -> {
                                        SwipeToDeleteRow(
                                            modifier = Modifier.padding(
                                                top = if (index == AppDefaults.ZERO)
                                                    AppTheme.spacing.spacingMedium
                                                else
                                                    AppTheme.spacing.spacingNone,
                                            ),
                                            onDelete = {
                                                setEvent(
                                                    InviteGroupMembersScreenContract.Event.RemoveGroupMember(
                                                        item = item,
                                                        tab = InviteGroupMembersTab.PLAYERS,
                                                    ),
                                                )
                                            },
                                        ) { contentModifier ->
                                            UserRow(
                                                modifier = contentModifier,
                                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                                userHeaderData = item.image ?: item.name.getUserNameFirstChar(),
                                                isHeaderUser = true,
                                                title = item.name,
                                                subTitle = arrayOf("Sporcu"),
                                                onClickAction = {
                                                    setEvent(
                                                        InviteGroupMembersScreenContract.Event.ChangeSelectedUserState(
                                                            item = item,
                                                            tab = state.selectedTab, // o anki tab
                                                        ),
                                                    )
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }

                    InviteGroupMembersTab.STAFF -> {
                        // Takip ettiklerim (antrenörler)
                        val baseCoaches = relation.coaches?.toPersistentList() ?: persistentListOf()

                        val extraCoachesFromSelected = state.selectedUserList
                            .filterNotNull()
                            .filter { ui ->
                                ui.role == InviteGroupMembersTab.STAFF &&
                                    // YENİ
                                    ui.id != null &&
                                    baseCoaches.none { it.id == ui.id }
                            }

                        val displayCoaches: List<Any> = baseCoaches + extraCoachesFromSelected

                        if (displayCoaches.isEmpty()) {
                            item {
                                EmptyCoachInfoMessage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = AppTheme.spacing.spacingHuge,
                                            vertical = AppTheme.spacing.spacingHuge,
                                        ),
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = displayCoaches,
                                key = { index, item ->
                                    when (item) {
                                        is CoachRelationUIItemModel -> "coach-${item.id}-$index"
                                        is SocialSearchUIItemModel -> "coach-extra-${item.id}-$index"
                                        else -> "coach-unknown-$index"
                                    }
                                },
                            ) { index, item ->
                                when (item) {
                                    is CoachRelationUIItemModel -> {
                                        val selectedItem = state.selectedUserList
                                            .filterNotNull()
                                            .find { it.id == item.id }
                                        val role = selectedItem?.attribute ?: item.summary ?: ""

                                        SwipeToDeleteRow(
                                            modifier = Modifier.padding(
                                                top = if (index == AppDefaults.ZERO)
                                                    AppTheme.spacing.spacingMedium
                                                else
                                                    AppTheme.spacing.spacingNone,
                                            ),
                                            onDelete = {
                                                setEvent(
                                                    InviteGroupMembersScreenContract.Event.RemoveGroupMember(
                                                        item = item,
                                                        tab = InviteGroupMembersTab.STAFF,
                                                    ),
                                                )
                                            },
                                        ) { contentModifier ->
                                            UserRow(
                                                modifier = contentModifier,
                                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                                userHeaderData = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                                isHeaderUser = true,
                                                title = item.name,
                                                subTitle = if (role.isNotEmpty()) arrayOf(role) else arrayOf(),
                                                trailingContent = {
                                                    UserRowFields.rightArrow()
                                                },
                                                onClickAction = {
                                                    setEvent(
                                                        InviteGroupMembersScreenContract.Event.OpenCoachRoleSelection(
                                                            coach = selectedItem ?: item,
                                                        ),
                                                    )
                                                },
                                            )
                                        }
                                    }

                                    is SocialSearchUIItemModel -> {
                                        val selectedItem = state.selectedUserList
                                            .filterNotNull()
                                            .find { it.id == item.id }
                                        val role = selectedItem?.attribute ?: item.attribute ?: ""

                                        SwipeToDeleteRow(
                                            modifier = Modifier.padding(
                                                top = if (index == AppDefaults.ZERO)
                                                    AppTheme.spacing.spacingMedium
                                                else
                                                    AppTheme.spacing.spacingNone,
                                            ),
                                            onDelete = {
                                                setEvent(
                                                    InviteGroupMembersScreenContract.Event.RemoveGroupMember(
                                                        item = item,
                                                        tab = InviteGroupMembersTab.STAFF,
                                                    ),
                                                )
                                            },
                                        ) { contentModifier ->
                                            UserRow(
                                                modifier = contentModifier,
                                                contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
                                                userHeaderData = item.image ?: item.name.getUserNameFirstChar(),
                                                isHeaderUser = true,
                                                title = item.name,
                                                subTitle = if (role.isNotEmpty()) arrayOf(role) else arrayOf(),
                                                trailingContent = {
                                                    UserRowFields.rightArrow()
                                                },
                                                onClickAction = {
                                                    setEvent(
                                                        InviteGroupMembersScreenContract.Event.OpenCoachRoleSelection(
                                                            coach = selectedItem ?: item,
                                                        ),
                                                    )
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showCoachRoleSelectionBottomSheet) {
        CoachRoleSelectionBottomSheet(
            state = state,
            setEvent = setEvent,
        )
    }
}

@Composable
private fun SwipeToDeleteRow(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    val deleteWidth = 72.dp
    val density = LocalDensity.current
    val maxSwipe = with(density) { -deleteWidth.toPx() }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = maxSwipe / 2f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds(),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Red),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Box(
                modifier = Modifier
                    .width(deleteWidth)
                    .fillMaxHeight()
                    .clickable(onClick = onDelete),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Sil",
                    tint = Color.White,
                )
            }
        }

        content(
            Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .zIndex(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(maxSwipe, 0f)
                        },
                        onDragEnd = {
                            offsetX = if (offsetX > swipeThreshold) 0f else maxSwipe
                        },
                    )
                },
        )
    }
}
@Composable
private fun EmptyPlayerInfoMessage(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "i",
                color = Color.White,
                style = AppTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingLarge))

        Text(
            text = "Antrenman grubunuzda sporcu bulunmamaktadır. Sporcu eklemek için \"Yeni Sporcu Ekle\" butonuna tıklayınız.",
            style = AppTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF4A4A4A),
        )
    }
}

@Composable
private fun EmptyCoachInfoMessage(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "i",
                color = Color.White,
                style = AppTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingLarge))

        Text(
            text = "Antreman grubunuzda antrenör bulunmamaktadır. Antrenör eklemek için \"Yeni Antrenör Ekle\" butonuna tıklayınız.",
            style = AppTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF4A4A4A),
        )
    }
}

/**
 * Oyuncular / Teknik Kadro tabbar
 */
@Composable
private fun InviteGroupMembersTabBar(
    selectedTab: InviteGroupMembersTab,
    onTabSelected: (InviteGroupMembersTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTab.ordinal,
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = Color.Black,
                height = 2.dp,
            )
        },
        divider = {
            Divider(color = Color(0xFFE5E5E5), thickness = 1.dp)
        },
    ) {
        Tab(
            selected = selectedTab == InviteGroupMembersTab.PLAYERS,
            onClick = { onTabSelected(InviteGroupMembersTab.PLAYERS) },
        ) {
            Text(
                text = "Oyuncular",
                modifier = Modifier
                    .padding(vertical = 12.dp),
                style = AppTheme.typography.bodyMedium,
                color = if (selectedTab == InviteGroupMembersTab.PLAYERS) {
                    Color.Black
                } else {
                    Color(0xFF9E9E9E)
                },
            )
        }

        Tab(
            selected = selectedTab == InviteGroupMembersTab.STAFF,
            onClick = { onTabSelected(InviteGroupMembersTab.STAFF) },
        ) {
            Text(
                text = "Teknik Kadro",
                modifier = Modifier
                    .padding(vertical = 12.dp),
                style = AppTheme.typography.bodyMedium,
                color = if (selectedTab == InviteGroupMembersTab.STAFF) {
                    Color.Black
                } else {
                    Color(0xFF9E9E9E)
                },
            )
        }
    }
}

@Composable
private fun StaffActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E5E5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.spacingLarge,
                vertical = AppTheme.spacing.spacingMedium,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.spacing.spacingLarge,
                    vertical = 8.dp,
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelLarge,
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
            InviteGroupMembersScreenContent(
                state = InviteGroupMembersScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenInviteGroupMemberRoute(
                        model = HomeScreenInviteGroupMemberScreenNavigationModel(
                            isEdit = true,
                            clubId = null,
                            clubName = "Sporthor Voleybol Kulübü",
                            clubLogo = null,
                            groupId = null,
                            groupName = "Başlangıç - Miniminik Erkek",
                        ),
                    ),
                    selectedTab = InviteGroupMembersTab.PLAYERS,
                    // searchResultList artık kullanılmıyor, gerekirse burada null geçebilirsin
                ),
                setEvent = { },
            )
        }
    }
}
