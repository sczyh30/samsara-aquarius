package controllers

import javax.inject.{Inject, Singleton}

import service.{ArticleService, CategoryService}

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Samsara Aquarius Route
  * Category Controller
  *
  * @author sczyh30
  */
@Singleton
class CategoryController @Inject() (service: CategoryService, ars: ArticleService) extends Controller {

  def index = Action.async { implicit request =>
    service.fetchAllWithCount map { res =>
      Ok(views.html.categories(res))
    }
  }

  def certainId(cid: Int) = TODO

  def certain(abbr: String) = Action.async { implicit request =>
    ars.fetchByCAbbr(abbr) map {
      case Some(data) =>
        Ok(views.html.articles(Left(data)))
      case None =>
        NotFound(views.html.error.NotFound())
    }
  }

}
