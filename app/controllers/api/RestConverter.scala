package controllers.api

import entity._

import play.api.libs.json.{JsValue, Json}

import scala.language.implicitConversions

/**
  * Samsara Aquarius
  * REST Data Converter
  */
object RestConverter {

  implicit val categoryFormat = Json.format[Category] // JSON automated mapping
  implicit val articleFormat = Json.format[Article]
  //implicit val commentFormat = Json.format[Comment]
  implicit val restArticleFormat = Json.format[RestArticle]
  implicit val resultFormat = Json.format[ProcessResult]
  implicit val userInfoFormat = Json.format[UserInfo]
  implicit val userTokenFormat = Json.format[UserToken]

  implicit def resToJson(res: ProcessResult): JsValue = Json.toJson(res)

  case class RestArticle(id: Int, title: String,
                         url: String, category: Category, updateDate: java.sql.Date)

  /**
    * Typeclass for REST Article entity
    */
  implicit class RestArticleConverter(t: (Article, Category)) { // Article with Category
    def fit = RestArticle(t._1.id, t._1.title, t._1.url, t._2, t._1.updateDate)
  }

  implicit class ApiUserConverter(user: User) {
    def fit = UserInfo(user.uid, user.username, user.joinDate, user.tips, user.website)
  }
}
