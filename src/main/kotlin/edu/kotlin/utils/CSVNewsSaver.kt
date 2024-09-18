package edu.kotlin.utils

import edu.kotlin.dtos.News
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths

class CSVNewsSaver {

    companion object {
        private const val FIELDS = """"id","title","place","description","siteUrl","favorites_count","comments_count","publication_date","rating""""
    }

    private val log: Logger = LoggerFactory.getLogger(CSVNewsSaver::class.java)

    fun saveNews(pathStr: String, news: Collection<News>) {
        val file: File = Paths.get(pathStr).toFile()
        if (file.exists() || file.isDirectory) {
            throw IllegalArgumentException("File already exist: $pathStr")
        }
        log.info("Start saving news in: $pathStr")
        BufferedWriter(FileWriter(file)).use { bw ->
            bw.write(FIELDS)
            bw.newLine()
            news.forEach {
                bw.write(
                    "${it.id},${it.title},${it.place}" +
                            ",${it.description},${it.siteUrl},${it.favoritesCount}" +
                            ",${it.commentsCount},${it.publicationDate},${it.rating}"
                )
                bw.newLine()
            }
        }
        log.info("Finish saving news in: $pathStr")
    }
}