package tv.orange.core.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import tv.orange.core.di.module.NetworkModule
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object NetUtil {
    private const val NET_BUFFER_SIZE = 4 * 1024

    private const val PROGRESS_UPDATE_TIMEOUT_MS = 500

    interface DownloadCallback {
        fun onProgressUpdate(progress: Int, downloadedBytes: Int, totalBytes: Int)
        fun isCanceled(): Boolean
    }

    fun download(url: URL, file: File, callback: DownloadCallback) {
        try {
            val connection = url.openConnection()
            connection.connect()

            val buf = ByteArray(NET_BUFFER_SIZE)
            connection.getInputStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    var current = 0
                    var count: Int
                    var progress: Int
                    var currentUpdate: Long
                    var lastUpdate = System.currentTimeMillis()
                    val contentLength = connection.contentLength
                    while (inputStream.read(buf).also { count = it } != -1) {
                        current += count
                        progress = ((current.toFloat().div(contentLength)) * 100).toInt()
                        currentUpdate = System.currentTimeMillis()
                        if (currentUpdate - lastUpdate > PROGRESS_UPDATE_TIMEOUT_MS) {
                            callback.onProgressUpdate(
                                progress,
                                current,
                                connection.contentLength
                            )
                            lastUpdate = currentUpdate
                        }

                        outputStream.write(buf, 0, count)
                        if (callback.isCanceled()) {
                            break
                        }
                    }
                    outputStream.flush()
                }
            }

        } catch (th: Throwable) {
            if (!callback.isCanceled()) {
                throw th
            }
        }
    }

    fun getFileSize(url: String): Long {
        var connection: HttpsURLConnection? = null
        return try {
            connection = (URL(url).openConnection() as HttpsURLConnection).apply {
                requestMethod = "HEAD"
                connectTimeout = NetworkModule.DEFAULT_OKHTTP_TIMEOUT.toInt()
                readTimeout = NetworkModule.DEFAULT_OKHTTP_TIMEOUT.toInt()
            }

            connection.contentLength.toLong()
        } catch (th: Throwable) {
            th.printStackTrace()
            -1
        } finally {
            connection?.disconnect()
        }
    }

    fun downloadClipLegacy(context: Context, url: String, filename: String) {
        val fixedFilename = normalizeFilename(filename, '_')
        try {
            val uri = Uri.parse(replaceTwitchCdnDomain(url))
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(uri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setMimeType("video/mp4")
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "/twitch/$fixedFilename.mp4"
                )
            }
            downloadManager.enqueue(request)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun normalizeFilename(filename: String, replace: Char): String {
        if (filename.isBlank()) {
            return ""
        }
        with(StringBuilder()) {
            filename.forEach { c ->
                if (isValidFatChar(c)) {
                    append(c)
                } else {
                    append(replace)
                }
            }

            return toString()
        }
    }

    private fun replaceTwitchCdnDomain(orgUrl: String): String = orgUrl.replace(
        "production.assets.clips.twitchcdn.net",
        "clips-media-assets2.twitch.tv"
    )

    private fun isValidFatChar(c: Char): Boolean {
        return if (c.code <= 0x1f) {
            false
        } else when (c) {
            '"', '*', '/', ':', '<', '>', '?', '\\', '|' -> false
            else -> true
        }
    }
}