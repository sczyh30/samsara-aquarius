package controllers

import javax.inject.{Singleton, Inject}

import entity.form.InfoForm
import service.{CategoryService, ArticleService}
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
class AdminController @Inject() (articleService: ArticleService, categoryService: CategoryService) extends Controller {

  def addInfoPage() = Action.async { implicit request =>
    categoryService.fetchAll map { categories =>
      Ok(views.html.admin.addInfo(InfoForm.form, categories))
    }
  }

  /*def processOKPage() = Action {
    Ok(views.html.admin.processOK)
  }*/

  def addInfoProcess() = Action.async { implicit request =>
    InfoForm.form.bindFromRequest.fold(
      errorForm => {
        println(errorForm)
        Future.successful(BadRequest(views.html.admin.addInfo(errorForm, Seq())))
      },
      data => {
        articleService.addInfo(data) map { res =>
          Redirect(routes.AdminController.addInfoPage())
        }
      })
  }

}
