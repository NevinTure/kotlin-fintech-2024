package edu.kotlin.clients

import edu.kotlin.dtos.News
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KudaGoClient {

    val log: Logger = LoggerFactory.getLogger(KudaGoClient::class.java)
    private val client: HttpClient = HttpClient(Java)
    private val baseUrl: String = "https://kudago.com/public-api/v1.4/news/"
    fun getNews(count: Int = 100): List<News> {
        val responseStr: String = runBlocking {
            client.request(baseUrl) {
                method = HttpMethod.Get
                parameter("page_size", count)
                parameter(
                    "fields",
                    "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
                )
                parameter("expand", "place")
                parameter("text_format", "text")
                parameter("location", "spb")
            }.bodyAsText()
        }
        return listOf()
    }
}