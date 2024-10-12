package edu.kotlin

import edu.kotlin.clients.KudaGoClient
import edu.kotlin.dtos.News
import edu.kotlin.utils.newsPrettyPrint
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val client = KudaGoClient()
//    val news = client.getNews(3)
//    println(news)
//    val output = newsPrettyPrint(news).toString()
//    Files.writeString(Path.of("test.html"), output)
    val news = runBlocking { client.getNewsAsync(20, 5) }
    println(news)
}
