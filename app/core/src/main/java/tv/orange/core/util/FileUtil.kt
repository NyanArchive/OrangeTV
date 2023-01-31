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

    fun File.deleteDir() {
        deleteRecursive(this)
    }

    fun File.dirSize(): Long {
        if (isFile) {
            return length()
        }
        var size = 0L
        for (child in this.listFiles() ?: emptyArray()) {
            size += child.dirSize()
        }

        return size
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        for (child in fileOrDirectory.listFiles() ?: emptyArray()) {
            deleteRecursive(child)
        }

        fileOrDirectory.delete()
    }
}