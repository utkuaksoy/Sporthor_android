import com.iamkurtgoz.app.utils.api
import com.iamkurtgoz.app.utils.implementation
import com.iamkurtgoz.app.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SubAndroidCoilConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                api(libs.findLibrary("coil.kt").get())
                api(libs.findLibrary("coil.kt.compose").get())
                implementation(libs.findLibrary("coil.kt.svg").get())
                implementation(libs.findLibrary("coil.kt.video").get())
                implementation(libs.findLibrary("coil.kt.gif").get())
                implementation(libs.findLibrary("coil.kt.network.okhttp").get())
            }
        }
    }
}
