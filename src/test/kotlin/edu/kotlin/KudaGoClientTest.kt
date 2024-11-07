package edu.kotlin

import edu.kotlin.clients.KudaGoClient
import edu.kotlin.dtos.News
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.Instant

class KudaGoClientTest {

    @Test
    fun testGetNewsWhen200Ok() {
        //when
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(
                    """
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
                """.trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = KudaGoClient(HttpClient(mockEngine))
        val result: List<News> = client.getNews(1)

        //then
        val expectedResult = listOf(
            News(
                51433,
                "News",
                "Unknown",
                "desc",
                URI("https://localhost"),
                12,
                20,
                Instant.ofEpochSecond(1726673377)
            )
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun testGetNewsWhenError() {
        //when
        val mockEngine = MockEngine { _ ->
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

    @Test
    fun testGetNewsAsyncWhen200Ok() {
        //when
        val mockEngine = MockEngine { request ->
            when (request.url.parameters["page"]) {
                "1" -> respond(
                    content = ByteReadChannel(
                        """
                    {                    
                        "results": [
                            {
                                "id": 51433,
                                "publication_date": 1726673377,
                                "title": "News1",
                                "place": null,
                                "description": "desc",
                                "site_url": "https://localhost",
                                "favorites_count": 12,
                                "comments_count": 20
                            }
                        ]
                    }
                """.trimIndent()),
                    status = HttpStatusCode.OK
                )
                "2" -> respond(
                    content = ByteReadChannel(
                        """
                    {                    
                        "results": [
                            {
                                "id": 51433,
                                "publication_date": 1726673377,
                                "title": "News2",
                                "place": {
                                    "title": "Place"
                                },
                                "description": "desc",
                                "site_url": "https://localhost",
                                "favorites_count": 0,
                                "comments_count": 3
                            }
                        ]
                    }
                """.trimIndent()),
                    status = HttpStatusCode.OK
                )
                else -> respond(
                    content = ByteReadChannel("""{}"""),
                    status = HttpStatusCode.NotFound
                )
            }
        }
        val client = KudaGoClient(HttpClient(mockEngine))
        val result: List<News> = runBlocking { client.getNewsAsync(2, 2) }

        //then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun testGetNewsAsyncWhenError() {
        //when
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""{}""".trimIndent()),
                status = HttpStatusCode.BadRequest,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = KudaGoClient(HttpClient(mockEngine))
        val result: List<News> = runBlocking { client.getNewsAsync(1) }

        //then
        assertThat(result).isEmpty()
    }
}