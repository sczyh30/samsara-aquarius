package controllers

import javax.inject.{Singleton, Inject}

import entity.Page
import service.ArticleService
import base.Constants.{WRAP_PAGE, IS_VALIDATE_PAGE}

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Main Page Controller
  *
  * @author sczyh30
  */
@Singleton
class Application @Inject() (articleService: ArticleService) extends Controller {

  /**implicit lazy val cached = userCache.get[UserToken]("user") match {
    * case Some(user) => user
    * case None => null
    * }*/

  def index() = Action.async { implicit request =>
    for {
      data <- articleService.fetchWithPage(0)
      pages <- articleService.calcPage
    } yield Ok(views.html.index(data, Page(1, pages, "/p")))
  }

  def page(page: Int) = Action.async { implicit request =>
    articleService.calcPage flatMap { pages =>
      if (IS_VALIDATE_PAGE(page, pages)) {
        articleService.fetchWithPage(WRAP_PAGE(page)) map { data =>
          Ok(views.html.index(data, Page(page, pages, "/p")))
        }
      }
      else
        Future.successful(NotFound(views.html.error.NotFound()))
    }
    /*for {
      data <- articleService.fetchWithPage(WRAP_PAGE(page))
      pages <- articleService.calcPage
    } yield {
      if (page > 0 && page <= pages)
        Ok(views.html.index(data, Page(page, pages, "/page")))
      else
        NotFound(views.html.error.NotFound())
    }*/
  }

  def share = Action { implicit request =>
    Ok(views.html.share())
  }

  def publishShare = TODO

  def about = Action { implicit request =>
    Ok(views.html.about())
  }

  def notFound = Action { implicit request =>
    NotFound(views.html.error.NotFound())
  }

}
