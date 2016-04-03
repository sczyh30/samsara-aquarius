package controllers

import javax.inject.{Singleton, Inject}

import entity.User
import play.api.cache.{NamedCache, CacheApi}
import service.ArticleService

import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class Application @Inject() (@NamedCache("user-cache") userCache: CacheApi, articleService: ArticleService) extends Controller {

  def index = Action.async { implicit request =>
    articleService.fetchAll map { data =>
      Ok(views.html.index(data, userCache.get[User]("user") match {
        case Some(user) => user
        case None => null
      }))
    }

  }

}
