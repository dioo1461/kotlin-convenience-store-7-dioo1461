package util

import java.io.File

object FileLoader {
    fun loadLines(filePath: String): List<String> {
        val file = File(filePath)
        return file.readLines()
    }
}