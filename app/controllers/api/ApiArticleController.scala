package controllers.api

import entity.Article
import service.ArticleService

import javax.inject.{Singleton, Inject}

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json


/**
  * Samsara Aquarius
  * Article API Controller
  */
@Singleton
class ApiArticleController @Inject() (service: ArticleService) extends Controller {

  implicit val articleReads = Json.reads[Article] // JSON automated mapping

  def latest = Action.async { implicit request =>
    service.fetchAll map { data =>
      Ok(data)
    }
  }

  def fetch(aid: Int) = Action.async { implicit request =>
    service.fetch(aid) map {
      case Some(x) => Ok(x)
      case None => Ok(404)
    }
  }

}
