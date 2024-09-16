package edu.kotlin.utils

import edu.kotlin.dtos.News
import kotlinx.serialization.json.Json

class NewsDeserializer {

    fun deserialize(json: String): News {
        val news: News = Json.decodeFromString<News>(json)
        return news
    }
}