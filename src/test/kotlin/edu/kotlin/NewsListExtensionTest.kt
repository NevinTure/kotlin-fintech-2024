package edu.kotlin

import edu.kotlin.dtos.News
import edu.kotlin.utils.getMostRatedNewsLoop
import edu.kotlin.utils.getMostRatedNewsSequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.LocalDate
import java.time.ZoneId

class NewsListExtensionTest {

    @Test
    fun testGetMostRatedNewsLoop() {
        //given
        val time: LocalDate = LocalDate.now()
        val news1: News = News(
            1,
            "test1",
            "place1",
            "desc1",
            URI.create("url1"),
            1,
            1,
            time.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val news2: News = News(
            2,
            "test2",
            "place2",
            "desc2",
            URI.create("url2"),
            2,
            2,
            time.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val news3: News = News(
            3,
            "test3",
            "place3",
            "desc3",
            URI.create("url3"),
            3,
            3,
            time.plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val newsList: List<News> = listOf(news1, news2, news3)

        //when
        val result: List<News> = newsList
            .getMostRatedNewsLoop(3, time..time.plusDays(2))

        //then
        val expectedResult: List<News> = listOf(news2, news1)
        assertThat(result).containsExactlyElementsOf(expectedResult)
    }

    @Test
    fun testGetMostRatedNewsSequence() {
        //given
        val time: LocalDate = LocalDate.now()
        val news1: News = News(
            1,
            "test1",
            "place1",
            "desc1",
            URI.create("url1"),
            1,
            1,
            time.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val news2: News = News(
            2,
            "test2",
            "place2",
            "desc2",
            URI.create("url2"),
            2,
            2,
            time.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val news3: News = News(
            3,
            "test3",
            "place3",
            "desc3",
            URI.create("url3"),
            3,
            3,
            time.plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
        val newsList: List<News> = listOf(news1, news2, news3)

        //when
        val result: List<News> = newsList
            .getMostRatedNewsSequence(3, time..time.plusDays(2))

        //then
        val expectedResult: List<News> = listOf(news2, news1)
        assertThat(result).containsExactlyElementsOf(expectedResult)
    }
}