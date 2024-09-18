package edu.kotlin.clients

import edu.kotlin.dtos.News
import edu.kotlin.dtos.NewsResponse
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
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
                }.bodyAsText()
            }
            log.info("Finish News query of size: $count")
            val newsResponse: NewsResponse = Json.decodeFromString(responseStr)
            return newsResponse.results
        } catch (e: Exception) {
            log.error("Request failed: ${e.message}")
            return listOf()
        }
    }
}