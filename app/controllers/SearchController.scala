package controllers

import javax.inject.{Singleton, Inject}

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import service.SearchService

/**
  * Samsara Aquarius Route
  * Search Controller
  *
  * @author sczyh30
  */
@Singleton
class SearchController @Inject() (service: SearchService) extends Controller {

  /**
    * Search articles by article partial name
    * <code>/search?q=???</code>
    * @param q article partial name
    */
  def search(q: String) = Action.async { implicit request =>
    service.byNameWithCategory(q) map { res =>
      Ok(views.html.articles(Right(res → q)))
    }
  }

}
