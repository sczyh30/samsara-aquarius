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
  implicit val responseFormat = Json.format[ArticleResponse]
  implicit val resultFormat = Json.format[ProcessResult]

  implicit def resToJson(res: ProcessResult): JsValue = Json.toJson(res)
}
