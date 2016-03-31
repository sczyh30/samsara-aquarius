package controllers

import javax.inject.{Singleton, Inject}

import service.InfoDataService

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class Application @Inject() (infoDataService: InfoDataService) extends Controller {

  def index = Action.async { implicit request =>
    infoDataService.fetchAll map { infoList =>
      Ok(views.html.index(infoList))
    }

  }

}
