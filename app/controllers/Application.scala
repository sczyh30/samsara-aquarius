package controllers

import javax.inject.Inject

import service.InfoDataService

import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject() (infoDataService: InfoDataService) extends Controller {

  def index = Action.async { implicit request =>
    infoDataService.getAll map { infoList =>
      Ok(views.html.index(infoList))
    }

  }

}
