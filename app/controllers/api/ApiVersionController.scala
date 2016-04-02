package controllers.api

import javax.inject.Singleton

import play.api.mvc.{Action, Controller}

/**
  * Samsara Aquarius
  * REST API Version Controller
  */
@Singleton
class ApiVersionController extends Controller {

  val api_ver = "v1"

  def version = Action { request =>
    Ok(api_ver)
  }

}
