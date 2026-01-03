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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.theme.AppTheme

@Composable
fun UpdateGroupTopRow(
    groupImage: String?,
    groupName: String?,
    onGroupNameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Profil Fotoğrafı
        AppAsyncImageLoader.Load(
            data = groupImage, // Buraya gerçek görsel URL'sini koy
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop,
            contentDescription = "Grup Profil Fotoğrafı",
        )

        Spacer(modifier = Modifier.height(16.dp))

        // "Grup Adı" yazısı
        Column(
            modifier = Modifier.clickable { onGroupNameClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Group Adı",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Grup adı (bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = groupName ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )

                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupInfoScreenPreview() {
    AppTheme {
        UpdateGroupTopRow(
            groupImage = "https://example.com/group_image.jpg",
            groupName = "Grup Adı",
            onGroupNameClick = {},
        )
    }
}
