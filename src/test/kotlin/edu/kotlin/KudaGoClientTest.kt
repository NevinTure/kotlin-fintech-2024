package edu.kotlin

import edu.kotlin.clients.KudaGoClient
import edu.kotlin.dtos.News
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.Instant

class KudaGoClientTest {

    @Test
    fun testClientWhen200Ok() {
        //when
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""
                    {                    
                        "results": [
                            {
                                "id": 51433,
                                "publication_date": 1726673377,
                                "title": "News",
                                "place": null,
                                "description": "desc",
                                "site_url": "https://localhost",
                                "favorites_count": 12,
                                "comments_count": 20
                            }
                        ]
                    }
                """.trimIndent()),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = KudaGoClient(HttpClient(mockEngine))
        val result: List<News> = client.getNews(1)

        //then
        val expectedResult = listOf(News(
            51433,
            "News",
            "Unknown",
            "desc",
            URI("https://localhost"),
            12,
            20,
            Instant.ofEpochSecond(1726673377)
        ))
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun testClientWhenError() {
        //when
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{}""".trimIndent()),
                status = HttpStatusCode.BadRequest,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = KudaGoClient(HttpClient(mockEngine))
        val result: List<News> = client.getNews(1)

        //then
        assertThat(result).isEmpty()
    }
}