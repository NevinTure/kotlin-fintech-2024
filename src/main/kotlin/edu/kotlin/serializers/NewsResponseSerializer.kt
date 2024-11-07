package edu.kotlin.serializers

import edu.kotlin.dtos.News
import edu.kotlin.dtos.NewsResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object NewsResponseSerializer: KSerializer<NewsResponse> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("NewsResponse") {
        element("data", buildClassSerialDescriptor("data") {
            element("results", ListSerializer(News.serializer()).descriptor)
        })
    }
    override fun serialize(encoder: Encoder, value: NewsResponse) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(buildJsonObject {
            put("results", JsonArray(value.results.map {
                news -> encoder.json.encodeToJsonElement(news)
            }))
        })
    }

    override fun deserialize(decoder: Decoder): NewsResponse {
        require(decoder is JsonDecoder)
        val root = decoder.decodeJsonElement()
        val results: List<News> = root.jsonObject["results"]
            ?.jsonArray
            ?.map {
                news -> decoder.json.decodeFromJsonElement(news)
            } ?: emptyList()
        return NewsResponse(results)
    }
}