package controllers

import javax.inject.{Singleton, Inject}

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
class Application @Inject() (articleService: ArticleService) extends Controller {

  /**implicit lazy val cached = userCache.get[UserToken]("user") match {
    case Some(user) => user
    case None => null
  }*/

  def index = Action.async { implicit request =>
    articleService.fetchAll map { data =>
      Ok(views.html.index(data))
    }
  }

  def share = Action { implicit request =>
    Ok(views.html.share())
  }

  def publishShare = TODO

  def about = Action { implicit request =>
    Ok(views.html.about())
  }

}
