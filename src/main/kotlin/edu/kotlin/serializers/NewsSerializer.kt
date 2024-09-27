package edu.kotlin.serializers

import edu.kotlin.dtos.News
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import java.net.URI
import java.time.Instant

object NewsSerializer: KSerializer<News> {


    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("News") {
        element<Long>("id")
        element<String>("title")
        element<String>("place")
        element<String>("description")
        element("site_url", URISerializer.descriptor)
        element<Long>("favorites_count")
        element<Long>("comments_count")
        element("publication_date", InstantSerializer.descriptor)
    }

    override fun serialize(encoder: Encoder, value: News) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(buildJsonObject {
            put("id", value.id)
            put("title", value.title)
            put("place", value.place)
            put("description", value.description)
            put("site_url", encoder.json.encodeToJsonElement(URISerializer, value.siteUrl))
            put("favorites_count", value.favoritesCount)
            put("comments_count", value.commentsCount)
            put("publication_date",
                encoder.json.encodeToJsonElement(InstantSerializer, value.publicationDate))
        })
    }

    override fun deserialize(decoder: Decoder): News {
        return decoder.decodeStructure(descriptor) {
            var id: Long? = null
            var title: String? = null
            var place: String? = null
            var description: String? = null
            var siteUrl: URI? = null
            var favouritesCount: Long? = null
            var commentsCount: Long? = null
            var publicationDate: Instant? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> id = decodeLongElement(descriptor, 0)
                    1 -> title = decodeStringElement(descriptor, 1)
                    2 -> place = decodeSerializableElement(descriptor, 2, PlaceSerializer)
                    3 -> description = decodeStringElement(descriptor, 3)
                    4 -> siteUrl = decodeSerializableElement(descriptor, 4, URISerializer)
                    5 -> favouritesCount = decodeLongElement(descriptor, 5)
                    6 -> commentsCount = decodeLongElement(descriptor, 6)
                    7 -> publicationDate = decodeSerializableElement(descriptor, 7, InstantSerializer)
                    else -> throw SerializationException("Unexpected index $index")
                }
            }

            News(
                requireNotNull(id),
                requireNotNull(title),
                requireNotNull(place),
                requireNotNull(description),
                requireNotNull(siteUrl),
                requireNotNull(favouritesCount),
                requireNotNull(commentsCount),
                requireNotNull(publicationDate)
            )
        }
    }
}