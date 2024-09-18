package edu.kotlin.utils

import edu.kotlin.dtos.News
import java.time.LocalDate
import java.time.ZoneId
import kotlin.collections.ArrayList

//Списки и циклы
fun List<News>.getMostRatedNewsLoop(count: Int, period: ClosedRange<LocalDate>): List<News> {
    val filteredNews: MutableList<News> = ArrayList()
    for (news in this) {
        if (period.contains(LocalDate.ofInstant(news.publicationDate, ZoneId.systemDefault()))) {
            filteredNews.add(news)
        }
    }
    filteredNews.sortByDescending { news: News -> news.rating }
    while (filteredNews.size > count) {
        filteredNews.removeLast()
    }
    return filteredNews
}

//Последовательности
fun List<News>.getMostRatedNewsSequence(count: Int, period: ClosedRange<LocalDate>): List<News> {
    return this.asSequence()
        .filter { v -> period.contains(LocalDate.ofInstant(v.publicationDate, ZoneId.systemDefault())) }
        .sortedByDescending { v -> v.rating }
        .take(count)
        .toList()
}