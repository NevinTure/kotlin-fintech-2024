package edu.kotlin.clients

import edu.kotlin.dtos.News
import edu.kotlin.dtos.NewsResponse
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KudaGoClient(private val client: HttpClient = HttpClient(Java)) {

    private val log: Logger = LoggerFactory.getLogger(KudaGoClient::class.java)

    companion object {
        private const val BASE_URL: String = "https://kudago.com/public-api/v1.4/news/"
        private const val FIELDS: String =
            "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
    }

    fun getNews(count: Int = 100): List<News> {
        val responseStr: String?
        try {
            log.info("Start News query of size: $count")
            responseStr = runBlocking {
                client.request(BASE_URL) {
                    method = HttpMethod.Get
                    parameter("page_size", count)
                    parameter("fields", FIELDS)
                    parameter("text_format", "text")
                    parameter("location", "spb")
                    parameter("expand", "place")
                }.bodyAsText()
            }
            log.info("Finish News query of size: $count")
            val newsResponse: NewsResponse = Json.decodeFromString(responseStr)
            return newsResponse.results
        } catch (e: Exception) {
            log.error("Request failed: ${e.message}", e)
            return listOf()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getNewsAsync(count: Int = 100, workers: Int = 10): List<News> = coroutineScope {
        newFixedThreadPoolContext(workers, "news-thread-pool")
        val channel = Channel<NewsResponse>()
        val perPage: Int = 100.coerceAtMost(Math.ceilDiv(count, workers))
        val pages = Math.ceilDiv(count, perPage)
        for (i in 1..pages) {
            launch {
                try {
                    log.info("Start News query of size: $perPage on page: $i")
                    channel.send(Json.decodeFromString(client.request(BASE_URL) {
                        method = HttpMethod.Get
                        parameter("page_size", perPage)
                        parameter("fields", FIELDS)
                        parameter("text_format", "text")
                        parameter("location", "spb")
                        parameter("page", i)
                        parameter("expand", "place")
                    }.bodyAsText()))
                } catch (e: Exception) {
                    log.error("Request failed: ${e.message}", e)
                }
            }
        }
        val news: MutableList<News> = mutableListOf()
        launch {
            repeat(pages) {
                val response = channel.receive()
                news.addAll(response.results)
            }
            channel.close()
        }
        return@coroutineScope news
    }
}