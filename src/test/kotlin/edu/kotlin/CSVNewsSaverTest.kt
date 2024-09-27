package edu.kotlin

import edu.kotlin.dtos.News
import edu.kotlin.utils.CSVNewsSaver
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.time.Instant

class CSVNewsSaverTest {

    @Test
    fun testSaveNews(@TempDir dir: File) {
        //given
        val file: File = File(dir, "test.csv")
        val newsList: List<News> = listOf(
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
        val saver: CSVNewsSaver = CSVNewsSaver()

        //when
        saver.saveNews(file.path, newsList)
        val result = Files.readAllLines(file.toPath()).joinToString("\n")

        //then
        val exceptedResult = """
            "id","title","place","description","siteUrl","favorites_count","comments_count","publication_date","rating"
            51433,News,Unknown,desc,https://localhost,12,20,${Instant.ofEpochSecond(1726673377)},0.6390927451628512
            """.trimIndent()
        assertThat(result).contains(exceptedResult)
    }

    @Test
    fun testSaveNewsWhenException(@TempDir dir: File) {
        //given
        val file: File = File(dir, "test.csv")
        val newsList: List<News> = listOf(
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
        val saver: CSVNewsSaver = CSVNewsSaver()

        //when
        Files.writeString(file.toPath(), "Already existed")

        //then
        assertThatIllegalArgumentException().isThrownBy { saver.saveNews(file.path, newsList) }
    }
}