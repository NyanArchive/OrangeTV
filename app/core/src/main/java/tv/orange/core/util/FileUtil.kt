package tv.orange.core.util

import java.io.File

object FileUtil {
    private const val COPY_BUFFER_SIZE = 8 * 1024

    fun File.copyTo(outFile: File) {
        this.inputStream().use { i ->
            outFile.outputStream().use { output ->
                i.copyTo(output, COPY_BUFFER_SIZE)
            }
        }
    }
}