package controllers.api

import javax.inject.Singleton

import base.Constants.AQUARIUS_API_VERSION

import play.api.mvc.{Action, Controller}

/**
  * Samsara Aquarius API v1
  * API Version Controller
  */
@Singleton
class ApiVersionController extends Controller {

  def version = Action { implicit request =>
    Ok(AQUARIUS_API_VERSION)
  }

}
