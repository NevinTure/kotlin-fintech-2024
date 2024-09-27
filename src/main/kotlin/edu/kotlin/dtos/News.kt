package edu.kotlin.dtos

import edu.kotlin.serializers.NewsSerializer
import kotlinx.serialization.Serializable
import java.net.URI
import java.time.Instant
import kotlin.math.exp

@Serializable(with = NewsSerializer::class)
data class News(
    val id: Long,
    val title: String,
    val place: String,
    val description: String,
    val siteUrl: URI,
    val favoritesCount: Long,
    val commentsCount: Long,
    val publicationDate: Instant,
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}
