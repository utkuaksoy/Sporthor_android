package com.iamkurtgoz.feature.home.paymentList.addfee

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun AddFeePersonPickerRow(
    onClick: () -> Unit,
    selectionSummary: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = resourcesR.drawable.plusbutton),
            contentDescription = null, // istersen localized string ver
            tint = Color.Unspecified,  // SVG kendi rengini korusun
        )

        Spacer(modifier = Modifier.width(16.dp))

        androidx.compose.foundation.layout.Column {
            Text(
                text = stringResource(resourcesR.string.addfeescreen_person_or_group),
                style = AppTheme.typography.heading05,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
            )

            if (!selectionSummary.isNullOrBlank()) {
                Text(
                    text = selectionSummary,
                    style = AppTheme.typography.bodyMedium,
                    color = Color(0xFF6A6A6A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

}
@PreviewAppWithNightMode
@Composable
private fun AddFeePersonPickerRowPreview() {
    AppTheme {
        AppThemeSurface {
            AddFeePersonPickerRow(
                modifier = Modifier.padding(16.dp),
                onClick = {},
                selectionSummary = "3 kişi seçildi",
            )
        }
    }
}
