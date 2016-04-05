package controllers.api

import entity.Results
import service.{SearchService, ArticleService}
import RestConverter.{resultFormat, articleFormat, RestArticleConverter}

import javax.inject.{Singleton, Inject}

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions


/**
  * Samsara Aquarius API v1
  * Article API Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiArticleController @Inject() (service: ArticleService, qs: SearchService) extends Controller {

  def latest = Action.async { implicit request =>
    service.latest map { data =>
      Ok(Json.toJson(data map (_.fit)))
    }
  }

  def all = Action.async { implicit request =>
    service.fetchAll map { data =>
      Ok(Json.toJson(data map (_.fit)))
    }
  }

  def fetch(aid: Int) = Action.async { implicit request =>
    service.fetch(aid) map {
      case Some(x) => Ok(Json.toJson(x.fit))
      case None => NotFound(Json.toJson(Results.ARTICLE_NOT_FOUND))
    }
  }

  def byName(q: String) = Action.async { implicit request =>
    qs.byName(q) map { res => // v1 version
      Ok(Json.toJson(res))
    }
  }

}
