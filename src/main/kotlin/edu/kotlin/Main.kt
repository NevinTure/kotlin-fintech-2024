package edu.kotlin

import edu.kotlin.clients.KudaGoClient
import kotlinx.coroutines.runBlocking

fun main() {
    val client = KudaGoClient()
    val start = System.currentTimeMillis()
    runBlocking { client.getNewsAsync(5000, 6) }
//    repeat(5) {
//        client.getNews(1000)
//    }
    val end = System.currentTimeMillis()
    println(end - start)
    //8973 7754 7041 usual
    //3888 3558 3570 3 workers
    //3253 3463 3864 5 workers
    //3557 3619 3322 6 workers
    //| mode        | single thread | async 3 workers | async 5 workers | async 6 workers |
    //|-------------|---------------|-----------------|-----------------|-----------------|
    //| avg time ms | 7922          | 3672            | 3526            | 3499            |
}
