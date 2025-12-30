package com.iamkurtgoz.feature.home.profile.settings.accountSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
internal fun AccountSettingsScreenContent(
    state: AccountSettingsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AccountSettingsScreenContract.Event) -> Unit,
    isPrivate: Boolean,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var localIsPrivate by remember(isPrivate) { mutableStateOf(isPrivate) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            ) {
                Text(
                    text = "Gizli Hesap",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hesabın herkese açık olduğunda, profilini ve gönderilerini Sporthor hesapları olmasa bile Sporthor'da veya Sporthor dışında herkes görebilir.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                )
            }

            Switch(
                checked = localIsPrivate,
                onCheckedChange = { isChecked ->
                    localIsPrivate = isChecked
                    setEvent(AccountSettingsScreenContract.Event.UpdateProfilePublicPrivate(isChecked))
                },
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Hesabı Sil
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDeleteDialog = true
                }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Hesabı Sil",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
            )
        }
    }

    // Hesap Silme Onay Dialog'u
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Hesabınızı silmek istediğinizden emin misiniz?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        setEvent(AccountSettingsScreenContract.Event.DeleteAccount)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.generalColors.textPrimary, // Açık yeşil renk
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = "Evet",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Hayır",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AccountSettingsScreenContent(
                state = AccountSettingsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
                isPrivate = false,
            )
        }
    }
}
