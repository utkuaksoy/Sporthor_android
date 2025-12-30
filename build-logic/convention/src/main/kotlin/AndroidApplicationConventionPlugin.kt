import com.android.build.api.dsl.ApplicationExtension
import com.iamkurtgoz.app.extensions.configureKotlinAndroid
import com.iamkurtgoz.app.extensions.registerModuleCreatorTask
import com.iamkurtgoz.app.extensions.registerPrePushTask
import com.iamkurtgoz.app.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.iamkurtgoz.sub.timber")
                apply("com.iamkurtgoz.sub.hilt")
                apply("com.iamkurtgoz.sub.ktlint")
                apply("com.iamkurtgoz.sub.detekt")
                apply("com.iamkurtgoz.sub.spotless")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
            }
            registerPrePushTask()
            registerModuleCreatorTask()
        }
    }
}
