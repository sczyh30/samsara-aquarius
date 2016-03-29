package controllers

import java.sql.Date
import javax.inject.Inject

import entity.InfoData
import entity.form.InfoForm
import play.api.mvc._
import service.InfoDataService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Admin Controller
  *
  * @author sczyh30
  */
class AdminController @Inject() (infoDataService: InfoDataService) extends Controller {

  def addInfoPage() = Action { implicit request =>
    Ok(views.html.admin.addInfo(InfoForm.form))
  }

  /*def processOKPage() = Action {
    Ok(views.html.admin.processOK)
  }*/

  def addInfoProcess() = Action.async { implicit request =>
    InfoForm.form.bindFromRequest.fold(
      errorForm => {println(errorForm);Future.successful(Ok(views.html.admin.addInfo(errorForm)))},
      data => {
        val newInfo = InfoData(0, data.title, url = data.url,
          cid = data.cid, updateDate = data.updateDate)
        println(newInfo)
        infoDataService.addInfo(newInfo) map { res =>
          Redirect(routes.AdminController.addInfoPage())
        }
      })
  }

}
