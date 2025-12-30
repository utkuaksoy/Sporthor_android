package com.iamkurtgoz.feature.home.profile.settings.aboutUs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun AboutUsScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = resourcesR.drawable.img_sporthor_logo), // örn: sporthor_logo.png
                contentDescription = "Sporthor Logo",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .height(40.dp),
            )
        }

        // Alt içerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        ) {
            Text(
                text = "Sporthor: Sporcular ve Antrenörler İçin Yeni Nesil Sosyal Platform",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Spor sadece sahada değil, her anında bir takım ruhu ve disiplin gerektirir. Sporthor, sporcular ve antrenörler için özel olarak tasarlanmış bir sosyal medya platformudur. Burada sporcular; takım arkadaşları ve antrenörleriyle bağlantıda kalırken, performanslarını geliştirmek için görevler alır ve antrenman takvimlerini yönetir. Sporcular için: Sporthor, futbol, basketbol, voleybol gibi farklı branşlardaki sporcuların hem kulüpleri hem de arkadaşlarıyla etkileşim kurmasını sağlar. Sporcular antrenmanlarını planlar, başarılarını paylaşır ve takım ruhunu her an hisseder. Antrenörler için: Sporthor, antrenörlerin sporcularına görevler atamasını, takvimler oluşturmasını ve gelişim süreçlerini yakından takip etmesini kolaylaştırır. Takım içi iletişimi güçlendirerek antrenmanları daha verimli hale getirir. Sporu dijital dünyayla buluşturan Sporthor, hem bireysel gelişimi hem de takım başarısını destekleyen yeni nesil bir deneyim sunuyor.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AboutUsScreenContent()
        }
    }
}
