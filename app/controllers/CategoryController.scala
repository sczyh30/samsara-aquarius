package controllers

import javax.inject.{Inject, Singleton}

import base.Constants._
import entity.Page
import service.{ArticleService, CategoryService}

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Category Controller
  *
  * @author sczyh30
  */
@Singleton
class CategoryController @Inject() (service: CategoryService, ars: ArticleService) extends Controller {

  def index = Action.async { implicit request =>
    for {
      data <- service.fetchWithPage(0)
      pages <- service.calcPage
    } yield Ok(views.html.categories(data, Page(1, pages, "/c")))
  }

  def page(page: Int) = Action.async { implicit request =>
    service.calcPage flatMap { pages =>
      if (IS_VALIDATE_PAGE(page, pages)) {
        service.fetchWithPage(WRAP_PAGE(page)) map { data =>
          Ok(views.html.categories(data, Page(page, pages, "/c")))
        }
      }
      else
        Future.successful(NotFound(views.html.error.NotFound()))
    }
  }

  def certain(abbr: String) = Action.async { implicit request =>
    ars.fetchByCAbbr(abbr) map {
      case Some(data) =>
        Ok(views.html.articles(Left(data)))
      case None =>
        NotFound(views.html.error.NotFound())
    }
  }

}
