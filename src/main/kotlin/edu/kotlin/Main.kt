package edu.kotlin

import edu.kotlin.clients.KudaGoClient

fun main() {
    val client: KudaGoClient = KudaGoClient()
    client.getNews(10)
}
