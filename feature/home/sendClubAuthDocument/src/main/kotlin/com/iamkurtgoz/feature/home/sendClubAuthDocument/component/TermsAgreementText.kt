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
package com.iamkurtgoz.feature.home.sendClubAuthDocument.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
fun TermsAgreementText(
    modifier: Modifier = Modifier,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    val termsTag = "TERMS"
    val privacyTag = "PRIVACY"

    val annotated = buildAnnotatedString {
        append("Belgeleri göndererek ")

        pushStringAnnotation(tag = termsTag, annotation = termsTag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("Kullanım Şartlarını")
        }
        pop()

        append(" ve ")

        pushStringAnnotation(tag = privacyTag, annotation = privacyTag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("Gizlilik sözleşmesini")
        }
        pop()

        append(" kabul etmiş sayılırsınız.")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        ClickableText(
            text = annotated,
            modifier = modifier
                .wrapContentWidth(),
            style = AppTheme.typography.bodyLarge.copy(
                fontSize = AppTheme.dimens.sp16,
            ),
            onClick = { offset ->
                annotated.getStringAnnotations(start = offset, end = offset)
                    .firstOrNull()
                    ?.let { span ->
                        when (span.tag) {
                            termsTag -> onTermsClick()
                            privacyTag -> onPrivacyClick()
                        }
                    }
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun TermsAgreementTextPreview() {
    AppTheme {
        AppThemeSurface {
            TermsAgreementText(
                onTermsClick = { /* Kullanım Şartlarına git */ },
                onPrivacyClick = { /* Gizlilik Sözleşmesine git */ },
            )
        }
    }
}
