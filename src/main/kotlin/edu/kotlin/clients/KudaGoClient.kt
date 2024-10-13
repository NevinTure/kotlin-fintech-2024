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
        val context = newFixedThreadPoolContext(workers, "news-client-thread")
        val channel = Channel<NewsResponse>()
        val perPage: Int = Math.ceilDiv(count, workers)
        for (i in 1..workers) {
            launch(context) {
                channel.send(run {
                    try {
                        log.info("Start News query of size: $perPage on page: $i")
                        val response = client.request(BASE_URL) {
                            method = HttpMethod.Get
                            parameter("page_size", perPage)
                            parameter("fields", FIELDS)
                            parameter("text_format", "text")
                            parameter("location", "spb")
                            parameter("page", i)
                            parameter("expand", "place")
                        }
                        if (response.contentType() == ContentType.Text.Xml) {
                            log.error("Request failed on page: $i")
                            return@run NewsResponse(emptyList())
                        }
                        log.info("Finish News query of size: $perPage on page: $i")
                        return@run Json.decodeFromString<NewsResponse>(response.bodyAsText())
                    } catch (e: Exception) {
                        log.error("Request failed: ${e.message} on page: $i", e)
                        return@run NewsResponse(emptyList())
                    }
                })
            }
        }
        val news: MutableList<News> = mutableListOf()
        launch {
            repeat(workers) {
                try {
                    val response = channel.receive()
                    news.addAll(response.results)
                } catch (e: Exception) {
                    log.error(e.message, e)
                }
            }
            channel.close()
        }
        return@coroutineScope news
    }
}