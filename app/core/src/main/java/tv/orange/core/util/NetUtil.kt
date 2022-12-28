package tv.orange.core.util

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
}