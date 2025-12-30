import com.android.build.api.dsl.ApplicationExtension
import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension
import com.iamkurtgoz.app.extensions.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SubAndroidFlavorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
            }

            extensions.configure<SecretsPluginExtension> {
                propertiesFileName = "secrets.properties"
            }

            extensions.configure<ApplicationExtension> {
                configureFlavors(this)
            }
        }
    }
}
