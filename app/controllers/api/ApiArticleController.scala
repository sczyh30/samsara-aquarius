package controllers.api

import entity.{ProcessResult, Results, ArticleResponse, Article}
import service.ArticleService
import entity.ProcessResult.resToJson

import javax.inject.{Singleton, Inject}

import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions


/**
  * Samsara Aquarius
  * Article API Controller
  */
@Singleton
class ApiArticleController @Inject() (service: ArticleService) extends Controller {

  implicit class fitResponse(t: (Article, String)) {
    def fit = ArticleResponse(t._1, t._2)
  }

  /*def latest = Action.async { implicit request =>
    service.fetchAll map { data =>
      Ok(Json.toJson(data))
    }
  }*/

  def fetch(aid: Int) = Action.async { implicit request =>
    service.fetch(aid) map {
      case Some(x) => Ok(Json.toJson(x.fit))
      case None => NotFound(Json.toJson(Results.ARTICLE_NOT_FOUND))
    }
  }

}
