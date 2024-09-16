package edu.kotlin.dtos

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URI
import java.time.OffsetDateTime
import kotlin.math.exp

@Serializable
data class News(
    val id: Long,
    val title: String,
    val place: String,
    val description: String,
    @Contextual val siteUrl: URI,
    val favoritesCount: Long,
    val commentsCount: Long,
    @Contextual val publishedDate: OffsetDateTime,
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}
