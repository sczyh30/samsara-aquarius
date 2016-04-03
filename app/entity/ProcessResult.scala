package entity

import play.api.libs.json.{JsValue, Json}

import scala.language.implicitConversions

/**
  * ProcessResult Entity
  */
case class ProcessResult(code: Int, msg: String)

object ProcessResult {
  implicit val resultFormat = Json.format[ProcessResult]

  implicit def resToJson(res: ProcessResult): JsValue = Json.toJson(res)
}

object Results {
  val ARTICLE_NOT_FOUND = ProcessResult(4001, "article_not_found")
}
