package entity

import play.api.libs.json.{JsValue, Json}

import scala.language.implicitConversions

/**
  * Process Result Entity
  */
case class ProcessResult(code: Int, msg: String)

object Results {
  val ARTICLE_NOT_FOUND = ProcessResult(4001, "article_not_found")
}
