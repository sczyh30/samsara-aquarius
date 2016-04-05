package controllers.api

import javax.inject.{Inject, Singleton}

import service.CategoryService

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Samsara Aquarius API v1
  * Category API Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiCategoryController @Inject() (service: CategoryService) extends Controller {

  import RestConverter.categoryFormat

  def fetchAll = Action.async { implicit request =>
    service.fetchAll map { res =>
      Ok(Json.toJson(res))
    }
  }

}
