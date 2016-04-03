package controllers.api

import entity._

import play.api.libs.json.{JsValue, Json}

import scala.language.implicitConversions

/**
  * REST Data Converter
  */
object RestConverter {

  implicit val categoryFormat = Json.format[Category] // JSON automated mapping
  implicit val articleFormat = Json.format[Article]
  implicit val restArticleFormat = Json.format[RestArticle]
  implicit val resultFormat = Json.format[ProcessResult]

  implicit def resToJson(res: ProcessResult): JsValue = Json.toJson(res)

  case class RestArticle(id: Int, title: String = "None",
                         url: String, category: Category, updateDate: java.sql.Date)

  implicit class RestArticleConverter(t: (Article, Category)) {
    def fit = RestArticle(t._1.id, t._1.title, t._1.url, t._2, t._1.updateDate)
  }
}
