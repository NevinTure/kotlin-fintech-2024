package edu.kotlin.dtos

import edu.kotlin.serializers.NewsResponseSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NewsResponseSerializer::class)
data class NewsResponse(val results: List<News>)