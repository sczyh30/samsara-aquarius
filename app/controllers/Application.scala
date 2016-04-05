package controllers

import javax.inject.{Singleton, Inject}

import entity.UserToken
import play.api.cache.{NamedCache, CacheApi}
import service.ArticleService

import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Samsara Aquarius
  * Main Page Controller
  *
  * @author sczyh30
  */
@Singleton
class Application @Inject() (@NamedCache("user-cache") userCache: CacheApi, articleService: ArticleService) extends Controller {

  implicit lazy val cached = userCache.get[UserToken]("user") match {
    case Some(user) => user
    case None => null
  }

  def index = Action.async { implicit request =>
    articleService.fetchAll map { data =>
      Ok(views.html.index(data, cached))
    }
  }

  def share = Action {
    Ok(views.html.share(cached))
  }

  def publishShare = TODO

  def about = Action {
    Ok(views.html.about(cached))
  }

}
