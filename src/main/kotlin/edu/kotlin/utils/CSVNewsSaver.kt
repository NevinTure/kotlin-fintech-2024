package edu.kotlin.utils

import edu.kotlin.dtos.News
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths

class CSVNewsSaver {

    private val log: Logger = LoggerFactory.getLogger(CSVNewsSaver::class.java)

    fun saveNews(pathStr: String, news: Collection<News>) {
        try {
            log.info("Start saving news in: $pathStr")
            val file: File = Paths.get(pathStr).toFile()
            BufferedWriter(FileWriter(file)).use { bw ->
                bw.write(""""id","title","place","description","siteUrl","favorites_count","comments_count","publication_date","rating"""")
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
        } catch (e: Exception) {
            log.error(e.message, e)
        }
    }
}