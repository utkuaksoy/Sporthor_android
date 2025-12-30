import org.gradle.api.GradleException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun gitCommitCount(): Int {
    return processCommand("git rev-list --all --count")?.toInt()
        ?: throw GradleException("Unable to get number of commits. Make sure git is initialized.")
}

@Suppress("NewApi")
fun processCommand(command: String): String? {
    val cmd = command.split("\\s+".toRegex()).toTypedArray()
    val process = ProcessBuilder(*cmd)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()
    return process.inputStream.bufferedReader().readLine()?.trim()
}

fun computedVersionCode(): Int {
    val date = Date()
    val formattedDate = SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(date)
    return formattedDate.toLong().toInt()
}
