package edu.kotlin

import edu.kotlin.clients.KudaGoClient

fun main() {
    val client: KudaGoClient = KudaGoClient()
    println(client.getNews(10))
}
