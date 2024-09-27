package edu.kotlin.utils

import edu.kotlin.dtos.News
import java.time.LocalDate
import java.time.ZoneId

fun newsPrettyPrint(newsList: List<News>) =
    ownDSL {
        header(level = 1) {
            + "NEWS Pretty Print"
        }
        text {
            for (news in newsList) {
                p {
                    b {
                        + "${news.title} (Id: ${news.id})"
                    }
                    + "Place: ${news.place}"
                    + "Description: ${news.description}"
                    a(news.siteUrl.toString()) {
                        + "Source"
                    }
                    + "Likes: ${news.favoritesCount} Comments: ${news.commentsCount}"
                    + "Publication date: ${LocalDate.ofInstant(news.publicationDate, ZoneId.systemDefault())}"
                }
            }
        }
    }