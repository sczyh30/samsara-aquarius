package controllers

import javax.inject.{Singleton, Inject}

import entity.form.InfoForm
import service.InfoDataService
import utils.FormConverter.infoConvert

import play.api.mvc._


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Admin Controller
  *
  * @author sczyh30
  */
@Singleton
class AdminController @Inject() (infoDataService: InfoDataService) extends Controller {

  def addInfoPage() = Action { implicit request =>
    Ok(views.html.admin.addInfo(InfoForm.form))
  }

  /*def processOKPage() = Action {
    Ok(views.html.admin.processOK)
  }*/

  def addInfoProcess() = Action.async { implicit request =>
    InfoForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.admin.addInfo(errorForm)))
      },
      data => {
        infoDataService.addInfo(data) map { res =>
          Redirect(routes.AdminController.addInfoPage())
        }
      })
  }

}
