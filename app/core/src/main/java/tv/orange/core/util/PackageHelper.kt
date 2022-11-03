package tv.orange.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object PackageHelper {
    private const val ANDROID_PACKAGE_ARCHIVE = "application/vnd.android.package-archive"

    fun installApk(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        } else {
            Uri.fromFile(file)
        }

        intent.setDataAndType(uri, ANDROID_PACKAGE_ARCHIVE)
        context.startActivity(intent)
    }

    fun canInstallApk(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }
}
