package com.iamkurtgoz.app.extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import com.iamkurtgoz.app.enums.AppFlavor
import com.iamkurtgoz.app.enums.FlavorDimension

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: AppFlavor) -> Unit = { },
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            AppFlavor.values().forEach { appFlavor ->
                register(appFlavor.name) {
                    dimension = appFlavor.dimension.name
                    flavorConfigurationBlock(this, appFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (appFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = appFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
