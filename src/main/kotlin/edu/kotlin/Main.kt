package edu.kotlin

import edu.kotlin.clients.KudaGoClient
import edu.kotlin.utils.CSVNewsSaver

fun main() {
    val client: KudaGoClient = KudaGoClient()
    val newsList = client.getNews(10)
    val csvSaver: CSVNewsSaver = CSVNewsSaver()
    csvSaver.saveNews("test.csv", newsList)
}
