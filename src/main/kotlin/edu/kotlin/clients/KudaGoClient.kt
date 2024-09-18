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

class KudaGoClient {

    private val log: Logger = LoggerFactory.getLogger(KudaGoClient::class.java)
    private val client: HttpClient = HttpClient(Java)
    private val baseUrl: String = "https://kudago.com/public-api/v1.4/news/"

    fun getNews(count: Int = 100): List<News> {
        log.info("Start News query of size: $count")
        val responseStr: String = runBlocking {
            val request = client.request(baseUrl) {
                method = HttpMethod.Get
                parameter("page_size", count)
                parameter(
                    "fields",
                    "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
                )
                parameter("text_format", "text")
                parameter("location", "spb")
            }
            if (request.status.value != 200) {
                log.error("Request failed: ${request.status}")
                return@runBlocking ""
            } else {
                return@runBlocking request.bodyAsText()
            }
        }
        log.info("Finish News query of size: $count")
        val newsResponse: NewsResponse = Json.decodeFromString(responseStr)
        return newsResponse.results
    }


}