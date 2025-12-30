import com.iamkurtgoz.app.utils.androidTestImplementation
import com.iamkurtgoz.app.utils.implementation
import com.iamkurtgoz.app.utils.libs
import com.iamkurtgoz.app.utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SubAndroidTimberConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                // App
                implementation(libs.findLibrary("timber").get())

                // Test
                testImplementation(libs.findLibrary("timber").get())

                // Android Test
                androidTestImplementation(libs.findLibrary("timber").get())
            }
        }
    }
}
