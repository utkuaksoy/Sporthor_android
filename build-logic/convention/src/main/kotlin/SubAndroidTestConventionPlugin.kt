import com.iamkurtgoz.app.utils.androidTestImplementation
import com.iamkurtgoz.app.utils.libs
import com.iamkurtgoz.app.utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SubAndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                // Test
                testImplementation(libs.findLibrary("junit4").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                testImplementation(libs.findLibrary("turbine").get())
                testImplementation(libs.findLibrary("kotest").get())
                testImplementation(libs.findLibrary("androidx.test.rules").get())
                testImplementation(libs.findLibrary("robolectric").get())
                testImplementation(libs.findLibrary("roborazzi").get())
                testImplementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                testImplementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                testImplementation(libs.findLibrary("androidx.navigation.testing").get())

                // Android Test
                androidTestImplementation(libs.findLibrary("kotest").get())
                androidTestImplementation(libs.findLibrary("mockk").get())
                androidTestImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                androidTestImplementation(libs.findLibrary("androidx.test.ext").get())
                androidTestImplementation(libs.findLibrary("androidx.test.core").get())
                androidTestImplementation(libs.findLibrary("androidx.test.espresso.core").get())
                androidTestImplementation(libs.findLibrary("androidx.test.rules").get())
                androidTestImplementation(libs.findLibrary("androidx.navigation.testing").get())
            }
        }
    }
}
