package entity

import play.api.libs.json.Json

import scala.language.implicitConversions

/**
  * Samsara Aquarius
  * Article row case class
  *
  * @param id info id
  * @param title info title
  * @param url info url
  * @param cid info category
  * @param updateDate info update date
  */
case class Article(id: Int, title: String = "No Title!",
                   url: String, cid: Int, updateDate: java.sql.Date)


case class ArticleResponse(article: Article, category: String)

object ArticleResponse {

  implicit val articleFormat = Json.format[Article] // JSON automated mapping

  implicit val responseFormat = Json.format[ArticleResponse]

  implicit def fitResponse(t: (Article, String)): ArticleResponse =
    ArticleResponse(t._1, t._2)
}

