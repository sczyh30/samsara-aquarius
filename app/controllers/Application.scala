package controllers

import service.InfoDataService

import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {

  def index = Action.async { implicit request =>
    InfoDataService.getAll map { infoList =>
      Ok(views.html.index(infoList))
    }

  }

}
